import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class InverseBWT {

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

    String inverseBWT(String bwt) {

        int first = -1;
        for (int i = 0; i < bwt.length(); i++) {
            if (bwt.charAt(i) == '$') {
                first = i;
                break;
            }
        }

        char[] chars = bwt.toCharArray();
        int n = chars.length;
        StringBuilder result = new StringBuilder();

        int[] count = new int[257];
        int[] next = new int[n];
        for (char c : chars) {
            count[c + 1]++;
        }
        for (int i = 0; i < 256; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < n; i++) {
            next[count[chars[i]]++] = i;
        }
        int index = next[first];
        for (int i = 0; i < n; i++, index = next[index]) {
            result.append(chars[index]);
        }

        return result.toString();
    }

    static public void main(String[] args) throws IOException {
        new InverseBWT().run();
    }

    public void run() throws IOException {
        FastScanner scanner = new FastScanner();
        String bwt = scanner.next();
        System.out.println(inverseBWT(bwt));
    }
}