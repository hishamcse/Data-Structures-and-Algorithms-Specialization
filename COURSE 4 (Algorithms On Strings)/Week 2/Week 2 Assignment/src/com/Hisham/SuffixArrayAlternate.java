package com.Hisham;

import java.util.*;
import java.io.*;

// alternative but easy implementation of suffix array problem

public class SuffixArrayAlternate {

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

    static class Suffix implements Comparable<Suffix> {
        String suffix;
        int start;

        Suffix(String suffix, int start) {
            this.suffix = suffix;
            this.start = start;
        }

        @Override
        public int compareTo(Suffix o) {
            return suffix.compareTo(o.suffix);
        }
    }

    // Build suffix array of the string text and
    // return an int[] result of the same length as the text
    // such that the value result[i] is the index (0-based)
    // in text where the i-th lexicographically smallest
    // suffix of text starts.
    public int[] computeSuffixArray(String text) {
        List<Suffix> list = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            list.add(new Suffix(text.substring(i), i));
        }
        Collections.sort(list);
        int[] res = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            res[i] = list.get(i).start;
        }
        return res;
    }


    static public void main(String[] args) throws IOException {
        new SuffixArrayAlternate().run();
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
        int[] SuffixArray = computeSuffixArray(text);
        print(SuffixArray);
    }
}