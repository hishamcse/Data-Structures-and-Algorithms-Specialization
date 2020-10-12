package com.Hisham;

import java.io.*;
import java.util.Arrays;
import java.util.StringTokenizer;

public class StockCharts {

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockCharts().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {

        int numStocks = stockData.length;
        int numPoint = stockData[0].length;

        boolean[][] compares = new boolean[numStocks][numStocks];

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < numStocks; j++) {
                if (i == j) {
                    continue;
                }
                compares[i][j] = true;
                for (int k = 0; k < numPoint; k++) {
                    compares[i][j] = compares[i][j] & (stockData[i][k] < stockData[j][k]);
                    if (!compares[i][j]) {  // as no chance to become true in this loop,so,terminate the inner loop
                        break;
                    }
                }
            }
        }

        // left(stock)=bipartite[0]  right(point)=bipartite[1]
        int[][] bipartiteMatching = new int[2][numStocks];
        for (int i = 0; i < 2; i++) {
            int[] rowPoint = bipartiteMatching[i];
            Arrays.fill(rowPoint, -1);
        }

        // finding out the intersection from comparisons
        int intersection = 0;
        for (int i = 0; i < numStocks; i++) {
            boolean[] marked = new boolean[numStocks];
            if (dfs(i, compares, bipartiteMatching, marked)) {
                intersection++;
            }
        }

        return numStocks - intersection;

    }

    private boolean dfs(int v, boolean[][] compares, int[][] bipartiteMatching, boolean[] marked) {
        if (v == -1) {
            return true;
        }
        if (marked[v]) {
            return false;
        }
        marked[v] = true;
        for (int w = 0; w < marked.length; w++) {
            if (compares[v][w] && dfs(bipartiteMatching[1][w], compares, bipartiteMatching, marked)) {
                bipartiteMatching[0][v] = w;
                bipartiteMatching[1][w] = v;
                return true;
            }
        }
        return false;
    }

    private void writeResponse(int result) {
        out.println(result);
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