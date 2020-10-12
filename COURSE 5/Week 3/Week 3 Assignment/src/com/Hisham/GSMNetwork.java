package com.Hisham;

import java.io.*;
import java.util.Locale;
import java.util.StringTokenizer;

// *take m Vertices and turn into m*3 variables (X) (3 colors)
// *each row corresponds to a color
// *first set of m (#ofVertices) clauses are COLUMN values OR
// *(e.g., X1 OR X1+m OR X1+2m)
// *This means that each Vertex has to have a color (actually, one or more colors)
// *second set of n*3 (#ofEdges *3) clauses are all negative adjacent ROW pairs OR
// *(e.g., -X1 OR -X2, -X2 OR -X3, -X1 OR -Xm, -X1+m OR -X2+m, etc.)
// *This means that adjacent Vertices in any row cannot have the same color

public class GSMNetwork {

    private final InputReader reader;
    private final OutputWriter writer;

    public GSMNetwork(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new GSMNetwork(reader, writer).run();
        writer.writer.flush();
    }

    static class Edge {
        int from;
        int to;
    }

    class ConvertGSMNetworkProblemToSat {
        int numVertices;
        Edge[] edges;

        ConvertGSMNetworkProblemToSat(int n, int m) {
            numVertices = n;
            edges = new Edge[m];
            for (int i = 0; i < m; ++i) {
                edges[i] = new Edge();
            }
        }

        void printEquiSatisfiableSatFormula() {
            // clauses & variables
            writer.printf("%d %d\n", numVertices + edges.length * 3, numVertices * 3);

            // true coloring rows
            for (int i = 1; i <= numVertices; i++) {
                writer.printf("%d %d %d %d\n", i, i + numVertices, i + numVertices * 2, 0);
            }

            // false coloring rows
            for (Edge e : edges) {
                writer.printf("%d %d %d\n", -e.from, -e.to, 0);
                writer.printf("%d %d %d\n", -e.from - numVertices, -e.to - numVertices, 0);
                writer.printf("%d %d %d\n", -e.from - numVertices * 2, -e.to - numVertices * 2, 0);
            }
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertGSMNetworkProblemToSat converter = new ConvertGSMNetworkProblemToSat(n, m);
        for (int i = 0; i < m; ++i) {
            converter.edges[i].from =  reader.nextInt();
            converter.edges[i].to =  reader.nextInt();
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