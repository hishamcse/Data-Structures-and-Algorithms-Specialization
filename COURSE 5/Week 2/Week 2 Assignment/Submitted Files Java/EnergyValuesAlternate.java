import java.util.Scanner;

class equation {

    double[][] a;
    double[] b;

    equation(double[][] a, double[] b) {
        this.a = a;
        this.b = b;
    }
}

class position {

    int column;
    int raw;

    position(int column, int raw) {
        this.column = column;
        this.raw = raw;
    }
}

class EnergyValuesAlternate {

    static equation ReadEquation() {
        Scanner scanner = new Scanner(System.in);
        int size = scanner.nextInt();

        double[][] a = new double[size][size];
        double[] b = new double[size];
        for (int raw = 0; raw < size; ++raw) {
            for (int column = 0; column < size; ++column)
                a[raw][column] = scanner.nextInt();
            b[raw] = scanner.nextInt();
        }
        return new equation(a, b);
    }

    static position SelectPivotElement(double[][] a, int p) {
        // This algorithm selects the first free element.
        // You'll need to improve it to pass the problem.
        position pivot_element = new position(0, 0);
        int m = a.length;
        int max = p;
        for (int i = p + 1; i < m; i++) {
            if (Math.abs(a[i][p]) > Math.abs(a[max][p])) {
                max = i;
            }
        }
        pivot_element.raw = max;
        pivot_element.column = p;
        return pivot_element;
    }

    static void SwapLines(double[][] a, double[] b, boolean[] used_raws, position pivot_element) {
        int size = a.length;

        for (int column = 0; column < size; ++column) {
            double tmpa = a[pivot_element.column][column];
            a[pivot_element.column][column] = a[pivot_element.raw][column];
            a[pivot_element.raw][column] = tmpa;
        }

        double tmpb = b[pivot_element.column];
        b[pivot_element.column] = b[pivot_element.raw];
        b[pivot_element.raw] = tmpb;

        boolean tmpu = used_raws[pivot_element.column];
        used_raws[pivot_element.column] = used_raws[pivot_element.raw];
        used_raws[pivot_element.raw] = tmpu;

        pivot_element.raw = pivot_element.column;
    }

    static void ProcessPivotElement(double[][] a, double[] b, position pivot_element) {
        // Write your code here
        int col = pivot_element.column;
        int row = pivot_element.raw;
        int m = b.length;
        int n = a[0].length;
        for (int i = col + 1; i < m; i++) {
            double alpha = a[i][col] / a[row][col];
            b[i] -= alpha * b[row];
            for (int j = col; j < n; j++) {
                a[i][j] -= alpha * a[row][j];
            }
        }
    }

    static void MarkPivotElementUsed(position pivot_element, boolean[] used_raws, boolean[] used_columns) {
        used_raws[pivot_element.raw] = true;
        used_columns[pivot_element.column] = true;
    }

    private static final double EPSILON = 1e-8;

    public static void solution(double[][] a,double[] b) {
        // back substitution
        int m=a.length;
        int n=a[0].length;

        for (int i = m-1; i >= 0; i--) {
            double sum = 0.0;
            for (int j = i+1; j < n; j++) {
                sum += a[i][j] * b[j];
            }

            b[i]-=sum;
            b[i]/=a[i][i];
        }
    }


    static double[] SolveEquation(equation equation1) {
        double[][] a = equation1.a;
        double[] b = equation1.b;
        int size = a.length;

        boolean[] used_columns = new boolean[size];
        boolean[] used_raws = new boolean[size];
        for (int step = 0; step < size; ++step) {
            position pivot_element = SelectPivotElement(a, step);
            SwapLines(a, b, used_raws, pivot_element);
            ProcessPivotElement(a, b, pivot_element);
            MarkPivotElementUsed(pivot_element, used_raws, used_columns);
        }

        solution(a,b);

        return b;
    }

    static void PrintColumn(double[] column) {
        for (double v : column) System.out.printf("%.20f\n", v);
    }

    public static void main(String[] args) {
        equation equation1 = ReadEquation();
        double[] solution = SolveEquation(equation1);
        PrintColumn(solution);
    }
}