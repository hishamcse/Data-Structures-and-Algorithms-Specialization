import java.io.*;
import java.util.BitSet;
import java.util.Locale;
import java.util.StringTokenizer;

public class BudgetAllocation {

    private final InputReader reader;
    private final OutputWriter writer;

    public BudgetAllocation(InputReader reader, OutputWriter writer) {
        this.reader = reader;
        this.writer = writer;
    }

    public static void main(String[] args) {
        InputReader reader = new InputReader(System.in);
        OutputWriter writer = new OutputWriter(System.out);
        new BudgetAllocation(reader, writer).run();
        writer.writer.flush();
    }

    class ConvertILPToSat {
        int[][] A;
        int[] b;

        ConvertILPToSat(int n, int m) {
            A = new int[n][m];
            b = new int[n];
        }

        void printEquiSatisfiableSatFormula() {

            BitSet combinations = new BitSet(3);
            StringBuilder clauses = new StringBuilder();

            int cnt = 0;
            for (int i = 0; i < A.length; ++i) {
                int[] row = A[i];
                int n = 0;

                // count of nonzero value in a row
                for (int value : row) {
                    if (value != 0) {
                        n++;
                    }
                }

                for (int j = 0, s = (int) Math.pow(2, n); j < s; ++j) {

                    // 000 or 001 or 011 or 111
                    char[] st = Integer.toBinaryString(j).toCharArray();
                    int cn = 0;

                    // last 3 digit.if the digit is 0, means false.otherwise true
                    for (int k = st.length - 1; k >= 0 && k >= st.length - 3; k--) {
                        combinations.set(cn, st[k] == '1');
                        cn++;
                    }
                    // if binaryString length is less than 3.so,set the rest of the combinations bitset false
                    while (cn < 3) {
                        combinations.set(cn, false);
                        cn++;
                    }

                    int sum = 0, comb = 0;
                    for (int x : row) {
                        if (x != 0 && combinations.get(comb++)) {
                            sum += x;
                        }
                    }

                    // non zero and >b[i] will be added at CNF as negation as they are false
                    if (sum > b[i]) {
                        boolean is_clause = false;
                        for (int k = 0, c = 0; k < row.length; ++k) {
                            if (row[k] != 0) {
                                String str = combinations.get(c) ? -(k + 1) + " " : (k + 1) + " ";
                                clauses.append(str);
                                ++c;
                                is_clause = true;
                            }
                        }
                        if (is_clause) {
                            clauses.append("0\n");
                            ++cnt;
                        }
                    }
                }
            }
            if (cnt == 0) {
                cnt++;
                clauses.append("1 -1 0\n");
            }
            writer.printf(cnt + " " + A[0].length + "\n");
            writer.printf(clauses.toString());
        }
    }

    public void run() {
        int n = reader.nextInt();
        int m = reader.nextInt();

        ConvertILPToSat converter = new ConvertILPToSat(n, m);
        for (int i = 0; i < n; ++i) {
            for (int j = 0; j < m; ++j) {
                converter.A[i][j] = reader.nextInt();
            }
        }
        for (int i = 0; i < n; ++i) {
            converter.b[i] = reader.nextInt();
        }
        converter.printEquiSatisfiableSatFormula();
    }

    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }
    }

    static class OutputWriter {
        public PrintWriter writer;

        OutputWriter(OutputStream stream) {
            writer = new PrintWriter(stream);
        }

        public void printf(String format, Object... args) {
            writer.print(String.format(Locale.ENGLISH, format, args));
        }
    }
}