package com.Hisham;

import java.util.*;

public class Toposort {

    private static boolean[] marked;
    private static ArrayList<Integer> postorder;  // vertices in postorder

    private static void dfs(ArrayList<Integer>[] adj, int v) {
        marked[v] = true;
        for (int w : adj[v]) {
            if (!marked[w]) {
                dfs(adj, w);
            }
        }
        postorder.add(v);
    }

    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        marked=new boolean[adj.length];
        postorder = new ArrayList<>();

        for (int v = 0; v < adj.length; v++) {
            if (!marked[v]) {
                dfs(adj, v);
            }
        }

        Collections.reverse(postorder);

        return postorder;
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
        }
        Iterable<Integer> order = toposort(adj);
        for (int x : order) {
            System.out.print((x + 1) + " ");
        }
    }
}


