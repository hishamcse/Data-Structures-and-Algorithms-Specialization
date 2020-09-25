import java.util.*;
import java.util.stream.Collectors;
import java.io.*;

public class matching_with_mismatches {

    private static final int R = 31;
    private static long m1 = 1000000007;
    private static long m2 = 1000000009;
    private long[] hText1, hPat1, hText2, hPat2;
    private int k;

    public ArrayList<Integer> solve(int k, String text, String pattern) {
        this.k = k;
        ArrayList<Integer> pos = new ArrayList<>();

        for (int i = 0; i <= text.length() - pattern.length(); i++) {
            int mismatch = numberOfMismatches(i, pattern.length(), i, 0);

            if (mismatch <= k) {
                pos.add(i);
            }
        }

        return pos;
    }

    private int numberOfMismatches(int index, int len, int i, int mismatch) {
        if (mismatch > k) {
            return mismatch;
        }
        if (len == 1 && NotEqual(index, index - i, len)) {
            return mismatch + 1;
        } else {
            int mid = len / 2;
            if (NotEqual(index, index - i, mid)) {
                mismatch = numberOfMismatches(index, mid, i, mismatch);
            }
            int textIndex = index + mid;
            int newLen = len - mid;

            if (mismatch <= k && NotEqual(textIndex, textIndex - i, newLen)) {
                return numberOfMismatches(textIndex, newLen, i, mismatch);
            } else {
                return mismatch;
            }
        }
    }

    private boolean NotEqual(int textIndex, int patternIndex, int len) {

        long power1 = power(m1, len);

        long textHash1 = subStringHash(textIndex, len, hText1, m1,power1);
        long patHash1 = subStringHash(patternIndex, len, hPat1, m1,power1);

        if (textHash1 == patHash1) {
            long power2 = power(m2, len);
            long textHash2 = subStringHash(textIndex, len, hText2, m2,power2);
            long patHash2 = subStringHash(patternIndex, len, hPat2, m2,power2);

            return textHash2 != patHash2;
        }
        return true;
    }

    private static long power(long m, long l) {
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

    private long[] preComputeHashes(String s, long m) {
        long[] h = new long[s.length() + 1];
        h[0] = 0;
        for (int i = 1; i <= s.length(); i++) {
            h[i] = ((R * h[i - 1]) % m) + m + s.charAt(i - 1);
            h[i] = h[i] % m;
        }
        return h;
    }

    private long subStringHash(int a, int l, long[] h, long m,long power) {
        long value = h[a + l] - ((power * h[a]) % m);
        if (value < 0) {
            value += m;
        }
        return value;
    }

    public void run() {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter out = new PrintWriter(System.out);
        in.lines().forEach(line -> {
            if (line != null && !line.isEmpty()) {
                StringTokenizer tok = new StringTokenizer(line);
                int k = Integer.parseInt(tok.nextToken());
                String s = tok.nextToken();
                String t = tok.nextToken();

                hText1 = preComputeHashes(s, m1);
                hPat1 = preComputeHashes(t, m1);

                hText2 = preComputeHashes(s, m2);
                hPat2 = preComputeHashes(t, m2);

                ArrayList<Integer> ans = solve(k, s, t);
                out.format("%d ", ans.size());
                out.println(ans.stream()
                        .map(String::valueOf)
                        .collect(Collectors.joining(" "))
                );
            }
        });
        out.close();
    }

    static public void main(String[] args) {
        new matching_with_mismatches().run();
    }
}

