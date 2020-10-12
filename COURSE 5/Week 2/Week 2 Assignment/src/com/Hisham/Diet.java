package com.Hisham;

import java.io.*;
import java.util.*;

public class Diet {

    BufferedReader br;
    PrintWriter out;
    StringTokenizer st;
    boolean eof;

    private static final double EPSILON = 1.0E-6;

    private double[][] a;   // tableaux
    // row m   = objective function
    // row m+1 = artificial objective function
    // column n to n+m-1 = slack variables
    // column n+m to n+m+m-1 = artificial variables

    private int m;          // number of constraints
    private int n;          // number of original variables

    private int[] basis;    // basis[i] = basic variable corresponding to row i

    // sets up the simplex tableaux
    public int Simplex(double[][] A, double[] b, double[] c) {
        m = b.length;
        n = c.length;
        a = new double[m + 2][n + m + m + 1];
        basis = new int[m];

        for (int i = 0; i < m; i++) {
            System.arraycopy(A[i], 0, a[i], 0, n);
        }
        for (int i = 0; i < m; i++) {
            a[i][n + i] = 1.0;
            a[i][n + m + m] = b[i];
            basis[i] = n + m + i;
        }
        System.arraycopy(c, 0, a[m], 0, n);

        // if negative RHS, multiply by -1
        for (int i = 0; i < m; i++) {
            if (b[i] < 0) {
                for (int j = 0; j <= n + m + m; j++) {
                    a[i][j] = -a[i][j];
                }
            }
            a[i][n + m + i] = 1.0;
            a[m + 1][n + m + i] = -1.0;
            pivot(i, n + m + i);
        }

        int z = phase1();

        if (z == -1 || z == 1) {
            return z;
        }

        return phase2();
    }

    // run phase I simplex algorithm to find basic feasible solution
    private int phase1() {
        int q;
        while (true) {

            // find entering column q
            q = bland1();
            if (q == -1) break;  // optimal

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1) {
                return 1;
            }
            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
        }
        if (a[m + 1][n + m + m] >= EPSILON) return -1;
        return 0;
    }

    // run simplex algorithm starting from initial basic feasible solution
    private int phase2() {
        int q;
        while (true) {

            // find entering column q
            q = bland2();
            if (q == -1) {  // optimal
                return 0;
            }

            // find leaving row p
            int p = minRatioRule(q);
            if (p == -1) return 1;

            // pivot
            pivot(p, q);

            // update basis
            basis[p] = q;
        }
    }

    // lowest index of a non-basic column with a positive cost - using artificial objective function
    private int bland1() {
        for (int j = 0; j <n+m; j++) {
            if (a[m + 1][j] >= EPSILON) return j;
        }
        return -1;  // optimal
    }

    // lowest index of a non-basic column with a positive cost
    private int bland2() {
        for (int j = 0; j < n+m; j++) {
            if (a[m][j] >= EPSILON) return j;
        }
        return -1;  // optimal
    }

    // find row p using min ratio rule (-1 if no such row)
    private int minRatioRule(int q) {
        int p = -1;
        for (int i = 0; i < m; i++) {
            if (a[i][q] <= EPSILON) {
            } else if ((p == -1)) {
                p = i;
            } else if (((a[i][n + m + m] / a[i][q]) <= (a[p][n + m + m] / a[p][q])  + EPSILON)) {
                p = i;
            }
        }
        return p;
    }

    // pivot on entry (p, q) using Gauss-Jordan elimination
    private void pivot(int p, int q) {

        // everything but row p and column q
        for (int i = 0; i <= m + 1; i++) {
            if (i != p) {
                for (int j = 0; j <= n + m + m; j++) {
                    if (j != q) {
                        a[i][j] -= a[p][j] * a[i][q] / a[p][q];
                    }
                }
                a[i][q] = 0.0;
            }
            if (i == m + 1) {   // end of loop
                for (int j = 0; j <= n + m + m; j++) {
                    if (j != q) a[p][j] /= a[p][q];
                }
                a[p][q] = 1.0;
            }
        }
    }

    // return primal solution vector
    public double[] primal() {
        double[] x = new double[n];
        for (int i = 0; i < m; i++)
            if (basis[i] < n) x[basis[i]] = a[i][n + m + m];
        return x;
    }

    void solve() {
        int n = nextInt();
        int m = nextInt();
        double[][] A = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                A[i][j] = nextInt();
            }
        }
        double[] b = new double[n];
        for (int i = 0; i < n; i++) {
            b[i] = nextInt();
        }
        double[] c = new double[m];
        for (int i = 0; i < m; i++) {
            c[i] = nextInt();
        }
        double[] ansX;
        int ansT = Simplex(A,b,c);
        ansX=primal();
        if(ansT == -1){
            out.printf("No solution\n");
            return;
        }
        if (ansT == 0) {
            out.printf("Bounded solution\n");
            for (int i = 0; i < m; i++) {
                out.printf("%.18f%c", ansX[i], i + 1 == m ? '\n' : ' ');
            }
            return;
        }
        if (ansT == 1) {
            out.printf("Infinity\n");
        }
    }

    Diet() {
        br = new BufferedReader(new InputStreamReader(System.in));
        out = new PrintWriter(System.out);
        solve();
        out.close();
    }

    public static void main(String[] args) {
        new Diet();
    }

    String nextToken() {
        while (st == null || !st.hasMoreTokens()) {
            try {
                st = new StringTokenizer(br.readLine());
            } catch (Exception e) {
                eof = true;
                return null;
            }
        }
        return st.nextToken();
    }

    int nextInt() {
        return Integer.parseInt(nextToken());
    }
}