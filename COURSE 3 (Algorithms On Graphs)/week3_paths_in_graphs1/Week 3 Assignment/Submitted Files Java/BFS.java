import java.util.*;

public class BFS {

    private static boolean[] marked;
    private static int[] edgeTo;
    private static int[] distTo;

    private static int distance(ArrayList<Integer>[] adj, int s, int t) {
        int n=adj.length;
        marked = new boolean[n];
        edgeTo = new int[n];
        distTo = new int[n];
        bfs(adj, s);

        return pathTo(t)-1;
    }

    private static void bfs(ArrayList<Integer>[] adj, int s) {
        Queue<Integer> queue = new LinkedList<>();
        for (int v = 0; v < adj.length; v++) {
            distTo[v] = Integer.MAX_VALUE;
        }
        queue.add(s);
        marked[s] = true;
        distTo[s] = 0;
        while (!queue.isEmpty()) {
            int v = queue.remove();
            for (int w : adj[v]) {
                if (!marked[w]) {
                    queue.add(w);
                    marked[w] = true;
                    edgeTo[w] = v;
                    distTo[w] = distTo[v] + 1;
                }
            }
        }
    }

    public static int distTo(int v) {   // shortest path length for vertex v
        return distTo[v];
    }

    public static boolean hasPathTo(int v){   // has shortest path??
        return marked[v];
    }

    public static int pathTo(int v) {
        if(!hasPathTo(v)){
            return 0;
        }
        Stack<Integer> stack = new Stack<>();
        int x;
        for (x = v; distTo(x) != 0; x = edgeTo[x]) {
            stack.push(x);
        }
        stack.push(x);
        return stack.size();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[])new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adj[x - 1].add(y - 1);
            adj[y - 1].add(x - 1);
        }
        int x = scanner.nextInt() - 1;
        int y = scanner.nextInt() - 1;
        System.out.println(distance(adj, x, y));
    }
}


