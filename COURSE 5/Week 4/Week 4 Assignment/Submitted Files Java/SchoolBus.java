import java.io.*;
import java.util.*;

// this implementation taken from this youtube channel video : https://www.youtube.com/c/WilliamFiset-videos/videos

public class SchoolBus {
    private static FastScanner in;
    private static final int INF = 1000 * 1000 * 1000 ;

    public static void main(String[] args) {
        in = new FastScanner();
        try {
            TspDynamicProgrammingRecursive tsp=new TspDynamicProgrammingRecursive(readData());
            printAnswer(new Answer(tsp.getTourCost(),tsp.getTour()));
        } catch (IOException exception) {
            System.err.print("Error during reading: " + exception.toString());
        }
    }

    public static class TspDynamicProgrammingRecursive {

        private final int N;
        private final int START_NODE;
        private final int FINISHED_STATE;

        private final int[][] distance;
        private int minTourCost = Integer.MAX_VALUE;

        private List<Integer> tour = new ArrayList<>();
        private boolean ranSolver = false;

        public TspDynamicProgrammingRecursive(int[][] distance) {
            this(0, distance);
        }

        public TspDynamicProgrammingRecursive(int startNode, int[][] distance) {

            this.distance = distance;
            N = distance.length;
            START_NODE = startNode;

//            // Validate inputs.
//            if (N <= 0) throw new IllegalStateException("TSP on 0, 1 or 2 nodes doesn't make sense.");
//            if (N != distance[0].length)
//                throw new IllegalArgumentException("Matrix must be square (N x N)");
//            if (START_NODE < 0 || START_NODE >= N)
//                throw new IllegalArgumentException("Starting node must be: 0 <= startNode < N");
//            if (N > 32)
//                throw new IllegalArgumentException(
//                        "Matrix too large! A matrix that size for the DP TSP problem with a time complexity of"
//                                + "O(n^2*2^n) requires way too much computation for any modern home computer to handle");

            // The finished state is when the finished state mask has all bits are set to
            // one (meaning all the nodes have been visited).
            FINISHED_STATE = (1 << N) - 1;
        }

        // Returns the optimal tour for the traveling salesman problem.
        public List<Integer> getTour() {
            if (!ranSolver) solve();
            return tour;
        }

        // Returns the minimal tour cost.
        public int getTourCost() {
            if (!ranSolver) solve();
            return minTourCost;
        }

        public void solve() {

            // Run the solver
            int state = 1 << START_NODE;
            Integer[][] memo = new Integer[N][1 << N];
            Integer[][] prev = new Integer[N][1 << N];
            minTourCost = tsp(START_NODE, state, memo, prev);

            // Regenerate path
            int index = START_NODE;
            while (true) {
                if(index==-1){
                    tour=null;
                    minTourCost=-1;
                    break;
                }
                tour.add(index+1);
                Integer nextIndex = prev[index][state];
                if (nextIndex == null) break;
                state = state | (1 << nextIndex);
                index = nextIndex;
            }
            ranSolver = true;
        }

        private int tsp(int i, int state, Integer[][] memo, Integer[][] prev) {

            // Done this tour. Return cost of going back to start node.
            if (state == FINISHED_STATE) return distance[i][START_NODE];

            // Return cached answer if already computed.
            if (memo[i][state] != null) return memo[i][state];

            int minCost = INF;
            int index = -1;
            for (int next = 0; next < N; next++) {

                // Skip if the next node has already been visited.
                if ((state & (1 << next)) != 0) continue;

                int nextState = state | (1 << next);
                int newCost = distance[i][next] + tsp(next, nextState, memo, prev);
                if (newCost < minCost) {
                    minCost = newCost;
                    index = next;
                }
            }

            prev[i][state] = index;
            return memo[i][state] = minCost;
        }

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