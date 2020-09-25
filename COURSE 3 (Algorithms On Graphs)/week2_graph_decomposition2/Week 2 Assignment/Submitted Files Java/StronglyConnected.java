import java.util.*;

public class StronglyConnected {

    private static boolean[] marked;
    private static ArrayList<Integer> postorder;  // vertices in postorder

    private static void dfsTopo(ArrayList<Integer>[] adj, int v) {
        marked[v] = true;
        for (int w : adj[v]) {
            if (!marked[w]) {
                dfsTopo(adj, w);
            }
        }
        postorder.add(v);
    }

    private static ArrayList<Integer> toposort(ArrayList<Integer>[] adj) {
        marked=new boolean[adj.length];
        postorder = new ArrayList<>();

        for (int v = 0; v < adj.length; v++) {
            if (!marked[v]) {
                dfsTopo(adj, v);
            }
        }

        Collections.reverse(postorder);
        return postorder;
    }

    private static int numberOfStronglyConnectedComponents(ArrayList<Integer>[] adj) {
        int count=0;
        ArrayList<Integer>[] rev=(ArrayList<Integer>[])new ArrayList[adj.length];
        for(int v=0;v<adj.length;v++){
            rev[v]=new ArrayList<>();
        }
        for(int v=0;v< adj.length;v++){
            for(int w:adj[v]){
                rev[w].add(v);
            }
        }

        Iterable<Integer> res=toposort(rev);
        marked=new boolean[adj.length];
        for (int v : res) {
            if (!marked[v]) {
                dfs(adj,marked, v);
                count++;
            }
        }

        return count;
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
        }
        System.out.println(numberOfStronglyConnectedComponents(adj));
    }
}


