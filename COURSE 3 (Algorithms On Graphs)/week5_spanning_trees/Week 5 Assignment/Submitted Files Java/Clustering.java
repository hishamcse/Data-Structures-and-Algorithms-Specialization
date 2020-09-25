import java.util.PriorityQueue;
import java.util.Scanner;

public class Clustering {

    static class Point {
        public int id;
        public int x;
        public int y;

        public Point(int x, int y, int id) {
            this.x = x;
            this.y = y;
            this.id = id;
        }
    }

    static class Edge implements Comparable<Edge> {
        public Point v;
        public Point w;
        public double weight;

        public Edge(Point v, Point w) {
            this.v = v;
            this.w = w;
            this.weight = Math.sqrt((w.x - v.x) * (w.x - v.x) + (w.y - v.y) * (w.y - v.y));
        }

        @Override
        public int compareTo(Edge o) {
            return Double.compare(this.weight, o.weight);
        }
    }

    static class UF {             // union find data type

        private final int[] parent;  // parent[i] = parent of i
        private final byte[] rank;   // rank[i] = rank of subtree rooted at i (never more than 31)
        public int connectedComponents;

        public UF(int n) {
            parent = new int[n];
            rank = new byte[n];
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                rank[i] = 0;
            }
            connectedComponents = n;
        }

        public int find(int p) {
            while (p != parent[p]) {
                parent[p] = parent[parent[p]];    // path compression by halving
                p = parent[p];
            }
            return p;
        }

        public void union(int p, int q) {
            int rootP = find(p);
            int rootQ = find(q);
            if (rootP == rootQ) return;

            if (rank[rootP] < rank[rootQ]) parent[rootP] = rootQ;
            else if (rank[rootP] > rank[rootQ]) parent[rootQ] = rootP;
            else {
                parent[rootQ] = rootP;
                rank[rootP]++;
            }
            connectedComponents--;
        }
    }

    private static double clustering(int[] x, int[] y, int k) {

        int n = x.length;

        PriorityQueue<Edge> minPQ = new PriorityQueue<>();

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y.length; j++) {
                if (i != j) {
                    minPQ.add(new Edge(new Point(x[i], y[i], i), new Point(x[j], y[j], j)));
                }
            }
        }

        UF uf = new UF(n);

        while (!minPQ.isEmpty()) {
            Edge e = minPQ.remove();
            Point v = e.v;
            Point w = e.w;
            if (uf.find(v.id) != uf.find(w.id)) {
                if (uf.connectedComponents == k) {
                    return e.weight;    // because this distance should be the largest distance of two point from different clusters
                }
                uf.union(v.id, w.id);
            }
        }

        return -1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] x = new int[n];
        int[] y = new int[n];
        for (int i = 0; i < n; i++) {
            x[i] = scanner.nextInt();
            y[i] = scanner.nextInt();
        }
        int k = scanner.nextInt();
        System.out.format("%.7f", clustering(x, y, k));
    }
}