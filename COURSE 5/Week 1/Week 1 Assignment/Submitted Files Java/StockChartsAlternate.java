import java.io.*;
import java.util.*;

public class StockChartsAlternate {

    private FastScanner in;
    private PrintWriter out;

    public static void main(String[] args) throws IOException {
        new StockChartsAlternate().solve();
    }

    public void solve() throws IOException {
        in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        int[][] stockData = readData();
        int result = minCharts(stockData);
        writeResponse(result);
        out.close();
    }

    int[][] readData() throws IOException {
        int numStocks = in.nextInt();
        int numPoints = in.nextInt();
        int[][] stockData = new int[numStocks][numPoints];
        for (int i = 0; i < numStocks; ++i)
            for (int j = 0; j < numPoints; ++j)
                stockData[i][j] = in.nextInt();
        return stockData;
    }

    private int minCharts(int[][] stockData) {

        int numStocks = stockData.length;
        int numPoint = stockData[0].length;
        int n = 2 * numStocks + 2;
        FlowGraph graph = new FlowGraph(n);
        int s = 0;
        int t = n - 1;

        for (int i = 0; i < numStocks; i++) {
            graph.addEdge(s, i + 1, 1);
        }

        boolean[][] compares = new boolean[numStocks][numStocks];

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < numStocks; j++) {
                if (i == j) {
                    continue;
                }
                compares[i][j] = true;
                for (int k = 0; k < numPoint; k++) {
                    compares[i][j] = compares[i][j] & (stockData[i][k] < stockData[j][k]);
                    if (!compares[i][j]) {  // as no chance to become true in this loop,so,terminate the inner loop
                        break;
                    }
                }
            }
        }

        for (int i = 0; i < numStocks; i++) {
            for (int j = 0; j < numStocks; j++) {
                if (compares[i][j]) {
                    graph.addEdge(i + 1, j + numStocks + 1, 1);
                }
            }
        }

        for (int j = 0; j < numStocks; j++) {
            graph.addEdge(j + numStocks + 1, t, 1);
        }

        maxFlow(graph, s, t);

        int intersection = 0;
        for (int i = 0; i < numStocks; i++) {
            List<Integer> ids = graph.getIds(i + 1);

            for (Integer v : ids) {
                Edge e = graph.getEdge(v);
                if (e.to == s) {
                    continue;
                }
                if (e.flow <= 0) {
                    continue;
                }
                intersection++;
                break;
            }
        }

        return numStocks - intersection;
    }

    private static int[] edgeTo;

    private static void maxFlow(FlowGraph g, int s, int t) {
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
        }
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

    static class Edge {
        int from, to, capacity, flow;

        public Edge(int from, int to, int capacity) {
            this.from = from;
            this.to = to;
            this.capacity = capacity;
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

        public FlowGraph(int n) {
            this.graph = (ArrayList<Integer>[]) new ArrayList[n];
            for (int i = 0; i < n; ++i)
                this.graph[i] = new ArrayList<>();
            this.edges = new ArrayList<>();
        }

        public void addEdge(int from, int to, int capacity) {
            /* Note that we first append a forward edge and then a backward edge,
             * so all forward edges are stored at even indices (starting from 0),
             * whereas backward edges are stored at odd indices. */
            Edge forwardEdge = new Edge(from, to, capacity);
            Edge backwardEdge = new Edge(to, from, 0);
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
             * should be taken.
             *
             * It turns out that id ^ 1 works for both cases. Think this through! */
            edges.get(id).flow += flow;
            edges.get(id ^ 1).flow -= flow;
        }
    }

    private void writeResponse(int result) {
        out.println(result);
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