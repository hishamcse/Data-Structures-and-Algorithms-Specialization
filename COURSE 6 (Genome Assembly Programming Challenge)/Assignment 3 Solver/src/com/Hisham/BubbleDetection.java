package com.Hisham;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

public class BubbleDetection {     // this implementation's idea is taken from discussion forum

    private static Set<String> marked;
    private static List<List<String>> paths;

    private static Map<String, Set<String>> DeBruijn_Graph_Construction(String[] k_mers, int kMer_len) {

        Map<String, Set<String>> adj = new HashMap<>();
        for (String k_mer : k_mers) {
            String from = k_mer.substring(0, kMer_len - 1);
            String to = k_mer.substring(1, kMer_len);

            if (!from.equals(to)) {
                Set<String> out_set = adj.get(from);
                if (out_set != null) {
                    out_set.add(to);
                    adj.put(from, out_set);
                } else {
                    Set<String> newSet = new HashSet<>();
                    newSet.add(to);
                    adj.put(from, newSet);
                }
            }
        }
        return adj;
    }

    private static void DFS_traversal(Map<String, Set<String>> graph, Set<String> possible_In, String v,
                                      List<String> tree, int t) {
        if (tree.size() <= t) {
            marked.add(v);
            tree.add(v);

            if (possible_In.contains(v)) {
                List<String> List = new ArrayList<>(tree);
                paths.add(List);
            }

            if (graph.get(v) != null) {
                for (String str : graph.get(v)) {
                    if (!marked.contains(str)) {
                        DFS_traversal(graph, possible_In, str, tree, t);
                    }
                }
            }

            marked.remove(v);                     // removing v from marked as in this iteration it's work is done
            tree.remove(tree.size() - 1);    // removing the last added node
        }
    }

    private static boolean is_Disjoint(List<String> path1, List<String> path2) {
        if (path1.get(path1.size() - 1).equals(path2.get(path2.size() - 1))) {  // ending must be same.for optimization
            for (int i = 1; i < path1.size() - 1; i++) {
                for (int j = 1; j < path2.size() - 1; j++) {
                    if (path1.get(i).equals(path2.get(j))) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    private static void assign_Degrees(Map<String, Set<String>> graph, List<String> outer, Set<String> inner) {
        Map<String, Integer> degreeIn = new HashMap<>();
        for (String str : graph.keySet()) {
            for (String s : graph.get(str)) {
                degreeIn.merge(s, 1, Integer::sum);
            }
            if (graph.get(str).size() > 1) {
                outer.add(str);
            }
        }

        for (String str : degreeIn.keySet()) {
            if (degreeIn.get(str) > 1) {
                inner.add(str);
            }
        }
    }

    private static int bubble_detection(String[] reads, int k, int t) {

        Map<String, Set<String>> graph = DeBruijn_Graph_Construction(reads, k);
        marked = new HashSet<>();
        List<String> possible_Out = new ArrayList<>();
        Set<String> possible_In = new HashSet<>();
        assign_Degrees(graph, possible_Out, possible_In);

        int count = 0;
        paths = new ArrayList<>();
        List<String> tree = new ArrayList<>();
        for (String str : possible_Out) {
            DFS_traversal(graph, possible_In, str, tree, t);
            for (int i = 0; i < paths.size() - 1; i++) {
                for (int j = i + 1; j < paths.size(); j++) {
                    if (is_Disjoint(paths.get(i), paths.get(j)))
                        count++;
                }
            }
            paths.clear();    // so that for next loop,we can store all the new paths
        }
        return count;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(System.in)));
        int k = scanner.nextInt();
        int t = scanner.nextInt();
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
            for (int i = 0; i < str.length() - k + 1; i++) {
                k_mers.add(str.substring(i, i + k));
            }
        }
        System.out.println(bubble_detection(k_mers.toArray(new String[0]), k, t));
    }
}