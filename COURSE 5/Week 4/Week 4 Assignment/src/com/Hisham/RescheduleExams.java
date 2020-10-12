package com.Hisham;

import java.util.*;
import java.io.PrintWriter;

public class RescheduleExams {

    static class Edge {
        int u, v;

        public Edge(int u, int v) {
            this.u = u;
            this.v = v;
        }
    }

    static class Clause {
        int firstVar, secondVar;

        public Clause(int firstVar, int secondVar) {
            this.firstVar = firstVar;
            this.secondVar = secondVar;
        }
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
                clauses[i] = new Clause(0, 0);
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
            marked[v] = true;
            id[v] = count;
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
                            result[i] = 1;
                        }
                    } else {
                        if (result[i - numVars] == -1) {
                            result[i - numVars] = 0;
                        }
                    }
                }
            }
            return true;
        }
    }

    char[] assignNewColors(int n, Edge[] edges, char[] colors) {
        int num_Vars = 3 * n;
        int num_Clauses = (3 * n) + (3 * edges.length);
        TwoSatisfiability twoSat = new TwoSatisfiability(num_Vars, num_Clauses);
        Clause[] clauses = new Clause[num_Clauses];
        int k = 0;

        for (int i = 0; i < n; i++) {
            int x1, x2, x3;
            if (colors[i] == 'R') {
                x1 = 3 * i + 1;
                x2 = 3 * i + 2;
                x3 = 3 * i + 3;
            } else if (colors[i] == 'G') {
                x1 = 3 * i + 2;
                x2 = 3 * i + 1;
                x3 = 3 * i + 3;
            } else {
                x1 = 3 * i + 3;
                x2 = 3 * i + 1;
                x3 = 3 * i + 2;
            }
            clauses[k++] = new Clause(-x1, -x1);
            clauses[k++] = new Clause(x2, x3);
            clauses[k++] = new Clause(-x2, -x3);
        }

        for (Edge edge : edges) {
            for (int j = 1; j <= 3; j++) {
                clauses[k++] = new Clause(-(edge.u * 3 + j), -(edge.v * 3 + j));
            }
        }

        twoSat.clauses = clauses;
        int[] result = new int[num_Vars];
        StringBuilder finalColors = new StringBuilder();

        if (twoSat.isSatisfiable(result)) {
            for (int i = 0; i < n; i++) {
                if (result[3 * i] == 1) {
                    finalColors.append('R');
                } else if (result[(3 * i) + 1] == 1) {
                    finalColors.append('G');
                } else if (result[(3 * i) + 2] == 1) {
                    finalColors.append('B');
                }
            }
            return finalColors.toString().toCharArray();
        }
        return null;
    }

    void run() {
        Scanner scanner = new Scanner(System.in);
        PrintWriter writer = new PrintWriter(System.out);

        int n = scanner.nextInt();
        int m = scanner.nextInt();
        scanner.nextLine();

        String colorsLine = scanner.nextLine();
        char[] colors = colorsLine.toCharArray();

        Edge[] edges = new Edge[m];
        for (int i = 0; i < m; i++) {
            int u = scanner.nextInt();
            int v = scanner.nextInt();
            edges[i] = new Edge(u - 1, v - 1);
        }

        char[] newColors = assignNewColors(n, edges, colors);

        if (newColors == null) {
            writer.println("Impossible");
        } else {
            writer.println(new String(newColors));
        }
        writer.close();
    }

    public static void main(String[] args) {
        new Thread(null, new Runnable() {
            public void run() {
                new RescheduleExams().run();
            }
        }, "1", 1 << 26).start();
    }
}