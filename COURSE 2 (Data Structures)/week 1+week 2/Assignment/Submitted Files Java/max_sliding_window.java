import java.util.*;
import java.io.*;

public class max_sliding_window {

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

    public void solve() throws IOException {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; ++i) a[i] = scanner.nextInt();
        int m = scanner.nextInt();
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < n; i++) {
            if (!deque.isEmpty() && deque.peekFirst() == i - m) {
                deque.pollFirst();
            }
            while (!deque.isEmpty() && a[deque.peekLast()] < a[i]) {
                deque.pollLast();
            }
            deque.offer(i);
            if (i + 1 >= m) {
                System.out.println(a[deque.peekFirst()]);
            }
        }
        System.out.println();
    }

    static public void main(String[] args) throws IOException {
        new max_sliding_window().solve();
    }
}
