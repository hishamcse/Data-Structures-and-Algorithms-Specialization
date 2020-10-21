import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class TipRemoval {

    private static final int K_MER_LEN = 15;

    private static Set<String> marked;
    private static ArrayList<String> postorder;  // vertices in postorder
    private static int count;

    private static void DeBruijn_Graph_Construction(String[] k_mers, Map<String, Set<String>> adjOut,
                                                    Map<String, Set<String>> adjIn) {
        for (String k_mer : k_mers) {
            String vertex = k_mer.substring(0, K_MER_LEN - 1);
            String edge = k_mer.substring(1);

            Set<String> out_set = adjOut.get(vertex);
            if (out_set != null) {
                out_set.add(edge);
                adjOut.put(vertex, out_set);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(edge);
                adjOut.put(vertex, newSet);
            }

            Set<String> in_set = adjIn.get(edge);
            if (in_set != null) {
                in_set.add(vertex);
                adjIn.put(edge, in_set);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(vertex);
                adjIn.put(edge, newSet);
            }
        }
    }

    private static void StronglyConnectedComponents(Map<String, Set<String>> adj, Map<String, Set<String>> rev) {

        ArrayList<String> res = toposort(rev);

        marked.clear();
        for (String v : res) {
            if (!marked.contains(v)) {
                dfs(adj, marked, v);
                count++;
            }
        }
    }

    private static ArrayList<String> toposort(Map<String, Set<String>> adj) {
        for (String v : adj.keySet()) {
            if (!marked.contains(v)) {
                dfsTopo(adj, v);
            }
        }
        Collections.reverse(postorder);
        return postorder;
    }

    private static void dfsTopo(Map<String, Set<String>> adj, String v) {
        marked.add(v);
        if (adj.get(v) != null) {
            for (String w : adj.get(v)) {
                if (!marked.contains(w)) {
                    dfsTopo(adj, w);
                }
            }
        }
        postorder.add(v);
    }

    private static void dfs(Map<String, Set<String>> adj, Set<String> marked, String v) {
        marked.add(v);
        if (adj.get(v) != null) {
            for (String w : adj.get(v)) {
                if (!marked.contains(w)) {
                    dfs(adj, marked, w);
                }
            }
        }
    }

    private static int tip_Removal(String[] k_mers) {
        Map<String, Set<String>> forward_graph = new HashMap<>();
        Map<String, Set<String>> reverse_graph = new HashMap<>();
        DeBruijn_Graph_Construction(k_mers, forward_graph, reverse_graph);

        count = 0;
        marked = new HashSet<>();
        postorder = new ArrayList<>();

        StronglyConnectedComponents(forward_graph, reverse_graph);
        return count - 1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        List<String> reads = new ArrayList<>();
        while (scanner.hasNext()) {
            String read = scanner.next();
            if (read == null || read.length() < 3) {
                break;
            }
            reads.add(read);
        }
        List<String> k_mers = new ArrayList<>();
        for (String str : reads) {
            for (int i = 0; i < str.length() - K_MER_LEN + 1; i++) {
                k_mers.add(str.substring(i, i + K_MER_LEN));
            }
        }

        new Thread(null, new Runnable() {     // for stackoverflow error. increasing stack size
            public void run() {
                try {
                    System.out.println(tip_Removal(k_mers.toArray(new String[0])));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, "1", 1 << 26).start();
    }
}