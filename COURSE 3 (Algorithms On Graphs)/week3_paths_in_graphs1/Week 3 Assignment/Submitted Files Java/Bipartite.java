import java.util.*;

public class Bipartite {

    private static boolean[] marked;
    private static boolean[] color;
    private static Queue<Integer> q;

    private static int bipartite(ArrayList<Integer>[] adj) {
        int n = adj.length;
        marked = new boolean[n];
        color = new boolean[n];
        q = new LinkedList<>();

        return bfs(adj);
    }

    private static int bfs(ArrayList<Integer>[] adj) {
        int n = adj.length;
        for (int s = 0; s < n; s++) {
            if (!marked[s]) {
                color[s] = true;
                marked[s] = true;
                q.add(s);

                while (!q.isEmpty()) {
                    int v = q.remove();
                    for (int w : adj[v]) {
                        if (!marked[w]) {
                            marked[w] = true;
                            color[w] = !color[v];
                            q.add(w);
                        } else if (color[w] == color[v]) {
                            return 0;
                        }
                    }
                }
            }
        }
        return 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adj = (ArrayList<Integer>[]) new ArrayList[n];
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
        System.out.println(bipartite(adj));
    }
}