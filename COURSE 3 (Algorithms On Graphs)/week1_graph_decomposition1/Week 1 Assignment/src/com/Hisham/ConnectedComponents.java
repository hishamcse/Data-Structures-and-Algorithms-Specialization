package com.Hisham;

import java.util.ArrayList;
import java.util.Scanner;

public class ConnectedComponents {

    private static boolean[] marked;
    private static int[] id;     // the same id indicates same connected component
    private static int count;          // number of connected components

    private static int numberOfComponents(ArrayList<Integer>[] adj) {
        int n= adj.length;
        marked=new boolean[n];
        count = 0;
        marked = new boolean[n];
        id = new int[n];
        for (int v = 0; v < n; v++) {
            if (!marked[v]) {
                dfs(adj,marked, v);
                count++;
            }
        }
        return count;
    }

    private static void dfs(ArrayList<Integer>[] adj,boolean[] marked, int v) {
        id[v] = count;
        marked[v] = true;
        for (int w : adj[v]) {
            if (!marked[w]) {
                dfs(adj,marked, w);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        System.out.println(numberOfComponents(adj));
    }
}


