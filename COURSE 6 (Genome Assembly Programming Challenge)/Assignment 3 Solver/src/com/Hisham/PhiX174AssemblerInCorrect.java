package com.Hisham;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

// it doesnt work properly. the other solutions work.
// PhiX174ErrorProne.java & PhiX174AssemblerDeBruijn.java are correct

public class PhiX174AssemblerInCorrect {

    private static final int K_MER_LEN = 20;

    private static List<String> inner_tips;    // indegree 0, but outdegree not 0
    private static List<String> outer_tips;    // outdegree 0, but indegree not 0

    private static List<String> possible_in;         // indegree > 1
    private static List<String> possible_out;         // outdegree > 1

    private static Map<CustomPair, Integer> kMer_count;
    private static Map<String, Map<String, List<BubblePath>>> paths;
    private static Stack<String> cycle;

    static class CustomPair {
        String from;
        String to;

        public CustomPair(String from, String to) {
            this.from = from;
            this.to = to;
        }
    }

    static class BubblePath {
        int kMerSum;
        List<String> path;
    }

    /********************************* DeBruijn Graph Construction *************************************/

    private static void DeBruijn_Graph_Construction(String[] k_mers, Map<String, Set<String>> adjOut,
                                                    Map<String, Set<String>> adjIn) {
        for (String k_mer : k_mers) {
            String from = k_mer.substring(0, K_MER_LEN - 1);
            String to = k_mer.substring(1);

            Set<String> out_set = adjOut.get(from);
            if (out_set != null) {
                out_set.add(to);
                adjOut.put(from, out_set);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(to);
                adjOut.put(from, newSet);
            }

            Set<String> in_set = adjIn.get(to);
            if (in_set != null) {
                in_set.add(from);
                adjIn.put(to, in_set);
            } else {
                Set<String> newSet = new HashSet<>();
                newSet.add(from);
                adjIn.put(to, newSet);
            }

            CustomPair pair1 = new CustomPair(from, to);
            kMer_count.merge(pair1, 1, Integer::sum);
            CustomPair pair2 = new CustomPair(to, from);
            kMer_count.merge(pair2, 1, Integer::sum);
        }
    }

    /**************************************** Tip Removal *******************************************/

    private static void assign_tips(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        Map<String, Integer> indegree = new HashMap<>();
        Map<String, Integer> outdegree = new HashMap<>();
        for (String str : forward.keySet()) {
            if (forward.get(str) != null) {
                outdegree.put(str, forward.get(str).size());
            } else {
                outdegree.put(str, 0);
            }
        }
        for (String str : reverse.keySet()) {
            if (reverse.get(str) != null) {
                indegree.put(str, reverse.get(str).size());
            } else {
                indegree.put(str, 0);
            }
        }

        for (String str : forward.keySet()) {
            if (indegree.get(str) != null) {
                if (outdegree.get(str) == 0 && indegree.get(str) != 0) {
                    outer_tips.add(str);
                }
                if (indegree.get(str) == 0 && outdegree.get(str) != 0) {
                    inner_tips.add(str);
                }
            }
        }
    }

    private static void remove_inner_tips(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        while (!inner_tips.isEmpty()) {
            String current = inner_tips.get(0);
            inner_tips.remove(0);

            Set<String> set_forward = forward.get(current);
            for (String str : forward.get(current)) {
                set_forward.remove(str);
                forward.put(current, set_forward);
                Set<String> set_rev = reverse.get(str);
                set_rev.remove(current);
                reverse.put(str, set_rev);

                if (set_rev.size() == 0 && !outer_tips.contains(str)) {
                    inner_tips.add(str);
                }
            }
        }
    }

    private static void remove_outer_tips(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        while (!outer_tips.isEmpty()) {
            String current = outer_tips.get(0);
            outer_tips.remove(0);

            int len = reverse.get(current).size();
            for (int i = 0; i < len; i++) {
                Set<String> set_rev = reverse.get(current);
                String str = set_rev.toArray(new String[0])[0];
                set_rev.remove(str);
                reverse.put(current, set_rev);
                Set<String> set_forward = forward.get(str);
                set_forward.remove(current);
                forward.put(str, set_forward);

                if (set_forward.size() == 0) {
                    outer_tips.add(str);
                }
            }
        }
    }

    private static void tip_removal(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        inner_tips = new ArrayList<>();
        outer_tips = new ArrayList<>();

        assign_tips(forward, reverse);
        remove_inner_tips(forward, reverse);
        remove_outer_tips(forward, reverse);
    }

    /***************************************** Bubble Detection & Removal ************************************/

    private static void assign_possible_vertices(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        Map<String, Integer> indegree = new HashMap<>();
        Map<String, Integer> outdegree = new HashMap<>();
        for (String str : forward.keySet()) {
            if (forward.get(str) != null) {
                outdegree.put(str, forward.get(str).size());
            } else {
                outdegree.put(str, 0);
            }
        }
        for (String str : reverse.keySet()) {
            if (reverse.get(str) != null) {
                indegree.put(str, reverse.get(str).size());
            } else {
                indegree.put(str, 0);
            }
        }

        for (String str : forward.keySet()) {
            if (outdegree.get(str) != null) {
                if (outdegree.get(str) > 1) {
                    possible_out.add(str);
                }
            }
        }
        for (String str : reverse.keySet()) {
            if (indegree.get(str) != null) {
                if (indegree.get(str) > 1) {
                    possible_in.add(str);
                }
            }
        }
    }

    private static void DFS_traversal(Map<String, Set<String>> forward, String root, String current,
                                      String parent, Set<String> marked, int tKMer_sum) {
        int nKMer_sum = 0;
        if (parent != null) {
            CustomPair pair = new CustomPair(parent, current);
            int k = kMer_count.get(pair);
            nKMer_sum = tKMer_sum + k;
        }

        if (!current.equals(root) && possible_in.contains(current)) {
            BubblePath bubblePath = new BubblePath();
            bubblePath.kMerSum = nKMer_sum;
            bubblePath.path = new ArrayList<>(marked);
            Map<String, List<BubblePath>> map = paths.get(root);
            if (map != null) {
                List<BubblePath> list = map.get(current);
                if (list == null) {
                    list = new ArrayList<>();
                }
                list.add(bubblePath);
                map.put(current, list);
                paths.put(root, map);
            } else {
                map = new HashMap<>();
                List<BubblePath> list = new ArrayList<>();
                list.add(bubblePath);
                map.put(current, list);
                paths.put(root, map);
            }
        }

        if (marked.size() > K_MER_LEN) {
            return;
        }

        for (String v : forward.keySet()) {
            if (!marked.contains(v)) {
                marked.add(v);

                if (parent != null) {
                    DFS_traversal(forward, root, v, current, marked, nKMer_sum);
                } else {
                    DFS_traversal(forward, root, v, current, marked, 0);
                }
            }
        }
    }

    private static int avg_Coverage_Minimum(BubblePath path1, BubblePath path2, int i, int j) {
        double avg_Cov_Path1 = ((double) path1.kMerSum) / (path1.path.size() - 1);
        double avg_Cov_Path2 = ((double) path2.kMerSum) / (path2.path.size() - 1);

        return avg_Cov_Path1 < avg_Cov_Path2 ? i : j;
    }

    private static void remove_Path(Map<String, Set<String>> forward, Map<String, Set<String>> reverse, List<String> path) {
        for (int i = 0; i < path.size() - 1; i++) {
            Set<String> set_forward = forward.get(path.get(i));
            if (set_forward != null) {
                set_forward.remove(String.valueOf(path.get(i + 1)));
                forward.put(path.get(i), set_forward);
            }

            Set<String> set_rev = reverse.get(path.get(i + 1));
            if (set_rev != null) {
                set_rev.remove(String.valueOf(path.get(i)));
                reverse.put(path.get(i + 1), set_rev);
            }
        }
    }

    private static boolean is_Disjoint(List<String> path1, List<String> path2, String src, String des) {
        Set<String> path = new HashSet<>(path2);
        for (String v : path1)
            if (!v.equals(src) && !v.equals(des))
                if (path.contains(v)) return false;
        return true;
    }

    private static void bubbles_removal(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        for (String src : paths.keySet()) {
            Map<String, List<BubblePath>> all_Paths = paths.get(src);

            for (String des : all_Paths.keySet()) {
                List<BubblePath> paths_from_des = all_Paths.get(des);

                if (paths_from_des != null) {
                    List<Boolean> removed_paths = new ArrayList<>();
                    for (int l = 0; l < paths_from_des.size(); l++) {
                        removed_paths.add(false);
                    }

                    for (int k = 0; k < paths_from_des.size(); k++) {
                        int i = k;
                        while (i < removed_paths.size() && removed_paths.get(i)) {
                            i++;
                        }

                        for (int w = i + 1; w < paths_from_des.size(); w++) {
                            int j = w;
                            while (j < removed_paths.size() && removed_paths.get(j)) {
                                j++;
                            }

                            if (is_Disjoint(paths_from_des.get(i).path, paths_from_des.get(j).path, src, des)) {
                                int p = avg_Coverage_Minimum(paths_from_des.get(i), paths_from_des.get(j), i, j);

                                remove_Path(forward, reverse, paths_from_des.get(p).path);

                                removed_paths.set(p, true);
                                if (p == i) {
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static void bubble_detect_remove(Map<String, Set<String>> forward, Map<String, Set<String>> reverse) {
        possible_in = new ArrayList<>();
        possible_out = new ArrayList<>();
        paths = new HashMap<>();

        assign_possible_vertices(forward, reverse);

        for (String root : possible_out) {
            Set<String> marked = new HashSet<>();
            marked.add(root);
            DFS_traversal(forward, root, root, null, marked, 0);
        }

        bubbles_removal(forward, reverse);
    }

    /************************************** Eulerian Cycle ************************************/

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

    /************************************* genome assembly ***************************************/

    private static String assemble_genome(String[] k_mers) {
        Map<String, Set<String>> forward_graph = new HashMap<>();
        Map<String, Set<String>> reverse_graph = new HashMap<>();
        kMer_count = new HashMap<>();
        DeBruijn_Graph_Construction(k_mers, forward_graph, reverse_graph);

        tip_removal(forward_graph, reverse_graph);

        bubble_detect_remove(forward_graph, reverse_graph);

        tip_removal(forward_graph, reverse_graph);

        EulerianCycle(forward_graph);

        StringBuilder sb = new StringBuilder();
        sb.append(cycle.peek());
        for (int i = 1; i < cycle.size() - K_MER_LEN + 1; i++) {
            String s = cycle.get(i);
            sb.append(s.substring(s.length() - 1));
        }

        return sb.toString();
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
                    System.out.println(assemble_genome(k_mers.toArray(new String[0])));
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }, "1", 1 << 26).start();
    }
}