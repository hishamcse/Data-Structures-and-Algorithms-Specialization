package com.Hisham;

import java.util.*;
import java.io.*;

public class common_substring {

    private static final int R = 13;
    private static long m1 = 1000000007;
    private static long m2 = 1000000009;

    public static class Answer {
        int i, j, length;

        Answer(int i, int j, int length) {
            this.i = i;
            this.j = j;
            this.length = length;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Answer answer = (Answer) o;
            return i == answer.i &&
                    j == answer.j &&
                    length == answer.length;
        }

        @Override
        public int hashCode() {
            return Objects.hash(i, j, length);
        }
    }

    private Answer solve(String s, String t) {
        Answer answer = new Answer(0, 0, 0);
        int left = 0;
        int right = Math.min(s.length(), t.length());
        while (left <= right) {
            int mid = (int) Math.floor((float) (left + right) / 2);

            Answer commonSubString = commonSubString(s, t, mid);
            if (commonSubString != null) {
                left = mid + 1;
                answer = commonSubString;
            } else {
                right = mid - 1;
            }
        }
        return answer;
    }

    private Answer commonSubString(String s, String t, int length) {
        Set<Answer> ans1 = possibleMatches(s, t, length, m1);
        Set<Answer> ans2 = possibleMatches(s, t, length, m2);

        for (Answer fromAns1 : ans1) {
            if (ans2.contains(fromAns1)) {
                return fromAns1;
            }
        }
        return null;
    }

    private Set<Answer> possibleMatches(String s, String t, int l, long m) {
        long power = power(m, l);

        long[] h1 = preComputeHashes(s, l, m, power);

        Map<Long, Integer> map = new HashMap<>();
        for (int i = 0; i < h1.length; i++) {
            map.put(h1[i], i);
        }

        long[] h2 = preComputeHashes(t, l, m, power);

        Set<Answer> possibleAnswers = new HashSet<>();
        for (int j = 0; j < h2.length; j++) {
            Integer i = map.get(h2[j]);
            if (i != null) {
                possibleAnswers.add(new Answer(i, j, l));
            }
        }
        return possibleAnswers;
    }

    private static long power(long m, int l) {
        long result = 1;
        long base = R;
        while (l > 0) {
            if (l % 2 == 1) {
                result = (result * base) % m;
            }
            base = (base * base) % m;
            l /= 2;
        }
        return result % m;
    }

    private static long[] preComputeHashes(String text, int subLength, long m, long power) {
        int textLength = text.length();
        String lastSubstring = text.substring(textLength - subLength, textLength);
        long[] h = new long[textLength - subLength + 1];
        h[h.length - 1] = hash(lastSubstring, m);

        for (int i = h.length - 2; i >= 0; i--) {
            char last = text.charAt(i + subLength);
            char first = text.charAt(i);
            long hash = (R * h[i + 1] + first - last * power) % m;
            if (hash < 0) hash += m;
            h[i] = hash;
        }
        return h;
    }

    private static int hash(String string, long m) {
        long hash = 0;
        for (int i = string.length() - 1; i >= 0; i--)
            hash = (hash * R + string.charAt(i)) % m;
        return (int) hash;
    }

    private void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            if (line != null && !line.isEmpty()) {
                StringTokenizer tok = new StringTokenizer(line);
                String s = tok.nextToken();
                String t = tok.nextToken();
                Answer ans = solve(s, t);
                out.format("%d %d %d\n", ans.i, ans.j, ans.length);
            }
        });
        out.close();
    }

    static public void main(String[] args) {
        new common_substring().run();
    }
}

