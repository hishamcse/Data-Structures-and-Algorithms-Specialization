import java.util.*;
import java.io.*;

public class substring_equality {

	private long m1, m2;
	private long R;
	private long[] h1, h2;

	public class Solver {
		private final String s;

		public Solver(String s) {
			this.s = s;
		}

		public boolean ask(int a, int b, int l) {
			return equal(a, b, l);
		}
	}

	private long[] preComputeHashes(String s, long q) {
		long[] h = new long[s.length() + 1];
		h[0] = 0;
		for (int i = 1; i <= s.length(); i++) {
			h[i] = ((R * h[i - 1]) % q) + q + s.charAt(i - 1);
			h[i] = h[i] % q;
		}
		return h;
	}

	private long power(long m,int l){
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

	private boolean equal(int a, int b, int l) {
		long power1 = power(m1,l);
		long value1A = h1[a + l] - ((power1 * h1[a]) % m1);
		if(value1A<0){
			value1A+=m1;
		}
		long value1B = h1[b + l] - ((power1 * h1[b]) % m1);
		if(value1B<0){
			value1B+=m1;
		}

		long power2 = power(m2,l);
		long value2A = h2[a + l] - ((power2 * h2[a]) % m2);
		if(value2A<0){
			value2A+=m2;
		}
		long value2B = h2[b + l] - ((power2 * h2[b]) % m2);
		if(value2B<0){
			value2B+=m2;
		}

		return value1A == value1B && value2A == value2B;

	}

	public void run() throws IOException {
		FastScanner in = new FastScanner();
		PrintWriter out = new PrintWriter(System.out);
		String s = in.next();
		int p = in.nextInt();
		m1 = 1000000007;
		m2 = 1000000009;

		R = 13;
		h1 = preComputeHashes(s, m1);
		h2 = preComputeHashes(s, m2);

		Solver solver = new Solver(s);
		for (int i = 0; i < p; i++) {
			int a = in.nextInt();
			int b = in.nextInt();
			int l = in.nextInt();
			out.println(solver.ask(a, b, l) ? "Yes" : "No");
		}
		out.close();
	}

	static public void main(String[] args) throws IOException {
		new substring_equality().run();
	}

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
}

