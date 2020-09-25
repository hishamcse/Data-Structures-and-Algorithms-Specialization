package com.Hisham;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class SuffixTreeFromArray {

    static class FastScanner {
        StringTokenizer tok = new StringTokenizer("");
        BufferedReader in;

        FastScanner() {
            in = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() throws IOException {
            while (!tok.hasMoreElements())
                tok = new StringTokenizer(in.readLine());
            return tok.nextToken();
        }

        int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }

    // driver method for lcp computation.not included in assignment
    private int lcpOfSuffixes(String s, int i, int j, int equal) {
        int lcp = Math.max(0, equal);
        while (i + lcp < s.length() && j + lcp < s.length()) {
            if (s.charAt(i + lcp) == s.charAt(j + lcp)) {
                lcp += 1;
            } else {
                break;
            }
        }
        return lcp;
    }

    // driver method for lcp computation.not included in assignment
    private int[] invertSuffixArray(int[] suffixArray) {
        int[] pos = new int[suffixArray.length];
        for (int i = 0; i < pos.length; i++) {
            pos[suffixArray[i]] = i;
        }
        return pos;
    }

    // method for lcp computation.not included in assignment
    private int[] computeLcpArray(String s, int[] order) {
        int[] lcpArray = new int[s.length() - 1];
        int lcp = 0;
        int[] posInOrder = invertSuffixArray(order);
        int suffix = order[0];
        for (int i = 0; i < s.length(); i++) {
            int orderIndex = posInOrder[suffix];
            if (orderIndex == s.length() - 1) {
                lcp = 0;
                suffix = (suffix + 1) % s.length();
                continue;
            }
            int nextSuffix = order[orderIndex + 1];
            lcp = lcpOfSuffixes(s, suffix, nextSuffix, lcp - 1);
            lcpArray[orderIndex] = lcp;
            suffix = (suffix + 1) % s.length();
        }

//        System.out.println(Arrays.toString(lcpArray));
        return lcpArray;
    }

    // Data structure to store edges of a suffix tree.
    static class Edge {
        // The ending node of this edge.
        int node;
        // Starting position of the substring of the text
        // corresponding to the label of this edge.
        int start;
        // Position right after the end of the substring of the text
        // corresponding to the label of this edge.
        int end;

        Edge(int node, int start, int end) {
            this.node = node;
            this.start = start;
            this.end = end;
        }
    }

    static class SuffixTreeNode {
        int id;
        SuffixTreeNode parent;
        Map<Character, Integer> children;
        int stringDepth;
        int edgeStart;
        int edgeEnd;

        public SuffixTreeNode(int id, SuffixTreeNode parent, Map<Character, Integer> children,
                              int stringDepth, int edgeStart, int edgeEnd) {
            this.id = id;
            this.parent = parent;
            this.children = children;
            this.stringDepth = stringDepth;
            this.edgeStart = edgeStart;
            this.edgeEnd = edgeEnd;
        }
    }

    private SuffixTreeNode createNewLeaf(List<SuffixTreeNode> list, SuffixTreeNode node, String s, int suffix) {
        SuffixTreeNode leaf = new SuffixTreeNode(list.size(), node, new TreeMap<>(), s.length() - suffix,
                suffix + node.stringDepth, s.length() - 1);
        list.add(leaf);
        node.children.put(s.charAt(leaf.edgeStart), leaf.id);
        return leaf;
    }

    private SuffixTreeNode breakEdge(List<SuffixTreeNode> list, SuffixTreeNode node, String s, int start, int offset) {
        char startChar = s.charAt(start);
        char midChar = s.charAt(start + offset);
        SuffixTreeNode midNode = new SuffixTreeNode(list.size(), node, new TreeMap<>(), node.stringDepth + offset,
                start, start + offset - 1);
        list.add(midNode);
        list.get(node.children.get(startChar)).edgeStart += offset;
        midNode.children.put(midChar, node.children.get(startChar));
        list.get(node.children.get(startChar)).parent = midNode;
        node.children.put(startChar, midNode.id);
        return midNode;
    }

    // Build suffix tree of the string text given its suffix array suffix_array
    // and LCP array lcp_array. Return the tree as a mapping from a node ID
    // to the list of all outgoing edges of the corresponding node. The edges in the
    // list must be sorted in the ascending order by the first character of the edge label.
    // Root must have node ID = 0, and all other node IDs must be different
    // nonnegative integers.
    //
    // For example, if text = "ACACAA$", an edge with label "$" from root to a node with ID 1
    // must be represented by new Edge(1, 6, 7). This edge must be present in the list tree.get(0)
    // (corresponding to the root node), and it should be the first edge in the list
    // (because it has the smallest first character of all edges outgoing from the root).
    Map<Integer, List<Edge>> SuffixTreeFromSuffixArray(int[] suffixArray, int[] lcpArray, final String text) {

        Map<Integer, List<Edge>> tree = new HashMap<Integer, List<Edge>>();
        List<SuffixTreeNode> list = new ArrayList<>();

        SuffixTreeNode root = new SuffixTreeNode(0, null, new TreeMap<>(), 0, -1, -1);
        list.add(root);

        int lcpPrev = 0;
        SuffixTreeNode curNode = root;

        for (int i = 0; i < text.length(); i++) {
            int suffix = suffixArray[i];
            while (curNode.stringDepth > lcpPrev) {
                curNode = curNode.parent;
            }
            if (curNode.stringDepth == lcpPrev) {
                curNode = createNewLeaf(list, curNode, text, suffix);
            } else {
                int edgeStart = suffixArray[i - 1] + curNode.stringDepth;
                int offset = lcpPrev - curNode.stringDepth;
                SuffixTreeNode midNode = breakEdge(list, curNode, text, edgeStart, offset);
                curNode = createNewLeaf(list, midNode, text, suffix);
            }
            if (i < text.length() - 1) {
                lcpPrev = lcpArray[i];
            }
        }

        for (SuffixTreeNode node : list) {
            if (!node.children.isEmpty()) {
                List<Edge> allChild = new ArrayList<>();
                for (Character c : node.children.keySet()) {
                    SuffixTreeNode child = list.get(node.children.get(c));
                    allChild.add(new Edge(child.id, child.edgeStart, child.edgeEnd + 1));
                }
                tree.put(node.id, allChild);
            }
        }

        return tree;
    }

    static public void main(String[] args) throws IOException {
        new SuffixTreeFromArray().run();
    }

    public void print(ArrayList<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffixArray = new int[text.length()];
        for (int i = 0; i < suffixArray.length; ++i) {
            suffixArray[i] = scanner.nextInt();
        }
        int[] lcpArray = new int[text.length() - 1];
        for (int i = 0; i + 1 < text.length(); ++i) {
            lcpArray[i] = scanner.nextInt();
        }
        System.out.println(text);
        // Build the suffix tree and get a mapping from
        // suffix tree node ID to the list of outgoing Edges.
        Map<Integer, List<Edge>> suffixTree = SuffixTreeFromSuffixArray(suffixArray, lcpArray, text);
        ArrayList<String> result = new ArrayList<String>();
        // Output the edges of the suffix tree in the required order.
        // Note that we use here the contract that the root of the tree
        // will have node ID = 0 and that each vector of outgoing edges
        // will be sorted by the first character of the corresponding edge label.
        //
        // The following code avoids recursion to avoid stack overflow issues.
        // It uses two stacks to convert recursive function to a while loop.
        // This code is an equivalent of
        //
        //    OutputEdges(tree, 0);
        //
        // for the following _recursive_ function OutputEdges:
        //
        // public void OutputEdges(Map<Integer, List<Edge>> tree, int nodeId) {
        //     List<Edge> edges = tree.get(nodeId);
        //     for (Edge edge : edges) {
        //         System.out.println(edge.start + " " + edge.end);
        //         OutputEdges(tree, edge.node);
        //     }
        // }
        //
        int[] nodeStack = new int[text.length()];
        int[] edgeIndexStack = new int[text.length()];
        nodeStack[0] = 0;
        edgeIndexStack[0] = 0;
        int stackSize = 1;
        while (stackSize > 0) {
            int node = nodeStack[stackSize - 1];
            int edgeIndex = edgeIndexStack[stackSize - 1];
            stackSize -= 1;
            if (suffixTree.get(node) == null) {
                continue;
            }
            if (edgeIndex + 1 < suffixTree.get(node).size()) {
                nodeStack[stackSize] = node;
                edgeIndexStack[stackSize] = edgeIndex + 1;
                stackSize += 1;
            }
            result.add(suffixTree.get(node).get(edgeIndex).start + " " + suffixTree.get(node).get(edgeIndex).end);
            nodeStack[stackSize] = suffixTree.get(node).get(edgeIndex).node;
            edgeIndexStack[stackSize] = 0;
            stackSize += 1;
        }
        print(result);
    }
}
