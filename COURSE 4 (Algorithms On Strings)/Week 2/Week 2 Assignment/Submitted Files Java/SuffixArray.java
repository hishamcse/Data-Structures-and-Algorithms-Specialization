import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class SuffixArray {

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

    private static int n;
    private static char[] str;

    static class CircularSuffix implements Comparable<CircularSuffix> {
        int index;

        public CircularSuffix(int index) {
            this.index = index;
        }

        @Override
        public int compareTo(CircularSuffix that) {
            for (int i = 0; i < n; i++) {
                if (str[(this.index + i) % n] < str[(that.index + i) % n]) {
                    return -1;
                }
                if (str[(this.index + i) % n] > str[(that.index + i) % n]) {
                    return 1;
                }
            }
            return 0;
        }
    }

    // circular suffix array of s
    public int[] computeSuffixArray(String s) {
        n = s.length();
        str = s.toCharArray();
        int[] indices = new int[n];

        CircularSuffix[] suffixes = new CircularSuffix[n];
        for (int i = 0; i < n; i++) {
            suffixes[i] = new CircularSuffix(i);
        }
        Arrays.sort(suffixes);

        for (int i = 0; i < n; i++) {
            indices[i] = suffixes[i].index;
        }

        return indices;
    }

    static public void main(String[] args) throws IOException {
        new SuffixArray().run();
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
