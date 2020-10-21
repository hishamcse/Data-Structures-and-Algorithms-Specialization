package com.Hisham;

import java.util.*;

public class OptimalKMer {

    private static final int TOTAL_ERROR_FREE_READ = 400;
    private static final int READ_LEN = 100;

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
            if (adj.get(v) == null) {
                cycle = null;
                return;
            }
            while (adj.get(v) != null && adj.get(v).hasNext()) {
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

    private static Map<String, Set<String>> DeBruijn_Graph_Construction(Set<String> k_mers) {

        Map<String, Set<String>> adj = new LinkedHashMap<>();
        for (String k_mer : k_mers) {
            String vertex = k_mer.substring(0, k_mer.length() - 1);
            String edge = k_mer.substring(1);

            Set<String> set = new HashSet<>();
            set.add(edge);
            adj.put(vertex, set);
        }
        return adj;
    }

    private static void find_Optimal_KMer(String[] k_mers) {

        for (int k = READ_LEN; k >= 2; k--) {

            Set<String> new_Mers = new HashSet<>();   // for time issue, list is avoided
            for (String k_mer : k_mers) {
                for (int j = 0; j < READ_LEN - k + 1; j++) {
                    new_Mers.add(k_mer.substring(j, k));
                }
            }

            Map<String, Set<String>> graph = DeBruijn_Graph_Construction(new_Mers);
            EulerianCycle(graph);

            if (cycle != null) {
                System.out.println(READ_LEN - k);
                return;
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] all = new String[TOTAL_ERROR_FREE_READ];
        for (int i = 0; i < TOTAL_ERROR_FREE_READ; i++) {
            all[i] = scanner.nextLine();
        }
        find_Optimal_KMer(all);
    }
}