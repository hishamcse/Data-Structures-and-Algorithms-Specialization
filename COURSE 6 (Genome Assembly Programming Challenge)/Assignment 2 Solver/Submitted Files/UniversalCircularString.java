import java.util.*;

public class UniversalCircularString {

    private static final int TOTAL_DIGIT_BIN = 16;

    private static Stack<String> cycle = null;

    public static void EulerianCycle(Map<String, Set<String>> adjOut) {

        Map<String, Iterator<String>> adj = new LinkedHashMap<>();
        for (String v : adjOut.keySet()) {
            adj.put(v, adjOut.get(v).iterator());
        }

        createCycle(adjOut, adj);
    }

    private static void createCycle(Map<String, Set<String>> adjOut, Map<String, Iterator<String>> adj) {

        String s = nonIsolatedVertex(adjOut);
        Stack<String> stack = new Stack<>();
        stack.push(s);

        cycle = new Stack<>();
        while (!stack.isEmpty()) {
            String v = stack.pop();
            while (adj.get(v).hasNext()) {
                stack.push(v);
                v = adj.get(v).next();
            }
            cycle.push(v);
        }
        Collections.reverse(cycle);
    }

    private static String nonIsolatedVertex(Map<String, Set<String>> adjOut) {
        for (String v : adjOut.keySet()) {
            if (adjOut.get(v).size() > 0) {
                return v;
            }
        }
        return null;
    }

    private static Map<String, Set<String>> DeBruijn_Graph_Construction(int k) {
        int p = (int) Math.pow(2, k);
        Map<String, Set<String>> adj = new LinkedHashMap<>();

        for (int i = 0; i < p; i++) {
            String s1 = Integer.toBinaryString(0x10000 | i).substring(1);
            String s2 = Integer.toBinaryString(0x10000 | (i * 2 % p)).substring(1);
            String s3 = Integer.toBinaryString(0x10000 | (i * 2 % p + 1)).substring(1);

            String vertex = s1.substring(TOTAL_DIGIT_BIN - k, TOTAL_DIGIT_BIN - 1);
            String edge1 = s2.substring(TOTAL_DIGIT_BIN - k);
            String edge2 = s3.substring(TOTAL_DIGIT_BIN - k);

            Set<String> set = adj.get(vertex);
            if (set != null) {
                set.add(edge1.substring(0, k - 1));
                set.add(edge2.substring(0, k - 1));
                adj.put(vertex, set);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(edge1.substring(0, k - 1));
                newSet.add(edge2.substring(0, k - 1));
                adj.put(vertex, newSet);
            }
        }
        return adj;
    }

    private static void find_Universal_Circular_String(int k) {
        Map<String, Set<String>> graph = DeBruijn_Graph_Construction(k);
        EulerianCycle(graph);
        for (int i = 0; i < cycle.size() - 1; i++) {
            String s = cycle.get(i);
            System.out.print(s.substring(0, s.length() - k + 2));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int k = scanner.nextInt();
        find_Universal_Circular_String(k);
    }
}