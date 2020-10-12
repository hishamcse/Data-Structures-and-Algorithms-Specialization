package com.Hisham;

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

            int count = 0;
            for (int i = 0; i < A.length; ++i) {
                int[] row = A[i];
                int n = 0;

                // count of nonzero values in a row
                for (int value : row) {
                    if (value != 0) {
                        n++;
                    }
                }

                for (int j = 0, limit = (int) Math.pow(2, n); j < limit; ++j) {

                    // 000 or 001 or 011 or 111 (we only see the last 3 digits)
                    char[] st = Integer.toBinaryString(j).toCharArray();
                    int cnt = 0;

                    // last 3 digit.if the digit is 0, means false.otherwise true
                    for (int k = st.length - 1; k >= 0 && k >= st.length - 3; k--) {
                        combinations.set(cnt, st[k] == '1');
                        cnt++;
                    }
                    // if binaryString length is less than 3.so,set the rest of the combinations bitset false
                    while (cnt < 3) {
                        combinations.set(cnt, false);
                        cnt++;
                    }

                    int sum = 0, com = 0;
                    for (int element : row) {
                        if (element != 0 && combinations.get(com++)) {
                            sum += element;
                        }
                    }

                    // non zero and >b[i] will be added at CNF as negation
                    if (sum > b[i]) {
                        boolean in_Clauses = false;
                        for (int k = 0, c = 0; k < row.length; ++k) {
                            if (row[k] != 0) {
                                String str = combinations.get(c) ? -(k + 1) + " " : (k + 1) + " ";
                                clauses.append(str);
                                ++c;
                                in_Clauses = true;
                            }
                        }
                        if (in_Clauses) {
                            clauses.append("0\n");
                            ++count;
                        }
                    }
                }
            }
            // means no solution exists.so,unsatisfiable
            if (count == 0) {
                count++;
                clauses.append("1 -1 0\n");
            }
            writer.printf(count + " " + A[0].length + "\n");
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