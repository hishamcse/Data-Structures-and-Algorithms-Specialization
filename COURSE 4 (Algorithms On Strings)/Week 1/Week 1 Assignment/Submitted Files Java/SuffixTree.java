import java.util.*;
import java.io.*;

public class SuffixTree {

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
    }

    static class Node {
        String text;
        List<Node> children = new ArrayList<>();
    }

    static class CommonPrefix {   // common prefix of two string
        int len;
        Node prefixNode;
    }

    public List<String> createSuffixTree(String text) {
        Node root = new Node();
        Node whole = new Node();
        whole.text = text;
        root.children.add(whole);

        for (int i = 1; i < text.length(); i++) {
            appendSuffixesToTree(root, new StringBuilder(text.substring(i)));
        }

        List<String> result = new ArrayList<>();
        resultTree(root, result);
        return result;
    }

    private void resultTree(Node x, List<String> result) {
        if (x.text != null) {
            result.add(x.text);
        }
        for (Node child : x.children) {
            resultTree(child, result);
        }
    }

    private void appendSuffixesToTree(Node current, StringBuilder str) {
        while (str.length() != 0) {
            CommonPrefix commonPrefix = findCommonPrefix(current, str);
            if (commonPrefix.len == 0) {   // no common prefix
                addChild(current, str.toString());
                str.delete(0, str.length());
            } else {
                current = commonPrefix.prefixNode;
                if (current.text.length() == commonPrefix.len) {   // common prefix string length equal to current
                    str.delete(0, commonPrefix.len);
                } else {
                    split(current, commonPrefix);
                    addChild(current, str.substring(commonPrefix.len));
                    str.delete(0, str.length());
                }
            }
        }
    }

    private CommonPrefix findCommonPrefix(Node current, StringBuilder str) {
        CommonPrefix prefix = new CommonPrefix();

        for (Node child : current.children) {
            int commonLength = 0;
            for (int i = 0; i < str.length() && i < child.text.length(); i++) {
                if (child.text.charAt(i) == str.charAt(i)) {
                    commonLength++;
                } else {
                    break;
                }
            }
            if (commonLength > prefix.len) {
                prefix.len = commonLength;
                prefix.prefixNode = child;
            }
        }
        return prefix;
    }

    private void addChild(Node node, String str) {
        Node newNode = new Node();
        newNode.text = str;
        node.children.add(newNode);
    }

    private void split(Node current, CommonPrefix commonPrefix) {
        Node newNode = new Node();
        newNode.text = current.text.substring(commonPrefix.len);
        newNode.children.addAll(current.children);
        current.text = current.text.substring(0, commonPrefix.len);
        current.children.clear();
        current.children.add(newNode);
    }

    // Build a suffix tree of the string text and return a list
    // with all of the labels of its edges (the corresponding
    // substrings of the text) in any order.
    public List<String> computeSuffixTreeEdges(String text) {
        return createSuffixTree(text);
    }

    static public void main(String[] args) throws IOException {
        new SuffixTree().run();
    }

    public void print(List<String> x) {
        for (String a : x) {
            System.out.println(a);
        }
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        List<String> edges = computeSuffixTreeEdges(text);
        print(edges);
    }
}