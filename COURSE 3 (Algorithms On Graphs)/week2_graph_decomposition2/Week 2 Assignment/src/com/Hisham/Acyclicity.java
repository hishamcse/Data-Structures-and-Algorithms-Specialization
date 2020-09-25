package com.Hisham;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Stack;

public class Acyclicity {

    private static boolean[] marked;
    private static int[] edgeTo;
    private static Stack<Integer> cycle;
    private static boolean[] onCycle;

    private static int acyclic(ArrayList<Integer>[] adj) {
        int n = adj.length;
        marked = new boolean[n];
        edgeTo = new int[n];
        onCycle = new boolean[n];

        for (int v = 0; v < n; v++) {
            if (cycle == null && !marked[v]) {
                dfs(adj, v);
            }
        }

        if (cycle != null) {
            return 1;
        }
        return 0;
    }

    private static void dfs(ArrayList<Integer>[] adj, int v) {
        onCycle[v] = true;
        marked[v] = true;
        for (int w : adj[v]) {
            if (cycle != null) {
                return;
            } else if (!marked[w]) {
                edgeTo[w] = v;
                dfs(adj, w);
            } else if (onCycle[w]) {
                cycle = new Stack<>();
                for (int x = v; x != w; x = edgeTo[x]) {
                    cycle.push(x);
                }
                cycle.push(w);
                cycle.push(v);
            }
        }
        onCycle[v] = false;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
        }
        System.out.println(acyclic(adj));
    }
}


