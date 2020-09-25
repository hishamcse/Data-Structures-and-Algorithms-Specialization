import java.util.ArrayList;
import java.util.Scanner;

public class Reachability {

    private static int reach(ArrayList<Integer>[] adj, int x, int y) {
        boolean[] marked=new boolean[adj.length];
        dfs(adj,marked,x);
        if(marked[y]){
            return 1;
        }
        return 0;
    }

    private static void dfs(ArrayList<Integer>[] adj,boolean[] marked, int v) {
        marked[v] = true;
        for (int w : adj[v]) {
            if (!marked[w]) {
                dfs(adj,marked, w);
            }
        }
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
        System.out.println(reach(adj, x, y));
    }
}
