import java.util.Scanner;

public class PlacingParentheses {

    private static long[] MinMax(int i, int j, long[][] m, long[][] M, char[] op) {
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (int k = i; k < j; k++) {
            long a = eval(M[i][k], M[k + 1][j], op[k]);
            long b = eval(M[i][k], m[k + 1][j], op[k]);
            long c = eval(m[i][k], m[k + 1][j], op[k]);
            long d = eval(m[i][k], M[k + 1][j], op[k]);
            min = findMin(min, a, b, c, d);
            max = findMax(max, a, b, c, d);
        }
        return new long[]{min, max};
    }

    private static long findMin(long a, long b, long c, long d, long e) {
        return Math.min(a, Math.min(b, Math.min(c, Math.min(d, e))));
    }

    private static long findMax(long a, long b, long c, long d, long e) {
        return Math.max(a, Math.max(b, Math.max(c, Math.max(d, e))));
    }

    private static long eval(long a, long b, char op) {
        if (op == '+') {
            return a + b;
        } else if (op == '-') {
            return a - b;
        } else if (op == '*') {
            return a * b;
        } else {
            assert false;
            return 0;
        }
    }

    public static long parenthesis(String exp) {
        long[] d = new long[exp.length() / 2 + 1];
        char[] op = new char[exp.length()];
        int x = 0;
        for (int i = 0; i < exp.length(); i += 2) {
            d[x++] = exp.charAt(i) - 48;
        }
        x = 0;
        for (int i = 1; i < exp.length(); i += 2) {
            op[x++] = exp.charAt(i);
        }
        long[][] m = new long[d.length][d.length];
        long[][] M = new long[d.length][d.length];
        for (int i = 0; i < d.length; i++) {
            m[i][i] = d[i];
            M[i][i] = d[i];
        }
        for (int s = 1; s < d.length; s++) {
            for (int i = 0; i < d.length - s; i++) {
                int j = i + s;
                long[] minmax = MinMax(i, j, m, M, op);
                m[i][j] = minmax[0];
                M[i][j] = minmax[1];
            }
        }
        return M[0][d.length - 1];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String exp = scanner.next();
        System.out.println(parenthesis(exp));
    }
}


