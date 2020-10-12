import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Heavy_Hitters {

    static class HashFunction {
        long a, b, p, m;

        public HashFunction(long a, long b, long p, long m) {
            this.a = a;
            this.b = b;
            this.p = p;
            this.m = m;
        }

        public long hash(long x) {
            return Math.abs(((a * x + b) % p) % m);
        }
    }

    static class SignFunction {
        HashFunction hashFunc;

        public SignFunction(HashFunction hashFunc) {
            this.hashFunc = hashFunc;
        }

        public int sign(long x) {
            return hashFunc.hash(x) < 500 ? -1 : 1;
        }
    }

    int[][] C;
    List<HashFunction> hashList;
    List<SignFunction> signList;
    int n, t, b;
    long prime = (long) (10e8 + 19);

    Heavy_Hitters(int n) {
        this.n = n;
        t = (int) (1 + Math.log(n));
        b = (int) (1850 * Math.log(n));
        C = new int[t][b];
        hashList = new ArrayList<>();
        signList = new ArrayList<>();
        Random random = new Random(1000000000L);

        for (int r = 0; r < t; r++) {
            hashList.add(new HashFunction(random.nextLong() % 5000 + 3, random.nextLong() % 5000 + 3, prime, b));
            HashFunction hashForSign = new HashFunction(random.nextLong() % 1000 + 10, random.nextLong() % 1000 + 10,
                    prime, 1000);
            signList.add(new SignFunction(hashForSign));
        }
    }

    private void update(int i, long val) {
        for (int r = 0; r < t; r++) {
            C[r][(int) hashList.get(r).hash(i)] += (signList.get(r).sign(i) * val);
        }
    }

    private long estimate(int i) {
        int[] estimations = new int[t];
        for (int r = 0; r < t; r++) {
            estimations[r] = C[r][(int) hashList.get(r).hash(i)] * signList.get(r).sign(i);
        }
        Arrays.sort(estimations);
        if (t % 2 == 0) {
            return (estimations[(t / 2) - 1] + estimations[t / 2]) / 2;
        }
        return estimations[t / 2];
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

    public static void main(String[] args) {
        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int threshold = scanner.nextInt();

        Heavy_Hitters heavy_hitters = new Heavy_Hitters(n);

        for (int i = 0; i < n; i++) {
            heavy_hitters.update(scanner.nextInt(), scanner.nextInt());
        }

        for (int i = 0; i < n; i++) {
            heavy_hitters.update(scanner.nextInt(), -scanner.nextInt());
        }

        int query = scanner.nextInt();
        for (int i = 0; i < query; i++) {
            if (heavy_hitters.estimate(scanner.nextInt()) >= threshold) {
                System.out.print(1 + " ");
            } else {
                System.out.print(0 + " ");
            }
        }
    }
}