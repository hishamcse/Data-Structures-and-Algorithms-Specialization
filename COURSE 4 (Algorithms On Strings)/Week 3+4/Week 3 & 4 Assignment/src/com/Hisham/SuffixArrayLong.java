package com.Hisham;

import java.util.*;
import java.io.*;

public class SuffixArrayLong {

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

//    static class Suffix implements Comparable<Suffix> {
//        String suffix;
//        int start;
//
//        Suffix(String suffix, int start) {
//            this.suffix = suffix;
//            this.start = start;
//        }
//
//        @Override
//        public int compareTo(Suffix o) {
//            return suffix.compareTo(o.suffix);
//        }
//    }

    private int letterToIndex(char letter) {

        switch (letter) {
            case '$':
                return 0;
            case 'A':
                return 1;
            case 'C':
                return 2;
            case 'G':
                return 3;
            case 'T':
                return 4;
            default:
                return -1;
        }
    }

    private int[] sortChars(String s) {
        int[] order = new int[s.length()];
        int r = 5;
        int[] count = new int[r];

        for (int i = 0; i < s.length(); i++) {
            count[letterToIndex(s.charAt(i))] += 1;
        }
        for (int j = 1; j < r; j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            int c = letterToIndex(s.charAt(i));
            count[c] -= 1;
            order[count[c]] = i;
        }

//        System.out.println("sorted chars = "+Arrays.toString(order));
        return order;
    }

    private int[] computeCharClasses(String s, int[] order) {
        int[] classes = new int[s.length()];
        classes[order[0]] = 0;
        for (int i = 1; i < s.length(); i++) {
            if (letterToIndex(s.charAt(order[i])) != letterToIndex(s.charAt(order[i - 1]))) {
                classes[order[i]] = classes[order[i - 1]] + 1;
            } else {
                classes[order[i]] = classes[order[i - 1]];
            }
        }

//        System.out.println("char classes = "+ Arrays.toString(classes));
        return classes;
    }

    private int[] sortDoubled(String s, int l, int[] order, int[] classes) {
        int[] newOrder = new int[s.length()];
        int[] count = new int[s.length()];

        for (int i = 0; i < s.length(); i++) {
            count[classes[i]] += 1;
        }
        for (int j = 1; j < s.length(); j++) {
            count[j] = count[j] + count[j - 1];
        }
        for (int i = s.length() - 1; i >= 0; i--) {
            int start = (order[i] - l + s.length()) % s.length();
            int cl = classes[start];
            count[cl] -= 1;
            newOrder[count[cl]] = start;
        }

//        System.out.println("sort doubled = "+ Arrays.toString(newOrder));
        return newOrder;
    }

    private int[] updateClasses(int[] newOrder, int[] classes, int l) {
        int n = newOrder.length;
        int[] newClasses = new int[n];
        newClasses[newOrder[0]] = 0;

        for (int i = 1; i < n; i++) {
            int curr = newOrder[i];
            int prev = newOrder[i - 1];
            int midCurr = curr + l;
            int midPrev = (prev + l) % n;

            if (classes[curr] != classes[prev] || classes[midCurr] != classes[midPrev]) {
                newClasses[curr] = newClasses[prev] + 1;
            } else {
                newClasses[curr] = newClasses[prev];
            }
        }

//        System.out.println("update classes = "+ Arrays.toString(newClasses));
        return newClasses;
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        int[] order;
        int[] classes;

        order = sortChars(text);
        classes = computeCharClasses(text, order);
        int l = 1;

        while (l <= text.length()) {
            order = sortDoubled(text, l, order, classes);
            classes = updateClasses(order, classes, l);
            l = 2 * l;
        }

        return order;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayLong().run();
    }

    public void print(int[] x) {
        for (int a : x) {
            System.out.print(a + " ");
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        int[] suffix_array = computeSuffixArray(text);
        print(suffix_array);
    }
}