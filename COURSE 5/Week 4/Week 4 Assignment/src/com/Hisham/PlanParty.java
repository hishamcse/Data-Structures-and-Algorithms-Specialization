package com.Hisham;

import java.io.*;
import java.util.*;

class Vertex {

    int weight;
    ArrayList<Integer> children;

    Vertex() {
        this.weight = 0;
        this.children = new ArrayList<Integer>();
    }
}

class PlanParty {

    static Vertex[] ReadTree() throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        StreamTokenizer tokenizer = new StreamTokenizer(reader);

        tokenizer.nextToken();
        int vertices_count = (int) tokenizer.nval;

        Vertex[] tree = new Vertex[vertices_count];

        for (int i = 0; i < vertices_count; ++i) {
            tree[i] = new Vertex();
            tokenizer.nextToken();
            tree[i].weight = (int) tokenizer.nval;
        }

        for (int i = 1; i < vertices_count; ++i) {
            tokenizer.nextToken();
            int from = (int) tokenizer.nval;
            tokenizer.nextToken();
            int to = (int) tokenizer.nval;
            tree[from - 1].children.add(to - 1);
            tree[to - 1].children.add(from - 1);
        }

        return tree;
    }

    static int dfs(Vertex[] tree, int vertex, int parent, int[] maxFun) {
        if (maxFun[vertex] != -1) {
            return maxFun[vertex];
        }
        int m1 = tree[vertex].weight;
        for (int child : tree[vertex].children) {
            if (child == parent) {
                continue;
            }
            for (int grandChild : tree[child].children) {
                if (grandChild == vertex) {
                    continue;
                }
                m1 += dfs(tree, grandChild, child, maxFun);
            }
        }
        int m0 = 0;
        for (int child : tree[vertex].children) {
            if (child == parent) {
                continue;
            }
            m0 += dfs(tree, child, vertex, maxFun);
        }
        maxFun[vertex] = Math.max(m1, m0);
        return maxFun[vertex];
    }

    static int MaxWeightIndependentTreeSubset(Vertex[] tree) {
        int size = tree.length;
        if (size == 0)
            return 0;
        int[] maxFun = new int[size];
        Arrays.fill(maxFun, -1);
        return dfs(tree, 0, -1, maxFun);
    }

    public static void main(String[] args) {
        // This is to avoid stack overflow issues
        new Thread(null, new Runnable() {
            public void run() {
                try {
                    new PlanParty().run();
                } catch (IOException ignored) {
                }
            }
        }, "1", 1 << 26).start();
    }

    public void run() throws IOException {
        Vertex[] tree = ReadTree();
        int weight = MaxWeightIndependentTreeSubset(tree);
        System.out.println(weight);
    }
}