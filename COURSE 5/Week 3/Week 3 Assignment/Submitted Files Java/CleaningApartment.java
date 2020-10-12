import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;

public class CleaningApartment {

    private final InputReader reader;
    private final OutputWriter writer;

    public CleaningApartment(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new CleaningApartment(reader, writer).run();
        writer.writer.flush();
    }

    static class Edge {
        int from;
        int to;
    }

    class ConvertHamiltonPathToSat {
        int numVertices;
        Edge[] edges;
        boolean isSatisfiable;

        ConvertHamiltonPathToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            isSatisfiable = false;
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquiSatisfiableSatFormula() {
            StringBuilder clauses = new StringBuilder();
            int countClause = 0;
            countClause += inPath(clauses);
            countClause += vertexInPath(clauses);
            countClause += onceInPath(clauses);
            countClause += onceInEachPosition(clauses);
            countClause += edgeConnection(clauses);

            writer.printf(countClause + " " + numVertices * numVertices + "\n");
            writer.printf(clauses.toString());

        }

        private int inPath(StringBuilder clauses) {
            int c = 0;
            for (int i = 1; i <= numVertices * numVertices; i += numVertices) {
                for (int j = 0; j < numVertices; j++) {
                    clauses.append(i + j).append(" ");
                }
                clauses.append("0\n");
                c++;
            }
            return c;
        }

        private int vertexInPath(StringBuilder clauses) {
            int c = 0;
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 0; j < numVertices * numVertices; j += numVertices) {
                    clauses.append(i + j).append(" ");
                }
                clauses.append("0\n");
                c++;
            }
            return c;
        }

        private int onceInPath(StringBuilder clauses) {
            int c = 0;
            for (int i = 1; i <= numVertices * numVertices; i += numVertices) {
                for (int j = 0; j < numVertices; j++) {
                    for (int k = j + 1; k < numVertices; k++) {
                        clauses.append(-(i + j)).append(" ");
                        clauses.append(-(i + k)).append(" ");
                        clauses.append("0\n");
                        c++;
                    }
                }
            }
            return c;
        }

        private int onceInEachPosition(StringBuilder clauses) {
            int c = 0;
            for (int i = 1; i <= numVertices; i++) {
                for (int j = 0; j < numVertices * numVertices; j += numVertices) {
                    for (int k = j + numVertices; k < numVertices * numVertices; k += numVertices) {
                        clauses.append(-(i + j)).append(" ");
                        clauses.append(-(i + k)).append(" ");
                        clauses.append("0\n");
                        c++;
                    }
                }
            }
            return c;
        }

        private int edgeConnection(StringBuilder clauses) {
            boolean[][] adj = new boolean[numVertices][numVertices];
            for (Edge e : edges) {
                adj[e.from - 1][e.to - 1] = true;
                adj[e.to - 1][e.from - 1] = true;
            }
            int c = 0;
            for (int i = 0; i < numVertices; i++) {
                for (int j = i + 1; j < numVertices; j++) {
                    if (!adj[i][j]) {
                        for (int k = 0; k < numVertices - 1; k++) {
                            clauses.append(-(i * numVertices + k + 1));
                            clauses.append(" ");
                            clauses.append(-(j * numVertices + k + 2));
                            clauses.append(" 0\n");
                            c++;
                            clauses.append(-(j * numVertices + k + 1));
                            clauses.append(" ");
                            clauses.append(-(i * numVertices + k + 2));
                            clauses.append(" 0\n");
                            c++;
                        }
                    }
                }
            }
            return c;
        }

    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertHamiltonPathToSat converter = new ConvertHamiltonPathToSat(n, m);
        for (int i = 0; i < m; ++i) {
            converter.edges[i].from = reader.nextInt();
            converter.edges[i].to = reader.nextInt();
        }

        converter.printEquiSatisfiableSatFormula();
    }

    static class InputReader {

        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    static class OutputWriter {

        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}