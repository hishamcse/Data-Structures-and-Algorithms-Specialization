package com.Hisham;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;

public class DistPreprocessSmall {

    private static class Impl {
        // See the descriptions of these fields in the starter for friend_suggestion
        int n;
        ArrayList<Integer>[][] adj;
        ArrayList<Long>[][] cost;
        Long[][] distance;
        ArrayList<PriorityQueue<Entry>> queue;
        boolean[] visited;
        ArrayList<Integer> workSet;
        final Long INFINITY = Long.MAX_VALUE / 4;

        // Position of the node in the node ordering
        Integer[] rank;
        // Level of the node for level heuristic in the node ordering
        long[] level;

        Impl(int n) {
            this.n = n;
            visited = new boolean[n];
            Arrays.fill(visited, false);
            workSet = new ArrayList<Integer>();
            rank = new Integer[n];
            level = new long[n];
            distance = new Long[][]{new Long[n], new Long[n]};
            for (int i = 0; i < n; ++i) {
                distance[0][i] = distance[1][i] = INFINITY;
                level[i] = 0L;
                rank[i] = 0;
            }
            queue = new ArrayList<PriorityQueue<Entry>>();
            queue.add(new PriorityQueue<Entry>(n));
            queue.add(new PriorityQueue<Entry>(n));
        }

        // Preprocess the graph
        void preprocess() {
            // This priority queue will contain pairs (importance, node) with the least important node in the head
            PriorityQueue<Entry> q = new PriorityQueue<Entry>(n);
            // Implement this method yourself
        }

        void add_edge(int side, int u, int v, Long c) {
            for (int i = 0; i < adj[side][u].size(); ++i) {
                int w = adj[side][u].get(i);
                if (w == v) {
                    Long cc = Math.min(cost[side][u].get(i), c);
                    cost[side][u].set(i, cc);
                    return;
                }
            }
            adj[side][u].add(v);
            cost[side][u].add(c);
        }

        void apply_shortcut(Shortcut sc) {
            add_edge(0, sc.u, sc.v, sc.cost);
            add_edge(1, sc.v, sc.u, sc.cost);
        }

        void clear() {
            for (int v : workSet) {
                distance[0][v] = distance[1][v] = INFINITY;
                visited[v] = false;
            }
            workSet.clear();
            queue.get(0).clear();
            queue.get(1).clear();
        }

        void mark_visited(int u) {
            visited[u] = true;
            workSet.add(u);
        }

        // See the description of this method in the starter for friend_suggestion
        boolean visit(int side, int v, long dist) {
            // Implement this method yourself
            return false;
        }

        // Add the shortcuts corresponding to contracting node v. Return v's importance.
        Long shortcut(int v) {
            // Implement this method yourself

            // Compute the node importance in the end
            long shortcuts = 0L;
            long vLevel = 0L;
            long neighbors = 0L;
            long shortcutCover = 0L;
            // Compute the correct values for the above heuristics before computing the node importance
            return (shortcuts - adj[0][v].size() - adj[1][v].size()) + neighbors + shortcutCover + vLevel;
        }

        // Returns the distance from s to t in the graph
        Long query(int s, int t) {
            if (s == t) {
                return 0L;
            }
            visit(0, s, 0L);
            visit(1, t, 0L);
            Long estimate = INFINITY;
            // Implement the rest of the algorithm yourself
            return estimate == INFINITY ? -1 : estimate;
        }

        static class Entry implements Comparable<Entry> {
            long cost;
            int node;

            public Entry(long cost, int node) {
                this.cost = cost;
                this.node = node;
            }

            public int compareTo(Entry other) {
                if (cost == other.cost) {
                    return Integer.compare(node, other.node);
                }
                return Long.compare(cost, other.cost);
            }
        }

        static class Shortcut {
            int u;
            int v;
            long cost;

            public Shortcut(int u, int v, long c) {
                this.u = u;
                this.v = v;
                cost = c;
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Impl ch = new Impl(n);
        @SuppressWarnings("unchecked")
        ArrayList<Integer>[][] tmp1 = (ArrayList<Integer>[][]) new ArrayList[2][];
        ch.adj = tmp1;
        @SuppressWarnings("unchecked")
        ArrayList<Long>[][] tmp2 = (ArrayList<Long>[][]) new ArrayList[2][];
        ch.cost = tmp2;
        for (int side = 0; side < 2; ++side) {
            @SuppressWarnings("unchecked")
            ArrayList<Integer>[] tmp3 = (ArrayList<Integer>[]) new ArrayList[n];
            ch.adj[side] = tmp3;
            @SuppressWarnings("unchecked")
            ArrayList<Long>[] tmp4 = (ArrayList<Long>[]) new ArrayList[n];
            ch.cost[side] = tmp4;
            for (int i = 0; i < n; i++) {
                ch.adj[side][i] = new ArrayList<Integer>();
                ch.cost[side][i] = new ArrayList<Long>();
            }
        }

        for (int i = 0; i < m; i++) {
            int x, y;
            Long c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextLong();
            ch.adj[0][x - 1].add(y - 1);
            ch.cost[0][x - 1].add(c);
            ch.adj[1][y - 1].add(x - 1);
            ch.cost[1][y - 1].add(c);
        }

        ch.preprocess();
        System.out.println("Ready");

        int t = in.nextInt();

        for (int i = 0; i < t; i++) {
            int u, v;
            u = in.nextInt();
            v = in.nextInt();
            System.out.println(ch.query(u - 1, v - 1));
        }
    }
}