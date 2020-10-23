package com.Hisham;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class FriendSuggestion {

    private static class Impl {
        // Number of nodes
        int n;

        // adj[0] and cost[0] store the initial graph, adj[1] and cost[1] store the reversed graph.
        // Each graph is stored as array of adjacency lists for each node. adj stores the edges,
        // and cost stores their costs.
        ArrayList<Integer>[][] adj;
        ArrayList<Integer>[][] cost;

        // distance[0] and distance[1] correspond to distance estimates in the forward and backward searches.
        Long[][] distance;

        // Two priority queues, one for forward and one for backward search.
        ArrayList<PriorityQueue<Entry>> queue;

        // visited[0][v] == true iff v was visited by forward search. visited[1][v] == true if visited by backward search
        boolean[][] visited;

        // List of all the nodes which were visited either by forward or backward search.
        ArrayList<Integer> workSet;

        final Long INFINITY = Long.MAX_VALUE / 4;

        Impl(int n) {
            this.n = n;
            visited = new boolean[2][n];
            workSet = new ArrayList<Integer>();
            distance = new Long[][]{new Long[n], new Long[n]};
            for (int i = 0; i < n; ++i) {
                distance[0][i] = distance[1][i] = INFINITY;
            }
            queue = new ArrayList<PriorityQueue<Entry>>();
            queue.add(new PriorityQueue<Entry>(n));
            queue.add(new PriorityQueue<Entry>(n));
        }

        // Reinitialize the data structures before new query after the previous query
        void clear() {
            for (int v : workSet) {
                distance[0][v] = distance[1][v] = INFINITY;
                visited[0][v] = visited[1][v] = false;
            }
            workSet.clear();
            queue.get(0).clear();
            queue.get(1).clear();
        }

        // Try to relax the distance from direction side to node v using value dist.
        void visit(int side, int v, Long dist) {
            // Implement this method yourself
            if (distance[side][v] > dist) {
                distance[side][v] = dist;
                workSet.add(v);
                queue.get(side).add(new Entry(distance[side][v], v));
            }
        }

        void process(int side, int v) {
            int c = 0;
            for (int w : adj[side][v]) {
                visit(side, w, distance[side][v] + cost[side][v].get(c));
                c++;
            }
        }

        long shortestPathDist() {
            long min_dist = INFINITY;
            for (int u : workSet) {
                if (distance[0][u] + distance[1][u] < min_dist) {
                    min_dist = distance[0][u] + distance[1][u];
                }
            }
            if (min_dist == INFINITY) {
                return -1;
            }
            return min_dist;
        }

        // Returns the distance from s to t in the graph.
        Long query(int s, int t) {
            clear();
            visit(0, s, 0L);
            visit(1, t, 0L);
            // Implement the rest of the algorithm yourself

            do {
                Entry v = queue.get(0).remove();
                process(0, v.node);
                visited[0][v.node] = true;
                if (visited[1][v.node]) {
                    return shortestPathDist();
                }

                Entry v_rev = queue.get(1).remove();
                process(1, v_rev.node);
                visited[1][v_rev.node] = true;
                if (visited[0][v_rev.node]) {
                    return shortestPathDist();
                }
            } while (!queue.get(0).isEmpty() && !queue.get(1).isEmpty());

            return -1L;
        }

        static class Entry implements Comparable<Entry> {
            Long cost;
            int node;

            public Entry(Long cost, int node) {
                this.cost = cost;
                this.node = node;
            }

            public int compareTo(Entry other) {
                return cost.compareTo(other.cost);
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Impl biDijkstra = new Impl(n);
        biDijkstra.adj = (ArrayList<Integer>[][]) new ArrayList[2][];
        biDijkstra.cost = (ArrayList<Integer>[][]) new ArrayList[2][];
        for (int side = 0; side < 2; ++side) {
            biDijkstra.adj[side] = (ArrayList<Integer>[]) new ArrayList[n];
            biDijkstra.cost[side] = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; i++) {
                biDijkstra.adj[side][i] = new ArrayList<Integer>();
                biDijkstra.cost[side][i] = new ArrayList<Integer>();
            }
        }

        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            biDijkstra.adj[0][x - 1].add(y - 1);
            biDijkstra.cost[0][x - 1].add(c);
            biDijkstra.adj[1][y - 1].add(x - 1);
            biDijkstra.cost[1][y - 1].add(c);
        }

        int t = in.nextInt();

        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(biDijkstra.query(u - 1, v - 1));
        }
    }
}