import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

/* collected solution modified version */
public class PhiX174AssemblerDeBruijn {

    private static final int K_MER_LEN = 20;
    private static final int NUCLEOTIDE_LEN = 5396;

    private static Map<String, Integer> kMer_count;

    static class Node {
        int node_Id;
        List<Node> children;
        Node parent;

        public Node(int node_Id) {
            this.node_Id = node_Id;
            children = new ArrayList<>();
            parent = null;
        }
    }

    static class Edge {
        int from;
        int to;
        boolean used;

        public Edge(int from, int to) {
            this.from = from;
            this.to = to;
            this.used = false;
        }
    }

    static class Vertex {
        int vertex_Id;
        String str;
        List<Integer> outer_edges;
        List<Integer> inner_edges;
        List<Integer> all_edges;

        boolean euler_marked;
        boolean tip_removed;
        boolean bubble_detected;
        Node node_bubble;

        public Vertex(int vertex_Id, String str, List<Integer> outer_edges, List<Integer> inner_edges) {
            this.vertex_Id = vertex_Id;
            this.str = str;
            this.outer_edges = outer_edges;
            this.inner_edges = inner_edges;
            all_edges = new ArrayList<>();
            euler_marked = false;
            tip_removed = false;
            bubble_detected = false;
            node_bubble = null;
        }
    }

    /********************************* DeBruijn Graph Construction *************************************/

    private static Vertex[] DeBruijn_Graph_Construction(String[] k_mers) {
        kMer_count = new HashMap<>();
        Map<String, ArrayList<Integer>> adjOut = new HashMap<>();
        Map<String, ArrayList<Integer>> adjIn = new HashMap<>();

        Map<String, Integer> id_count = new HashMap<>();
        Set<String> used_Str = new HashSet<>();
        int id = 0;

        for (String k_mer : k_mers) {
            String from = k_mer.substring(0, k_mer.length() - 1);
            String to = k_mer.substring(1);

            if (used_Str.contains(k_mer)) {
                kMer_count.put(k_mer, kMer_count.get(k_mer) + 1);
                continue;
            }

            used_Str.add(k_mer);
            kMer_count.put(k_mer, 1);

            if (!id_count.containsKey(from)) {
                id_count.put(from, id);
                adjOut.put(from, new ArrayList<>());
                adjIn.put(from, new ArrayList<>());
                id++;
            }
            if (!id_count.containsKey(to)) {
                id_count.put(to, id);
                adjOut.put(to, new ArrayList<>());
                adjIn.put(to, new ArrayList<>());
                id++;
            }

            if (overlap(from, to)) {
                adjOut.get(from).add(id_count.get(to));
                adjIn.get(to).add(id_count.get(from));
            }
        }

        Vertex[] adj = new Vertex[id_count.size()];
        for (String str : id_count.keySet()) {
            int v_id = id_count.get(str);
            adj[v_id] = new Vertex(v_id, str, adjOut.get(str), adjIn.get(str));
        }
        return adj;
    }

    private static boolean overlap(String s1, String s2) {
        int j = 0;
        for (int i = 1; i < s1.length(); i++) {
            if (s1.charAt(i) != s2.charAt(j)) {
                return false;
            }
            j++;
        }
        return true;
    }

    /**************************************** Tip Removal *******************************************/

    private static void tip_removal(Vertex[] graph) {
        for (int v = 0; v < graph.length; v++) {
            if (graph[v].tip_removed) {
                continue;
            }

            if (graph[v].outer_edges.size() == 0) {
                remove_inner_tips(graph, v);
                continue;
            }
            if (graph[v].inner_edges.size() == 0) {
                remove_outer_tips(graph, v);
            }
        }
    }

    private static void remove_inner_tips(Vertex[] graph, int v) {
        if (graph[v].outer_edges.size() != 0 || graph[v].inner_edges.size() != 1) {
            return;
        }
        graph[v].tip_removed = true;

        int w = graph[v].inner_edges.get(0);
        graph[w].outer_edges.remove(Integer.valueOf(v));
        graph[v].inner_edges.remove(Integer.valueOf(w));

        remove_inner_tips(graph, w);
    }

    private static void remove_outer_tips(Vertex[] graph, int v) {
        if (graph[v].inner_edges.size() != 0 || graph[v].outer_edges.size() != 1) {
            return;
        }
        graph[v].tip_removed = true;

        int w = graph[v].outer_edges.get(0);
        graph[w].inner_edges.remove(Integer.valueOf(v));
        graph[v].outer_edges.remove(Integer.valueOf(w));

        remove_outer_tips(graph, w);
    }

    /***************************************** Bubble Detection & Removal ************************************/

    private static void process_bubbles(Vertex[] graph) {
        for (Vertex vertex : graph) {
            if (vertex.tip_removed || vertex.outer_edges.size() < 2) {
                continue;
            }
            explore_initialize(graph, vertex.vertex_Id);
        }
    }

    private static void explore_initialize(Vertex[] graph, int v) {
        Set<Integer> visited = new HashSet<>();
        Node node = new Node(v);

        for (Vertex vertex : graph) {
            vertex.bubble_detected = false;
            vertex.node_bubble = null;
        }

        DFS_traversal(graph, node, visited);
    }

    private static void DFS_traversal(Vertex[] graph, Node node, Set<Integer> visited) {
        visited.add(node.node_Id);

        if (graph[node.node_Id].bubble_detected) {
            Node common = common_origin(graph, node);
            bubble_removal(graph, node, common, visited);
            if (!visited.contains(node.node_Id)) {
                return;
            }
        }

        graph[node.node_Id].bubble_detected = true;
        graph[node.node_Id].node_bubble = node;

        if (visited.size() > K_MER_LEN) {
            visited.remove(node.node_Id);
            return;
        }

        List<Integer> list = new ArrayList<>(graph[node.node_Id].outer_edges);
        for (int w : list) {
            if (visited.contains(w)) {
                continue;
            }

            Node child = new Node(w);
            child.parent = node;
            node.children.add(child);

            DFS_traversal(graph, child, visited);

            if (!visited.contains(node.node_Id)) {
                return;
            }
        }
        visited.remove(node.node_Id);
    }

    private static Node common_origin(Vertex[] graph, Node node) {
        Node x = graph[node.node_Id].node_bubble;
        List<Node> list = new ArrayList<>();
        while (x != null) {
            list.add(x);
            x = x.parent;
        }
        x = node;
        while (x != null) {
            for (int i = list.size() - 1; i >= 0; i--) {
                if (x == list.get(i)) {
                    return x;
                }
            }
            x = x.parent;
        }
        return null;
    }

    private static void bubble_removal(Vertex[] graph, Node node, Node common, Set<Integer> visited) {
        Node first = node;
        Node other = graph[node.node_Id].node_bubble;

        double coverage_first = find_coverage(graph, first, common);
        double coverage_other = find_coverage(graph, other, common);

        first = node;
        other = graph[node.node_Id].node_bubble;

        List<Integer> removed_vertices = new ArrayList<>();
        Node temp;
        if (coverage_first <= coverage_other) {
            temp = delete_path(graph, first, common, removed_vertices);
            reInitialize(graph, temp);
            graph[node.node_Id].bubble_detected = true;
            graph[node.node_Id].node_bubble = other;

            for (Integer removed_vertex : removed_vertices) {
                visited.remove(removed_vertex);
            }
        } else {
            temp = delete_path(graph, other, common, removed_vertices);
            reInitialize(graph, temp);
            graph[node.node_Id].bubble_detected = true;
            graph[node.node_Id].node_bubble = first;

        }
        common.children.remove(temp);
    }

    private static double find_coverage(Vertex[] graph, Node node, Node common) {
        int edge_count = 0;
        double sum = 0;
        while (node != common) {
            String s1 = graph[node.node_Id].str;
            String s2 = graph[node.parent.node_Id].str;
            String str = s2 + s1.charAt(s1.length() - 1);
            sum += kMer_count.get(str);
            edge_count++;
            node = node.parent;
        }
        return sum / edge_count;
    }

    private static Node delete_path(Vertex[] graph, Node node, Node common, List<Integer> removed_vertices) {
        Node parent = node.parent;
        Node child = node;
        Node result = null;

        while (child != common) {
            graph[parent.node_Id].outer_edges.remove(Integer.valueOf(child.node_Id));
            removed_vertices.add(child.node_Id);
            result = child;
            child = parent;
            parent = parent.parent;
        }
        return result;
    }

    // after the common node, all are reinitialized to default
    private static void reInitialize(Vertex[] graph, Node node) {
        graph[node.node_Id].bubble_detected = false;
        graph[node.node_Id].node_bubble = null;

        for (int i = 0; i < node.children.size(); i++) {
            reInitialize(graph, node.children.get(i));
        }
    }

    /*************************************** Eulerian Cycle ***************************************/

    private static void EulerianCycle(Vertex[] graph, List<Edge> edges, int v, List<Integer> cycle) {
        List<Integer> list = graph[v].all_edges;
        graph[v].euler_marked = true;
        for (Integer w : list) {
            Edge edge = edges.get(w);
            if (!edge.used) {
                edge.used = true;
                EulerianCycle(graph, edges, edge.to, cycle);
            }
        }
        cycle.add(v);
    }

    /************************************* genome assembly ***************************************/

    private static List<Edge> Edge_Construction(Vertex[] graph) {
        List<Edge> edges = new ArrayList<>();
        for (int v = 0; v < graph.length; v++) {
            if (graph[v].tip_removed) {
                continue;
            }
            List<Integer> list = graph[v].outer_edges;
            for (Integer w : list) {
                Edge edge = new Edge(v, w);
                graph[v].all_edges.add(edges.size());
                edges.add(edge);
            }
        }
        return edges;
    }

    private static String genome_finder(Vertex[] graph, List<Edge> edges) {
        int max_len = -1;
        String genome = "";
        Map<String, Integer> map = new HashMap<>();
        for (int i = 0; i < graph.length; i++) {

            if (!graph[i].tip_removed && !graph[i].euler_marked) {
                List<Integer> cycle = new ArrayList<>();
                EulerianCycle(graph, edges, i, cycle);

                StringBuilder sb = new StringBuilder(graph[cycle.get(cycle.size() - 1)].str);

                for (int j = cycle.size() - 2; j >= 0; j--) {
                    String s = graph[cycle.get(j)].str;
                    sb.append(s.charAt(s.length() - 1));
                }

                if (sb.length() >= NUCLEOTIDE_LEN) {
                    map.put(sb.toString(), sb.length() - NUCLEOTIDE_LEN);
                }

                if (sb.length() > max_len) {
                    max_len = sb.length();
                    genome = sb.toString();
                }
            }
        }

        int min_len = Integer.MAX_VALUE;
        for (String s : map.keySet()) {
            if (min_len > map.get(s)) {
                min_len = map.get(s);
                genome = s;
            }
        }
        return genome.substring(14, NUCLEOTIDE_LEN + 14);
    }

    private static String assemble_genome(String[] k_mers) {
        Vertex[] graph = DeBruijn_Graph_Construction(k_mers);
        tip_removal(graph);
        process_bubbles(graph);
        tip_removal(graph);
        List<Edge> edges = Edge_Construction(graph);
        return genome_finder(graph, edges);
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

        new Thread(null, () -> {     // increasing stack size to avoid stack overflow error
            try {
                System.out.println(assemble_genome(k_mers.toArray(new String[0])));
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }, "1", 1 << 26).start();
    }
}