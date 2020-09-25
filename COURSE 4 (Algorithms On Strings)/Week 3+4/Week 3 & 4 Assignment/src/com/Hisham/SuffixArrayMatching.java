package com.Hisham;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class SuffixArrayMatching {

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

        return newClasses;
    }

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

    public List<Integer> findOccurrences(String pattern, String text, int[] suffixArray) {

        List<Integer> result = new ArrayList<>();
        int min = 0;
        int max = text.length();

        while (min < max) {
            int mid = (min + max) / 2;
            String str = text.substring(suffixArray[mid], Math.min(suffixArray[mid] + pattern.length(), text.length()));
            if (pattern.compareTo(str) > 0) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }
        int start = min;
        max = text.length();
        while (min < max) {
            int mid = (min + max) / 2;
            String str = text.substring(suffixArray[mid], Math.min(suffixArray[mid] + pattern.length(), text.length()));
            if (pattern.compareTo(str) < 0) {
                max = mid;
            } else {
                min = mid+1;
            }
        }
        int end = max;
        if (start <= end) {
            for (int i = start; i < end; i++) {
                result.add(suffixArray[i]);
            }
        }

        return result;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArrayMatching().run();
    }

    public void print(boolean[] x) {
        for (int i = 0; i < x.length; ++i) {
            if (x[i]) {
                System.out.print(i + " ");
            }
        }
        System.out.println();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next() + "$";
        int[] suffixArray = computeSuffixArray(text);
        int patternCount = scanner.nextInt();
        boolean[] occurs = new boolean[text.length()];
        for (int patternIndex = 0; patternIndex < patternCount; ++patternIndex) {
            String pattern = scanner.next();
            List<Integer> occurrences = findOccurrences(pattern, text, suffixArray);
            for (int x : occurrences) {
                occurs[x] = true;
            }
        }
        print(occurs);
    }
}