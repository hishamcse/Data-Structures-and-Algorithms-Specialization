package com.Hisham;

import java.io.*;
import java.util.*;

class KNode {

    public static final int Letters = 4;
    public static final int NA = -1;
    public int[] next;
    public boolean patternEnd;

    KNode() {
        next = new int[Letters];
        Arrays.fill(next, NA);
        patternEnd = false;
    }

    public boolean isPatternEnd() {
        return patternEnd;
    }
}

public class TrieMatchingExtended implements Runnable {

    int letterToIndex(char letter) {
        switch (letter) {
            case 'A':
                return 0;
            case 'C':
                return 1;
            case 'G':
                return 2;
            case 'T':
                return 3;
            default:
                assert (false);
                return KNode.NA;
        }
    }

    List<KNode> buildTrie(String[] patterns) {
        List<KNode> trie = new ArrayList<KNode>();
        KNode root = new KNode();
        trie.add(root);

        for (String pattern : patterns) {
            KNode current = root;
            for (int i = 0; i < pattern.length(); i++) {
                char currentSymbol = pattern.charAt(i);
                int id = current.next[letterToIndex(currentSymbol)];
                if (id != KNode.NA) {
                    current = trie.get(id);
                } else {
                    KNode newNode = new KNode();
                    trie.add(newNode);
                    current.next[letterToIndex(currentSymbol)] = trie.size() - 1;
                    current = newNode;
                }
                if (i == pattern.length() - 1) {
                    // it should be checked at last(not at first) of each iteration because at each iteration a char is
                    // of ith char in the current node. If we check first it will not work because at that time
                    // string has been created upto i-1.but we actually checking for i so result will be wrong
                    current.patternEnd = true;
                }
            }
        }

        return trie;
    }

    private boolean prefixTrieMatching(String text, List<KNode> trie) {
        int i = 0;
        char symbol = text.charAt(0);
        KNode v = trie.get(0);
        while (true) {
            int id = v.next[letterToIndex(symbol)];
            if (v.isPatternEnd()) {
                return true;
            } else if (v.next[letterToIndex(symbol)] != KNode.NA) {
                v = trie.get(id);
                if (i + 1 < text.length()) {
                    symbol = text.charAt(++i);
                } else {
                    return v.isPatternEnd();
                }
            } else {
                return false;
            }
        }
    }

    List<Integer> solve(String text, List<String> patterns) {

        List<KNode> trie = buildTrie(patterns.toArray(new String[0]));

        List<Integer> result = new ArrayList<Integer>();
        int i = 0;
        while (!text.isEmpty()) {
            if (prefixTrieMatching(text, trie)) {
                result.add(i);
            }
            text = text.substring(1);
            i++;
        }

        return result;
    }

    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            String text = in.readLine();
            int n = Integer.parseInt(in.readLine());
            List<String> patterns = new ArrayList<String>();
            for (int i = 0; i < n; i++) {
                patterns.add(in.readLine());
            }

            List<Integer> ans = solve(text, patterns);

            for (int j = 0; j < ans.size(); j++) {
                System.out.print("" + ans.get(j));
                System.out.print(j + 1 < ans.size() ? " " : "\n");
            }
        } catch (Throwable e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static void main(String[] args) {
        new Thread(new TrieMatchingExtended()).start();
    }
}