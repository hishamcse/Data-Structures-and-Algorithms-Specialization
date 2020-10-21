import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class PhiX174ErrorProne {

    private static final int TOTAL_ERROR_PRONE_READ = 1618;
    private static final int MER_SIZE = 45;
    private static final int MAX_ERROR = 2;

    static class Vertex implements Comparable<Vertex> {
        int id;
        StringBuilder read;
        int weight;

        public Vertex(int id, StringBuilder read, int weight) {
            this.id = id;
            this.read = read;
            this.weight = weight;
        }

        @Override
        public int compareTo(Vertex o) {
            return Integer.compare(o.weight, this.weight);
        }
    }

    private static int overLapLength(String s1, String s2) {
        for (int i = 0, n = 1 + s1.length() - MER_SIZE; i < n; ++i) {
            int errors = 0;
            for (int j = 0, s = s1.length() - i; j < s && errors <= MAX_ERROR; ++j) {
                if (s1.charAt(i + j) != s2.charAt(j)) ++errors;
            }

            if (errors <= MAX_ERROR) {
                return s1.length() - i;
            }
        }
        return 0;
    }

    private static char get_most_freq_char(char[] list) {
        Map<Character, Integer> counts = new TreeMap<>();

        for (Character c : list) {
            if (counts.containsKey(c)) {
                int i = counts.get(c);
                counts.put(c, i + 1);
            } else {
                counts.put(c, 1);
            }
        }

        int max = counts.get(list[0]);
        char max_char = list[0];

        for (Character c : list) {
            if (counts.get(c) > max) {
                max = counts.get(c);
                max_char = c;
            }
        }
        return max_char;
    }

    public static String assemble_genome_error_prone(Vertex[] vertices) {

        Vector<Vertex> allVertex = new Vector<>(Arrays.asList(vertices));

        StringBuilder genome = new StringBuilder();
        genome.append(vertices[0].read);

        String first_read = vertices[0].read.toString(), cur_read;
        int cur_id = 0;

        while (allVertex.size() > 1) {
            cur_read = allVertex.remove(cur_id).read.toString();

            Vector<Integer> overlaps = new Vector<>();
            Vector<Integer> positions = new Vector<>();

            for (int j = 0; j < allVertex.size(); ++j) {
                int overlap = overLapLength(cur_read, allVertex.elementAt(j).read.toString());

                if (overlaps.isEmpty() || overlap >= overlaps.lastElement()) {
                    overlaps.add(overlap);
                    positions.add(j);
                    cur_id = j;
                }
            }

            if (overlaps.size() > 3) {

                int suffixPos = genome.length() - overlaps.get(overlaps.size() - 4);
                int prefix1Pos = 0;
                int prefix2Pos = overlaps.get(overlaps.size() - 3) - overlaps.get(overlaps.size() - 4);
                int prefix3Pos = overlaps.get(overlaps.size() - 2) - overlaps.get(overlaps.size() - 4);
                int prefix4Pos = overlaps.get(overlaps.size() - 1) - overlaps.get(overlaps.size() - 4);

                char suffix, prefix1, prefix2, prefix3, prefix4;

                for (int i = 0, n = overlaps.get(overlaps.size() - 4); i < n; ++i,
                        ++suffixPos, ++prefix1Pos, ++prefix2Pos, ++prefix3Pos, ++prefix4Pos) {
                    suffix = genome.charAt(suffixPos);
                    prefix1 = allVertex.get(positions.get(positions.size() - 4)).read.charAt(prefix1Pos);
                    prefix2 = allVertex.get(positions.get(positions.size() - 3)).read.charAt(prefix2Pos);
                    prefix3 = allVertex.get(positions.get(positions.size() - 2)).read.charAt(prefix3Pos);
                    prefix4 = allVertex.get(positions.get(positions.size() - 1)).read.charAt(prefix4Pos);

                    if (suffix == prefix1 && prefix1 == prefix2 &&
                            prefix2 == prefix3 && prefix3 == prefix4) {
                        continue;
                    }

                    char c = get_most_freq_char(new char[]{suffix, prefix1, prefix2, prefix3, prefix4});
                    genome.setCharAt(suffixPos, c);
                    allVertex.get(positions.get(positions.size() - 4)).read.setCharAt(prefix1Pos, c);
                    allVertex.get(positions.get(positions.size() - 3)).read.setCharAt(prefix2Pos, c);
                    allVertex.get(positions.get(positions.size() - 2)).read.setCharAt(prefix3Pos, c);
                    allVertex.get(positions.get(positions.size() - 1)).read.setCharAt(prefix4Pos, c);
                }
            }
            genome.append(allVertex.get(cur_id).read.substring(overlaps.lastElement()));
        }
        genome.delete(0, overLapLength(allVertex.get(0).read.toString(), first_read));
        return genome.toString();
    }

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        Vertex[] vertices = new Vertex[TOTAL_ERROR_PRONE_READ];
        for (int i = 0; i < TOTAL_ERROR_PRONE_READ; i++) {
            vertices[i] = new Vertex(i, new StringBuilder(scanner.next()), 0);
        }
        Collections.shuffle(Arrays.asList(vertices));

        System.out.println(assemble_genome_error_prone(vertices));
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
    }
}