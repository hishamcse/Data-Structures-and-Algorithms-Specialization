package com.Hisham;

import java.util.*;

public class Dijkstra {

    static class DirectedEdge implements Comparable<DirectedEdge> {
        int id;
        int dist;

        public DirectedEdge(int id, int dist) {
            this.id = id;
            this.dist = dist;
        }

        @Override
        public int compareTo(DirectedEdge o) {
            return Integer.compare(this.dist, o.dist);
        }
    }

    private static long distance(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s, int t) {
        int n = adj.length;
        int[] distTo = new int[n];
        PriorityQueue<DirectedEdge> minPQ = new PriorityQueue<>();

        for (int i = 0; i < n; i++) {
            distTo[i] = Integer.MAX_VALUE;
        }
        distTo[s] = 0;

        minPQ.add(new DirectedEdge(s, distTo[s]));

        while (!minPQ.isEmpty()) {
            DirectedEdge e = minPQ.remove();
            int v = e.id;
            for (int i = 0; i < adj[v].size(); i++) {
                int w = adj[v].get(i);
                if (distTo[w] > distTo[v] + cost[v].get(i)) {
                    distTo[w] = distTo[v] + cost[v].get(i);
                    minPQ.add(new DirectedEdge(w, distTo[w]));
                }
            }
        }

        if (distTo[t] != Integer.MAX_VALUE) {
            return distTo[t];
        }
        return -1;

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
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, cost, x, y));
    }
}


