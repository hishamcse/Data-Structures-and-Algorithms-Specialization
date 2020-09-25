import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class BuildHeap {

    private static List<Swap> swaps;
    private int[] data;
    private int n;

    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        new BuildHeap().solve();
    }

    private static void writeResponse() {
        out.println(swaps.size());
        for (Swap swap : swaps) {
            out.println(swap.index1 + " " + swap.index2);
        }
    }

    private void sink(int k) {
        while (2 * k <= n) {
            int j = 2 * k;
            if (j < n && greater(j, j + 1)) {
                j++;
            }
            if (!greater(k, j)) {
                break;
            }
            swap(data, k, j);
            k = j;
        }
    }

    private boolean greater(int i, int j) {
        return data[i] > data[j];
    }

    private void swap(int[] data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        swaps.add(new Swap(i - 1, j - 1));
    }

    private void generateSwaps() {
        swaps = new ArrayList<Swap>();

        for (int i = n / 2; i >= 1; i--) {
            sink(i);
        }
    }

    public void solve() throws IOException {
        FastScanner in = new FastScanner();
        out = new PrintWriter(new BufferedOutputStream(System.out));
        n = in.nextInt();
        data = new int[n + 1];
        for (int i = 1; i <= n; ++i) {    // as our original implementation was 1 indexed
            data[i] = in.nextInt();
        }
        generateSwaps();
        writeResponse();
        out.close();
    }

    static class Swap {
        int index1;
        int index2;

        public Swap(int index1, int index2) {
            this.index1 = index1;
            this.index2 = index2;
        }
    }

    static class FastScanner {
        private final BufferedReader reader;
        private StringTokenizer tokenizer;

        public FastScanner() {
            reader = new BufferedReader(new InputStreamReader(System.in));
            tokenizer = null;
        }

        public String next() throws IOException {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                tokenizer = new StringTokenizer(reader.readLine());
            }
            return tokenizer.nextToken();
        }

        public int nextInt() throws IOException {
            return Integer.parseInt(next());
        }
    }
}

