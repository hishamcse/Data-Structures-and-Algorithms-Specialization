package com.Hisham;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PhiX174ErrorFree {

    private static final int TOTAL_ERROR_FREE_READ = 1618;
    private static final int MER_SIZE = 12;

    private static ArrayList<Vertex>[] adj;
    private static boolean[] marked;

    static class Vertex implements Comparable<Vertex> {
        int id;
        String read;
        int weight;

        public Vertex(int id, String read, int weight) {
            this.id = id;
            this.read = read;
            this.weight = weight;
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(o.weight, this.weight);
        }
    }

    private static int overLapLength(String s1, String s2) {
        int len = 0;
        int suffixId = 0,prefixId = 0;

        while (prefixId < s1.length()) {
            int id = prefixId;
            while (id < s1.length() && s1.charAt(id++) == s2.charAt(suffixId++)) {
                len++;
            }
            if (id == s1.length()) {
                break;
            }
            len = 0;
            suffixId = 0;
            prefixId++;
        }
        return len;
    }

    public static void buildSortedOverlapGraph(Vertex[] vertices) {

        adj = (ArrayList<Vertex>[]) new ArrayList[vertices.length];
        for (int i = 0; i < vertices.length; i++) {
            adj[i] = new ArrayList<>();
        }
        for (int i = 0; i < vertices.length - 1; i++) {
            for (int j = i + 1; j < vertices.length; j++) {
                int len = overLapLength(vertices[i].read, vertices[j].read);
                if (len > MER_SIZE) {
                    adj[i].add(new Vertex(j, vertices[j].read, len));
                }
                int revLen = overLapLength(vertices[j].read, vertices[i].read);
                if (revLen > MER_SIZE) {
                    adj[j].add(new Vertex(i, vertices[i].read, revLen));
                }
            }
        }

        for (ArrayList<Vertex> list : adj) {
            Collections.sort(list);
        }
    }

    private static void findHamiltonianPath(Vertex[] vertices, Deque<Vertex> path) {
        marked = new boolean[adj.length];
        Vertex start = new Vertex(0, vertices[0].read, 0);

        traversal(start, path);
    }

    private static boolean traversal(Vertex v, Deque<Vertex> path) {
        path.add(v);
        marked[v.id] = true;

        if (path.size() == adj.length) {
            return true;
        }

        for (Vertex w : adj[v.id]) {
            if (!marked[w.id] && traversal(w, path)) {
                return true;
            }
        }
        Vertex t = path.removeLast();
        marked[t.id] = false;
        return false;
    }

    private static void checkCircularOverlap(Vertex[] vertices, Deque<Vertex> path, StringBuilder ans) {
        int lastId = path.getLast().id;
        int circularLen = overLapLength(vertices[lastId].read, vertices[0].read);

        if (circularLen > 0) {
            ans.delete(ans.length() - circularLen, ans.length());
        }
    }

    private static void createAnswer(Vertex[] vertices, Deque<Vertex> path, StringBuilder ans) {
        for (Vertex v : path) {
            ans.append(v.read.substring(v.weight));
        }
        checkCircularOverlap(vertices, path, ans);
    }

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        Vertex[] vertices = new Vertex[TOTAL_ERROR_FREE_READ];
        for (int i = 0; i < TOTAL_ERROR_FREE_READ; i++) {
            vertices[i] = new Vertex(i, scanner.next(), 0);
        }
        buildSortedOverlapGraph(vertices);

        Deque<Vertex> hamiltonPath = new LinkedList<>();
        findHamiltonianPath(vertices, hamiltonPath);

        StringBuilder answer = new StringBuilder();
        createAnswer(vertices, hamiltonPath, answer);

        System.out.println(answer.toString());
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
    }
}