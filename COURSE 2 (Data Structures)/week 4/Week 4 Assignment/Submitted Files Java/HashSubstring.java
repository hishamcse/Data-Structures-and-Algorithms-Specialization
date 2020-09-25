import java.io.*;
import java.util.*;

public class HashSubstring {

    private final String pat;
    private final long patHash;
    private final int m;
    private final long q;
    private final int R;
    private long RM;

    public HashSubstring(String pattern) {
        m = pattern.length();
        R = 256;
        q = (long) (1e9 + 7);
        pat = pattern;

        RM = 1;
        for (int i = 1; i < m; i++) {
            RM = (R * RM + q) % q;
        }
        patHash = hash(pattern, m);
    }

    private long hash(String pattern, int m) {        // polynomial hash family
        long hash = 0;
        for (int i = 0; i < m; i++) {
            hash = ((R * hash) + pattern.charAt(i));
            hash = (hash % q + q) % q;
        }
        return hash;
    }

    // las vagas version to check
    private boolean checkLasVagas(String text, int i) {
        for (int j = 0; j < m; j++) {
            if (pat.charAt(j) != text.charAt(i + j)) {
                return false;
            }
        }
        return true;
    }

    public ArrayList<Integer> search(String key) {
        ArrayList<Integer> list=new ArrayList<>();   // all the occurrences
        int n = key.length();
        long textHash = hash(key, m);
        if (textHash == patHash && checkLasVagas(key, 0)) {   // textHash==patHash is monte carlo check
            list.add(0);
        }
        for (int i = m; i < n; i++) {
            textHash=(textHash + q - RM*key.charAt(i-m) % q) % q;
            textHash = (textHash * R + key.charAt(i)) % q;
            if (textHash == patHash && checkLasVagas(key, i - m + 1)) {
                list.add(i-m+1);
            }
        }
        return list;
    }

    public static void main(String[] args) throws IOException {
        FastScanner in =new FastScanner();
        PrintWriter out=new PrintWriter(new BufferedOutputStream(System.out));
        String pat = in.next();
        String txt = in.next();

        HashSubstring searcher = new HashSubstring(pat);
        ArrayList<Integer> offset = searcher.search(txt);

        for (Integer integer : offset) {
            out.print(integer + " ");
        }
        out.close();
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
