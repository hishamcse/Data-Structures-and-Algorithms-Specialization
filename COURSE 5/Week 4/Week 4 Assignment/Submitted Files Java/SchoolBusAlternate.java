import java.io.*;
import java.util.*;

public class SchoolBusAlternate {

    private static FastScanner in;
    private static final int INF = 1000 * 1000 * 1000;

    public static void main(String[] args) {
        in = new FastScanner();
        try {
            printAnswer(SchoolBus(readData()));
        } catch (IOException exception) {
            System.err.print("Error during reading: " + exception.toString());
        }
    }

    private static void helper(List<int[]> combinations, int[] data, int start, int end, int index) {
        if (index == data.length) {
            int[] combination = data.clone();
            combinations.add(combination);
        } else if (start <= end) {
            data[index] = start;
            helper(combinations, data, start + 1, end, index + 1);
            helper(combinations, data, start + 1, end, index);
        }
    }

    public static List<int[]> generate(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        helper(combinations, new int[r + 1], 1, n - 1, 1);
        return combinations;
    }

    static class track {
        int id1;
        int id2;

        public track(int id1, int id2) {
            this.id1 = id1;
            this.id2 = id2;
        }
    }

    static Answer SchoolBus(int[][] graph) {
        int n = graph.length;
        int[][] C = new int[1 << n][n];
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = INF;
            }
        }
        track[][] backtrack = new track[1 << n][n];
        for (int i = 0; i < (1 << n); i++) {
            for (int j = 0; j < n; j++) {
                backtrack[i][j] = new track(-1, -1);
            }
        }
        C[1][0] = 0;

        int current, index1, index2 = 0, min_cost = INF;

        for (int size = 1; size < n; size++) {
            List<int[]> combinations = generate(n, size);
            for (int[] S : combinations) {
                int k = 0;
                for (int value : S) {
                    k += (1 << value);
                }
                for (int i : S) {
                    if (i == 0) continue;
                    for (int j : S) {
                        if (j == i) continue;
                        current = C[k ^ (1 << i)][j] + graph[i][j];
                        if (current < C[k][i]) {
                            C[k][i] = current;
                            backtrack[k][i] = new track(k ^ (1 << i), j);
                        }
                    }
                }
            }
        }
        for (int i = 0; i < n; i++) {
            if ((C[(1 << n) - 1][i] + graph[0][i] < min_cost)) {
                min_cost = C[(1 << n) - 1][i] + graph[0][i];
                index2 = i;
            }
        }
        if (min_cost >= INF) {
            return new Answer(-1, null);
        }

        List<Integer> min_path = new ArrayList<>();
        index1 = (1 << n) - 1;
        while (-1 != index1) {
            min_path.add(index2 + 1);
            track tr = backtrack[index1][index2];
            index1 = tr.id1;
            index2 = tr.id2;
        }

        return new Answer(min_cost, min_path);
    }

    private static int[][] readData() throws IOException {
        int n = in.nextInt();
        int m = in.nextInt();
        int[][] graph = new int[n][n];

        for (int i = 0; i < n; ++i)
            for (int j = 0; j < n; ++j)
                graph[i][j] = INF;

        for (int i = 0; i < m; ++i) {
            int u = in.nextInt() - 1;
            int v = in.nextInt() - 1;
            int weight = in.nextInt();
            graph[u][v] = graph[v][u] = weight;
        }
        return graph;
    }

    private static void printAnswer(final Answer answer) {
        System.out.println(answer.weight);
        if (answer.weight == -1)
            return;
        for (int x : answer.path)
            System.out.print(x + " ");
        System.out.println();
    }

    static class Answer {
        int weight;
        List<Integer> path;

        public Answer(int weight, List<Integer> path) {
            this.weight = weight;
            this.path = path;
        }
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