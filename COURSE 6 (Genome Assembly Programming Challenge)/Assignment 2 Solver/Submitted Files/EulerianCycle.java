import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class EulerianCycle {

    private Stack<Integer> cycle = null;
    private final int E;

    public EulerianCycle(ArrayList<Integer>[] adjIn,ArrayList<Integer>[] adjOut,int m) {

        E = m;
        for (int v = 0; v < adjOut.length; v++) {
            if (adjIn[v].size() != adjOut[v].size()) {
                return;
            }
        }

        Iterator<Integer>[] adj = (Iterator<Integer>[]) new Iterator[adjOut.length];
        for (int v = 0; v < adjOut.length; v++) {
            adj[v] = adjOut[v].iterator();
        }

        createCycle(adjIn, adjOut, adj);
    }


    private void createCycle(ArrayList<Integer>[] adjIn, ArrayList<Integer>[] adjOut, Iterator<Integer>[] adj) {
        int s = nonIsolatedVertex(adjOut);
        Stack<Integer> stack = new Stack<>();
        stack.push(s);

        cycle = new Stack<>();
        while (!stack.isEmpty()) {
            int v = stack.pop();
            while (adj[v].hasNext()) {
                stack.push(v);
                v = adj[v].next();
            }
            cycle.push(v);
        }

        if (cycle.size() != E + 1) {
            cycle = null;
        }
    }

    private int nonIsolatedVertex(ArrayList<Integer>[] adjOut) {
        for (int v = 0; v < adjOut.length; v++) {
            if (adjOut[v].size() > 0) {
                return v;
            }
        }
        return -1;
    }

    public boolean hasEulerianCycle() {
        return cycle != null;
    }

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        int n = scanner.nextInt();
        int m = scanner.nextInt();
        ArrayList<Integer>[] adjIn = (ArrayList<Integer>[]) new ArrayList[n];
        ArrayList<Integer>[] adjOut = (ArrayList<Integer>[]) new ArrayList[n];
        for (int i = 0; i < n; i++) {
            adjIn[i] = new ArrayList<Integer>();
            adjOut[i] = new ArrayList<Integer>();
        }
        for (int i = 0; i < m; i++) {
            int x, y;
            x = scanner.nextInt();
            y = scanner.nextInt();
            adjOut[x - 1].add(y - 1);
            adjIn[y - 1].add(x - 1);
        }
        EulerianCycle eulerianCycle = new EulerianCycle(adjIn, adjOut, m);
        if (!eulerianCycle.hasEulerianCycle()) {
            System.out.println(0);
        } else {
            System.out.println(1);
            for (int i = eulerianCycle.cycle.size()-2; i >= 0; i--) {
                System.out.print(eulerianCycle.cycle.get(i) + 1 + " ");
            }
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