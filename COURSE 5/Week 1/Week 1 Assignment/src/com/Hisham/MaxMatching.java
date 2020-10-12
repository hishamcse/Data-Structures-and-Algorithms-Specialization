package com.Hisham;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class MaxMatching {

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new MaxMatching().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        boolean[][] bipartiteGraph = readData();
        int[] matching = findMatching(bipartiteGraph);
        writeResponse(matching);
        out.close();
    }

    boolean[][] readData() throws IOException {
        int numLeft = in.nextInt();
        int numRight = in.nextInt();
        boolean[][] adjMatrix = new boolean[numLeft][numRight];
        for (int i = 0; i < numLeft; ++i)
            for (int j = 0; j < numRight; ++j)
                adjMatrix[i][j] = (in.nextInt() == 1);
        return adjMatrix;
    }

    private int[] findMatching(boolean[][] bipartiteGraph) {

        int numLeft = bipartiteGraph.length;
        int numRight = bipartiteGraph[0].length;

        int[] matchLeft = new int[numLeft];
        int[] matchRight = new int[numRight];
        Arrays.fill(matchLeft, -1);
        Arrays.fill(matchRight, -1);

        for (int i = 0; i < numLeft; ++i) {
            boolean[] marked = new boolean[numLeft];
            dfs(i, marked, matchLeft, matchRight, bipartiteGraph);
        }

        return matchLeft;
    }

    private static boolean dfs(int v, boolean[] marked, int[] matchLeft, int[] matchRight, boolean[][] bipartiteGraph) {
        if (v == -1) {
            return true;
        }
        if (marked[v]) {
            return false;
        }
        marked[v] = true;
        for (int i = 0; i < matchRight.length; i++) {
            if (bipartiteGraph[v][i] && dfs(matchRight[i], marked, matchLeft, matchRight, bipartiteGraph)) {
                matchLeft[v] = i;
                matchRight[i] = v;
                return true;
            }
        }
        return false;
    }

    private void writeResponse(int[] matching) {
        for (int i = 0; i < matching.length; ++i) {
            if (i > 0) {
                out.print(" ");
            }
            if (matching[i] == -1) {
                out.print("-1");
            } else {
                out.print(matching[i] + 1);
            }
        }
        out.println();
    }

    static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}