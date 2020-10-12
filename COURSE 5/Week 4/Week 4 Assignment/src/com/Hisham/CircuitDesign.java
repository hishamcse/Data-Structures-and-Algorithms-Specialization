package com.Hisham;

import java.io.*;
import java.util.*;

public class CircuitDesign {

    private final InputReader reader;
    private final OutputWriter writer;

    public CircuitDesign(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                InputReader reader = new InputReader(System.in);
                OutputWriter writer = new OutputWriter(System.out);
                new CircuitDesign(reader, writer).run();
                writer.writer.flush();
            }
        }, "1", 1 << 26).start();
    }

    static class Clause {
        int firstVar;
        int secondVar;
    }

    static class TwoSatisfiability {
        int numVars;
        Clause[] clauses;
        boolean[] marked;
        ArrayList<Integer> postorder;  // vertices in postorder
        int[] id;       // the same id indicates same strongly connected component
        int count;          // number of strongly connected components

        TwoSatisfiability(int n, int m) {
            numVars = n;
            clauses = new Clause[m];
            for (int i = 0; i < m; ++i) {
                clauses[i] = new Clause();
            }
            count = 0;
            id = new int[2 * numVars];
            marked = new boolean[2 * numVars];
            postorder = new ArrayList<>();
        }

        private void StronglyConnectedComponents(ArrayList<ArrayList<Integer>> adj) {
            ArrayList<ArrayList<Integer>> rev = new ArrayList<>();
            for (int i = 0; i < 2 * numVars; i++) {
                rev.add(new ArrayList<Integer>());
            }
            for (int v = 0; v < adj.size(); v++) {
                for (int w : adj.get(v)) {
                    rev.get(w).add(v);
                }
            }

            ArrayList<Integer> res = toposort(rev);

            Arrays.fill(marked, false);

            for (int v : res) {
                if (!marked[v]) {
                    dfs(adj, marked, v);
                    count++;
                }
            }
        }

        private ArrayList<Integer> toposort(ArrayList<ArrayList<Integer>> adj) {
            for (int v = 0; v < adj.size(); v++) {
                if (!marked[v]) {
                    dfsTopo(adj, v);
                }
            }
            Collections.reverse(postorder);
            return postorder;
        }

        private void dfsTopo(ArrayList<ArrayList<Integer>> adj, int v) {
            marked[v] = true;
            for (int w : adj.get(v)) {
                if (!marked[w]) {
                    dfsTopo(adj, w);
                }
            }
            postorder.add(v);
        }

        private void dfs(ArrayList<ArrayList<Integer>> adj, boolean[] marked, int v) {
            id[v] = count;
            marked[v] = true;
            for (int w : adj.get(v)) {
                if (!marked[w]) {
                    dfs(adj, marked, w);
                }
            }
        }

        boolean isSatisfiable(int[] result) {
            ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
            ArrayList<ArrayList<Integer>> listOfSccS = new ArrayList<>();
            for (int i = 0; i < 2 * numVars; i++) {
                adj.add(new ArrayList<Integer>());
                listOfSccS.add(new ArrayList<Integer>());
            }

            for (Clause clause : clauses) {
                int x, y, u, v;
                x = Math.abs(clause.firstVar) - 1;
                y = Math.abs(clause.secondVar) - 1;
                u = Math.abs(clause.firstVar) + numVars - 1;
                v = Math.abs(clause.secondVar) + numVars - 1;
                if (clause.firstVar < 0 && clause.secondVar < 0) {
                    adj.get(x).add(v);
                    adj.get(y).add(u);
                } else if (clause.firstVar > 0 && clause.secondVar > 0) {
                    adj.get(u).add(y);
                    adj.get(v).add(x);
                } else if (clause.firstVar < 0 && clause.secondVar > 0) {
                    adj.get(x).add(y);
                    adj.get(v).add(u);
                } else {
                    adj.get(u).add(v);
                    adj.get(y).add(x);
                }
            }

            StronglyConnectedComponents(adj);

            for (int i = 1; i <= numVars; i++) {
                if (id[i - 1] == id[numVars + i - 1]) {
                    return false;
                }
            }

            for (int i = 0; i < 2 * numVars; i++) {
                listOfSccS.get(id[i]).add(i);
            }
            Arrays.fill(result, -1);

            for (ArrayList<Integer> integers : listOfSccS) {
                for (int i : integers) {
                    if (i < numVars) {
                        if (result[i] == -1) {
                            result[i] = 0;
                        }
                    } else {
                        if (result[i - numVars] == -1) {
                            result[i - numVars] = 1;
                        }
                    }
                }
            }
            return true;
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        TwoSatisfiability twoSat = new TwoSatisfiability(n, m);
        for (int i = 0; i < m; ++i) {
            twoSat.clauses[i].firstVar = reader.nextInt();
            twoSat.clauses[i].secondVar = reader.nextInt();
        }

        int[] result = new int[n];
        if (twoSat.isSatisfiable(result)) {
            writer.printf("SATISFIABLE\n");
            for (int i = 1; i <= n; ++i) {
                if (result[i - 1] == 1) {
                    writer.printf("%d", -i);
                } else {
                    writer.printf("%d", i);
                }
                if (i < n) {
                    writer.printf(" ");
                } else {
                    writer.printf("\n");
                }
            }
        } else {
            writer.printf("UNSATISFIABLE\n");
        }
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