package com.Hisham;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

// this is an alternate and easy implementation of BWT

public class BurrowsWheelerTransformAlternate {

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

    String BWT(String text) {

        StringBuilder builder = new StringBuilder(text);
        List<String> circularStrings = new LinkedList<>();
        circularStrings.add(text);

        for (int i = 0; i < text.length() - 1; i++) {
            builder.append(builder.charAt(0));
            builder.delete(0, 1);
            circularStrings.add(builder.toString());
        }

        Collections.sort(circularStrings);

        StringBuilder result = new StringBuilder();
        for (String str : circularStrings) {
            result.append(str.charAt(text.length() - 1));
        }

        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new BurrowsWheelerTransformAlternate().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String text = scanner.next();
        System.out.println(BWT(text));
    }
}