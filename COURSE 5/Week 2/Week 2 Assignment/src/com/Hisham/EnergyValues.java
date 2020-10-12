package com.Hisham;

import java.util.Scanner;

// this implementation partially taken from MY COMPLETED 'Algorithms Part II' course (Princeton University)

class Equation {

    double[][] a;
    double[] b;

    Equation(double[][] a, double[] b) {
        this.a = a;
        this.b = b;
    }
}

class EnergyValues {

    static Equation ReadEquation() {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        if (size == 0) {
            return null;
        }

        double[][] a = new double[size][size];
        double[] b = new double[size];
        for (int raw = 0; raw < size; ++raw) {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        return new Equation(a, b);
    }

    private static final double EPSILON = 1e-8;

    private static int m;      // number of rows
    private static int n;      // number of columns
    private static double[][] a;     // m-by-(n+1) augmented matrix

    public static void GaussianElimination(double[][] A, double[] b) {
        m = A.length;
        n = A[0].length;

        // build augmented matrix
        a = new double[m][n + 1];
        for (int i = 0; i < m; i++)
            for (int j = 0; j < n; j++)
                a[i][j] = A[i][j];
        for (int i = 0; i < m; i++)
            a[i][n] = b[i];

        forwardElimination();
    }

    // forward elimination
    private static void forwardElimination() {

        for (int p = 0; p < Math.min(m, n); p++) {

            // find pivot row using partial pivoting
            int max = p;
            for (int i = p + 1; i < m; i++) {
                if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                    max = i;
                }
            }

            // swap
            swap(p, max);

            // singular or nearly singular
            if (Math.abs(a[p][p]) <= EPSILON) {
                continue;
            }

            // pivot
            pivot(p);
        }
    }

    // swap row1 and row2
    private static void swap(int row1, int row2) {
        double[] temp = a[row1];
        a[row1] = a[row2];
        a[row2] = temp;
    }

    // pivot on a[p][p]
    private static void pivot(int p) {
        for (int i = p + 1; i < m; i++) {
            double alpha = a[i][p] / a[p][p];
            for (int j = p; j <= n; j++) {
                a[i][j] -= alpha * a[p][j];
            }
        }
    }

    public static double[] primal() {
        // back substitution
        double[] x = new double[n];
        for (int i = Math.min(n - 1, m - 1); i >= 0; i--) {
            double sum = 0.0;
            for (int j = i + 1; j < n; j++) {
                sum += a[i][j] * x[j];
            }

            if (Math.abs(a[i][i]) > EPSILON)
                x[i] = (a[i][n] - sum) / a[i][i];
            else if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }

        // redundant rows
        for (int i = n; i < m; i++) {
            double sum = 0.0;
            for (int j = 0; j < n; j++) {
                sum += a[i][j] * x[j];
            }
            if (Math.abs(a[i][n] - sum) > EPSILON)
                return null;
        }
        return x;
    }

    static double[] SolveEquation(Equation equation) {
        if (equation == null) {
            return null;
        }
        double[][] a = equation.a;
        double[] b = equation.b;

        GaussianElimination(a, b);
        b = primal();

        return b;
    }

    static void PrintColumn(double[] column) {
        if (column == null) {
            return;
        }
        for (double v : column) System.out.printf("%.20f\n", v);
    }

    public static void main(String[] args) {
        Equation equation = ReadEquation();
        double[] solution = SolveEquation(equation);
        PrintColumn(solution);
    }
}