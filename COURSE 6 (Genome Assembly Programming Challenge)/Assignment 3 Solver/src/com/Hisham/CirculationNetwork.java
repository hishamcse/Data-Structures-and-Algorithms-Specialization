package com.Hisham;

import java.io.*;
import java.util.*;

// slight modification of course 5 week 1 assignment (Evacuate.java)
public class CirculationNetwork {

    private static FastScanner in;

    private static int[] edgeTo;
    private static int original_Edge_count;

    static class Edge {
        int from, to, capacity, lowerLimit, flow;

        public Edge(int from, int to, int lowerLimit, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
            this.lowerLimit = lowerLimit;
            this.flow = 0;
        }
    }

    /* This class implements a bit unusual scheme to store the graph edges, in order
     * to retrieve the backward edge for a given edge quickly. */
    static class FlowGraph {
        /* List of all - forward and backward - edges */
        private final List<Edge> edges;
        /* These adjacency lists store only indices of edges from the edges list */
        private final List<Integer>[] graph;

        private int lowerLimit_sum = 0;

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int lowerLimit, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, lowerLimit, capacity);
            Edge backwardEdge = new Edge(to, from, lowerLimit, 0);
            graph[from].add(edges.size());
            edges.add(forwardEdge);
            graph[to].add(edges.size());
            edges.add(backwardEdge);
        }

        public int size() {
            return graph.length;
        }

        public List<Integer> getIds(int from) {
            return graph[from];
        }

        public Edge getEdge(int id) {
            return edges.get(id);
        }

        public void addFlow(int id, int flow) {
            /* To get a backward edge for a true forward edge (i.e id is even), we should get id + 1
             * due to the described above scheme. On the other hand, when we have to get a "backward"
             * edge for a backward edge (i.e. get a forward edge for backward - id is odd), id - 1
             * should be taken. It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    private static int maxFlow(FlowGraph g, int s, int t) {
        int flow = 0;
        edgeTo = new int[g.size()];

        while (hasAugmentingPath(g, s, t)) {

            int bottle = Integer.MAX_VALUE;
            for (int v = t; v != s; v = g.getEdge(edgeTo[v]).from) {
                int weight = g.getEdge(edgeTo[v]).capacity - g.getEdge(edgeTo[v]).flow;
                bottle = Math.min(bottle, weight);
            }

            for (int v = t; v != s; v = g.getEdge(edgeTo[v]).from) {
                g.addFlow(edgeTo[v], bottle);
            }

            flow += bottle;
        }
        return flow;
    }

    private static boolean hasAugmentingPath(FlowGraph g, int s, int t) {
        boolean[] marked = new boolean[g.size()];
        Queue<Integer> queue = new LinkedList<>();

        queue.add(s);
        marked[s] = true;

        while (!queue.isEmpty() && !marked[t]) {
            int v = queue.remove();
            for (Integer w : g.getIds(v)) {
                Edge e = g.getEdge(w);
                if (e.capacity - e.flow > 0 && !marked[e.to]) {
                    marked[e.to] = true;
                    edgeTo[e.to] = w;
                    queue.add(e.to);
                }
            }
        }
        return marked[t];
    }

    static FlowGraph readGraph() throws IOException {
        int vertex_count = in.nextInt();
        int edge_count = in.nextInt();
        vertex_count += 2;
        FlowGraph graph = new FlowGraph(vertex_count);

        int[] inner = new int[vertex_count];
        int[] outer = new int[vertex_count];
        original_Edge_count = 2 * edge_count;   // as we have to add edges from source to i and i to target

        for (int i = 0; i < edge_count; ++i) {
            int from = in.nextInt(), to = in.nextInt();
            int lowerLimit = in.nextInt(), capacity = in.nextInt();
            graph.addEdge(from, to, lowerLimit, capacity - lowerLimit);
            inner[to] += lowerLimit;
            outer[from] += lowerLimit;
            graph.lowerLimit_sum += lowerLimit;
        }

        int s = 0, t = vertex_count - 1;       // source & sink
        for (int i = 1; i < t; i++) {       // adding edges for source and target
            graph.addEdge(s, i, inner[i], inner[i]);
            graph.addEdge(i, t, outer[i], outer[i]);
        }
        return graph;
    }

    public static void main(String[] args) throws IOException {
        in = new FastScanner();
        FlowGraph graph = readGraph();

        int val = maxFlow(graph, 0, graph.size() - 1);

        if (graph.lowerLimit_sum != val) {
            System.out.println("NO");
        } else {
            System.out.println("YES");
            // as we store all the forward(original) edges at even position and residual edges at odd position.
            // so,only even indexed edges are original and we have to print them only
            for (int i = 0; i < original_Edge_count; i += 2) {
                Edge e = graph.edges.get(i);
                System.out.println(e.flow + e.lowerLimit);
            }
        }
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