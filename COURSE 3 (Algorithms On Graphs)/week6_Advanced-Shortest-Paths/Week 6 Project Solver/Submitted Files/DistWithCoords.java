import java.util.*;

public class DistWithCoords {        // unidirectional version

    private static class Impl {
        // Number of nodes
        int n;
        // Coordinates of nodes
        long[] x;
        long[] y;
        // See description of these fields in the starters for friend_suggestion
        ArrayList<Integer>[] adj;
        ArrayList<Integer>[] cost;
        long[] distance;
        PriorityQueue<Entry> queue;
        boolean[] visited;
        ArrayList<Integer> workSet;
        Map<Integer, Long> map;
        final long INFINITY = Long.MAX_VALUE / 4;

        Impl(int n) {
            this.n = n;
            visited = new boolean[n];
            x = new long[n];
            y = new long[n];
            workSet = new ArrayList<Integer>();
            distance = new long[n];
            for (int i = 0; i < n; ++i) {
                distance[i] = INFINITY;
            }
            queue = new PriorityQueue<Entry>();
            map = new HashMap<>();
        }

        // See the description of this method in the starters for friend_suggestion
        void clear() {
            for (int v : workSet) {
                distance[v] = INFINITY;
                visited[v] = false;
            }
            workSet.clear();
            queue.clear();
            map.clear();
        }

        // See the description of this method in the starters for friend_suggestion
        void visit(int v, long dist, long potential) {
            // Implement this method yourself
            if (distance[v] > dist) {
                distance[v] = dist;
                workSet.add(v);
                queue.add(new Entry(distance[v] + potential, v));
            }
        }

        long Euclid_Potential(int i, int t) {
            if (!map.containsKey(i)) {
                map.put(i, (long) Math.sqrt((x[i] - x[t]) * (x[i] - x[t]) + (y[i] - y[t]) * (y[i] - y[t])));
            }
            return map.get(i);
        }

        void process(int v, int t) {
            for (int i = 0; i < adj[v].size(); i++) {
                int w = adj[v].get(i);
                if (!visited[w]) {
                    visit(w, distance[v] + cost[v].get(i), Euclid_Potential(w, t));
                }
            }
        }

        // Returns the distance from s to t in the graph.
        long query(int s, int t) {
            clear();
            visit(s, 0L, Euclid_Potential(s, t));
            // Implement the rest of the algorithm yourself

            while (!queue.isEmpty()) {
                Entry v = queue.remove();
                if (v.node == t) {
                    if (distance[t] != INFINITY) {
                        return distance[t];
                    }
                    return (long) -1;
                }
                if (!visited[v.node]) {
                    process(v.node, t);
                    visited[v.node] = true;
                }
            }
            return -1L;
        }

        static class Entry implements Comparable<Entry> {
            long cost;
            int node;

            public Entry(long cost, int node) {
                this.cost = cost;
                this.node = node;
            }

            public int compareTo(Entry other) {
                return Long.compare(cost,other.cost);
            }
        }
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();
        int m = in.nextInt();
        Impl DistWithCoords = new Impl(n);
        DistWithCoords.adj = (ArrayList<Integer>[]) new ArrayList[n];
        DistWithCoords.cost = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            DistWithCoords.adj[i] = new ArrayList<Integer>();
            DistWithCoords.cost[i] = new ArrayList<Integer>();
        }

        for (int i = 0; i < n; i++) {
            long x, y;
            x = in.nextLong();
            y = in.nextLong();
            DistWithCoords.x[i] = x;
            DistWithCoords.y[i] = y;
        }

        for (int i = 0; i < m; i++) {
            int x, y, c;
            x = in.nextInt();
            y = in.nextInt();
            c = in.nextInt();
            DistWithCoords.adj[x - 1].add(y - 1);
            DistWithCoords.cost[x - 1].add(c);
        }

        int t = in.nextInt();

        new Thread(null, new Runnable() {     // for stackoverflow error. increasing stack size
            public void run() {
                try {
                    for (int i = 0; i < t; i++) {
                        int u, v;
                        u = in.nextInt();
                        v = in.nextInt();
                        System.out.println(DistWithCoords.query(u - 1, v - 1));
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, "1", 1 << 26).start();
    }
}