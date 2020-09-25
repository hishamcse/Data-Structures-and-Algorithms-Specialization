package com.Hisham;

import java.util.*;

public class NegativeCycle {

    private static int[] distTo;

    private static int negativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        int n = adj.length;
        distTo = new int[n];

        for (int i = 0; i < n; i++) {
            distTo[i] = 99999;
        }
        distTo[0] = 0;

        for (int v = 0; v < n - 1; v++) {      // n-1 times iteration
            processRelax(adj, cost);
        }

        int[] copy = Arrays.copyOf(distTo, distTo.length);
        processRelax(adj, cost);               // for the nth iteration

        if (Arrays.equals(copy, distTo)) {    // if in the nth iteration,array doesnt change,so no negative cycle
            return 0;
        }
        return 1;
    }

    private static void processRelax(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost) {
        for (int v = 0; v < adj.length; v++) {
            for (int i = 0; i < adj[v].size(); i++) {
                int w = adj[v].get(i);
                if (distTo[w] > distTo[v] + cost[v].get(i)) {
                    distTo[w] = distTo[v] + cost[v].get(i);
                }
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
        ArrayList<Integer>[] cost = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
            cost[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y, w;
            x = scanner.nextInt();
            y = scanner.nextInt();
            w = scanner.nextInt();
            adj[x - 1].add(y - 1);
            cost[x - 1].add(w);
        }
        System.out.println(negativeCycle(adj, cost));
    }
}