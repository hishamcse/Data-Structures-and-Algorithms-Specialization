import java.util.*;

public class ShortestPaths {


    private static void shortestPaths(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost, int s,
                                      long[] distance, int[] reachable, int[] shortest) {
        defineReachability(adj, s, reachable);
        distance[s]=0;
        defineNegativeCycle(adj, cost, reachable, shortest, distance);
    }

    private static void defineReachability(ArrayList<Integer>[] adj, int s, int[] reachable) {
        Queue<Integer> queue = new LinkedList<>();
        queue.add(s);
        long[] distTo=count(adj, queue);

        for (int i = 0; i < distTo.length; i++) {
            if (distTo[i] != -1) {
                reachable[i] = 1;
            }
        }
    }

    private static long[] count(ArrayList<Integer>[] adj, Queue<Integer> queue) {

        int n = adj.length;
        long[] distTo = new long[n];
        for (int v = 0; v < adj.length; v++) {
            distTo[v] = -1;
        }

        distTo[queue.peek()] = 0;

        while (!queue.isEmpty()) {
            int v = queue.remove();
            for (int w : adj[v]) {
                if (distTo[w] == -1) {
                    queue.add(w);
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
        return distTo;
    }

    private static void defineNegativeCycle(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost,
                                            int[] reachable, int[] shortest, long[] distance) {
        int n = adj.length;

        for (int v = 0; v < n - 1; v++) {      // n-1 times iteration
            processRelax(adj, cost, distance,reachable);
        }

        long[] copy = Arrays.copyOf(distance, distance.length);
        processRelax(adj, cost, distance,reachable);               // for the nth iteration

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < copy.length; i++) {
            if (copy[i] != distance[i] && reachable[i] == 1) {
                queue.add(i);
            }
        }

        if (!queue.isEmpty()) {
            long[] distTo=count(adj, queue);
            for (int i = 0; i < distTo.length; i++) {
                if (distTo[i] != -1) {
                    shortest[i] = 0;
                }
            }
        }
    }

    private static void processRelax(ArrayList<Integer>[] adj, ArrayList<Integer>[] cost,long[] distTo,int[] reachable) {
        for (int v = 0; v < adj.length; v++) {
            if (reachable[v] == 1) {
                for (int i = 0; i < adj[v].size(); i++) {
                    int w = adj[v].get(i);
                    if (distTo[w] > distTo[v] + cost[v].get(i)) {
                        distTo[w] = distTo[v] + cost[v].get(i);
                    }
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
        int s = scanner.nextInt() - 1;
        long[] distance = new long[n];
        int[] reachable = new int[n];
        int[] shortest = new int[n];
        for (int i = 0; i < n; i++) {
            distance[i] = 9999999999L;
            reachable[i] = 0;
            shortest[i] = 1;
        }
        shortestPaths(adj, cost, s, distance, reachable, shortest);
        for (int i = 0; i < n; i++) {
            if (reachable[i] == 0) {
                System.out.println('*');
            } else if (shortest[i] == 0) {
                System.out.println('-');
            } else {
                System.out.println(distance[i]);
            }
        }
    }
}


