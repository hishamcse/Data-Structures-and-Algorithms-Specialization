package com.Hisham;

import java.util.*;

public class PhiX174Assembler {

    private static final int TOTAL_K_MERS = 5396;
    private static final int K_MINUS1_MER = 9;

    private static Stack<String> cycle = null;

    public static void EulerianCycle(Map<String, List<String>> adjOut) {

        Map<String, Iterator<String>> adj = new LinkedHashMap<>();
        for (String v : adjOut.keySet()) {
            adj.put(v, adjOut.get(v).iterator());
        }
        createCycle(adjOut, adj);
    }

    private static void createCycle(Map<String, List<String>> adjOut, Map<String, Iterator<String>> adj) {

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

    private static String nonIsolatedVertex(Map<String, List<String>> adjOut) {
        for (String v : adjOut.keySet()) {
            if (adjOut.get(v).size() > 0) {
                return v;
            }
        }
        return null;
    }

    private static Map<String, List<String>> DeBruijn_Graph_Construction(String[] k_mers) {

        Map<String, List<String>> adj = new LinkedHashMap<>();

        for (String k_mer : k_mers) {
            String vertex = k_mer.substring(0, k_mer.length() - 1);
            String edge = k_mer.substring(1);

            List<String> set = adj.get(vertex);
            if (set != null) {
                set.add(edge);
                adj.put(vertex, set);
            } else {
                Vector<String> newSet = new Vector<>();
                newSet.add(edge);
                adj.put(vertex, newSet);
            }
        }
        return adj;
    }

    private static void find_Assembled_Genome(String[] k_mers) {

        Map<String, List<String>> graph = DeBruijn_Graph_Construction(k_mers);
        EulerianCycle(graph);

        System.out.print(cycle.peek());
        for (int i = 1; i < cycle.size() - K_MINUS1_MER; i++) {
            String s = cycle.get(i);
            System.out.print(s.substring(s.length() - 1));
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String[] all = new String[TOTAL_K_MERS];
        for (int i = 0; i < TOTAL_K_MERS; i++) {
            all[i] = scanner.nextLine();
        }
        find_Assembled_Genome(all);
    }
}