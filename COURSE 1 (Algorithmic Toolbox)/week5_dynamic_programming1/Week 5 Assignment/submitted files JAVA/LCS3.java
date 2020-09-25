import java.util.Scanner;

public class LCS3 {

    private static int lcs3(int[] a, int[] b, int[] c) {
        int[][][] d = new int[a.length + 1][b.length + 1][c.length + 1];
        for (int k = 0; k <= c.length; k++) {
            for (int j = 0; j <= b.length; j++) {
                for (int i = 0; i <= a.length; i++) {
                    if (i == 0 || j == 0 || k == 0) {
                        d[i][j][k] = 0;
                    } else {
                        int insertion = d[i][j - 1][k];
                        int deletion = d[i - 1][j][k];
                        int extra = d[i][j][k - 1];
                        int match = d[i - 1][j - 1][k - 1];
                        if (a[i - 1] == b[j - 1] && a[i - 1] == c[k - 1]) {
                            d[i][j][k] = 1 + match;
                        } else {
                            d[i][j][k] = Math.max(extra, Math.max(insertion, deletion));
                        }
                    }
                }
            }
        }
        return d[a.length][b.length][c.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        System.out.println(lcs3(a, b, c));
    }
}


