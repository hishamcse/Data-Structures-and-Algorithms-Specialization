package com.Hisham;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class PuzzleAssembly {

    private static final int TOTAL_SQUARES = 25;

    private static Square leftUp, leftDown, rightUp, rightDown;
    private static List<Square> upSquares, leftSquares, downSquares, rightSquares, midSquares;

    static class Square implements Comparable<Square> {
        int id;
        String up;
        String left;
        String down;
        String right;

        public Square(int id, String up, String left, String down, String right) {
            this.id = id;
            this.up = up;
            this.left = left;
            this.down = down;
            this.right = right;
        }

        @Override
        public int compareTo(Square o) {
            return Integer.compare(this.id, o.id);
        }

        @Override
        public String toString() {
            return "(" + up + ',' + left + ',' + down + ',' + right + ")";
        }
    }

    private static void initializeSquares() {
        leftUp = null;
        leftDown = null;
        rightUp = null;
        rightDown = null;
        upSquares = new ArrayList<>();
        leftSquares = new ArrayList<>();
        downSquares = new ArrayList<>();
        rightSquares = new ArrayList<>();
        midSquares = new ArrayList<>();
    }

    // taken from geeksforgeeks - algorithm to find next permutation :
    // 1. Find the longest non-increasing suffix and find the pivot.
    // 2. If the suffix is the whole array, then there is no higher order permutation for the data.
    // 3. Find the rightmost successor to the pivot.
    // 4. Swap the successor and the pivot.
    // 5. Reverse the suffix.

    static class NextPermutation {

        public static void swap(List<Square> data, int left, int right) {
            Square temp = data.get(left);
            data.set(left, data.get(right));
            data.set(right, temp);
        }

        public static void reverse(List<Square> data, int left, int right) {
            while (left < right) {
                Square temp = data.get(left);
                data.set(left++, data.get(right));
                data.set(right--, temp);
            }
        }

        public static void findNextPermutation(List<Square> data) {

            if (data.size() <= 1) {   // next_permutation is not possible for the size <=1
                return;
            }

            int last = data.size() - 2;
            while (last >= 0) {       // find the longest non-increasing suffix and find the pivot
                if (data.get(last).compareTo(data.get(last + 1)) < 0) {
                    break;
                }
                last--;
            }
            if (last < 0) {        // If there is no increasing pair there is no higher order permutation
                return;
            }

            int nextGreater = data.size() - 1;
            for (int i = data.size() - 1; i > last; i--) {     // Find the rightmost successor to the pivot
                if (data.get(i).compareTo(data.get(last)) > 0) {
                    nextGreater = i;
                    break;
                }
            }

            swap(data, nextGreater, last);// Swap the successor and the pivot
            reverse(data, last + 1, data.size() - 1);// Reverse the suffix
        }
    }

    private static void assignSquare(Square square) {
        if (square.up.equals("black") && square.left.equals("black")) {
            leftUp = square;
        } else if (square.left.equals("black") && square.down.equals("black")) {
            leftDown = square;
        } else if (square.up.equals("black") && square.right.equals("black")) {
            rightUp = square;
        } else if (square.down.equals("black") && square.right.equals("black")) {
            rightDown = square;
        } else if (square.up.equals("black")) {
            upSquares.add(square);
        } else if (square.left.equals("black")) {
            leftSquares.add(square);
        } else if (square.down.equals("black")) {
            downSquares.add(square);
        } else if (square.right.equals("black")) {
            rightSquares.add(square);
        } else {
            midSquares.add(square);
        }
    }

    private static boolean is_Middle_Permuted() {
        for (int i = 0; i < upSquares.size(); i++) {
            if (!upSquares.get(i).down.equals(midSquares.get(i).up)) {
                return false;
            }
        }
        for (int i = 0; i < leftSquares.size(); i++) {
            if (!leftSquares.get(i).right.equals(midSquares.get(i * leftSquares.size()).left)) {
                return false;
            }
        }
        for (int i = 0; i < downSquares.size(); i++) {
            if (!downSquares.get(downSquares.size() - i - 1).up.equals(midSquares.get(midSquares.size() - i - 1).down)) {
                return false;
            }
        }
        for (int i = 0; i < rightSquares.size(); i++) {
            if (!rightSquares.get(i).left.equals(midSquares.get(i * rightSquares.size() + (rightSquares.size() - 1)).right)) {
                return false;
            }
        }
        return true;
    }

    private static void find_Result_Permutation_Puzzle() {
        while (!leftUp.right.equals(upSquares.get(0).left) || !upSquares.get(0).right.equals(upSquares.get(1).left)
                || !upSquares.get(2).right.equals(rightUp.left)) {
            NextPermutation.findNextPermutation(upSquares);
        }
        while (!leftDown.right.equals(downSquares.get(0).left) || !downSquares.get(0).right.equals(downSquares.get(1).left)
                || !downSquares.get(2).right.equals(rightDown.left)) {
            NextPermutation.findNextPermutation(downSquares);
        }
        while (!leftUp.down.equals(leftSquares.get(0).up) || !leftSquares.get(0).down.equals(leftSquares.get(1).up)
                || !leftSquares.get(2).down.equals(leftDown.up)) {
            NextPermutation.findNextPermutation(leftSquares);
        }
        while (!rightUp.down.equals(rightSquares.get(0).up) || !rightSquares.get(0).down.equals(rightSquares.get(1).up)
                || !rightSquares.get(2).down.equals(rightDown.up)) {
            NextPermutation.findNextPermutation(rightSquares);
        }
        while (!is_Middle_Permuted()) {
            NextPermutation.findNextPermutation(midSquares);
        }
    }

    private static void print_Result() {
        StringBuilder sb = new StringBuilder();

        // first row
        sb.append(leftUp.toString()).append(";");
        for (Square upSq : upSquares) {
            sb.append(upSq.toString()).append(";");
        }
        sb.append(rightUp.toString()).append("\n");

        // second to fourth row
        for (int i = 0; i < 3; i++) {
            sb.append(leftSquares.get(i).toString()).append(";");
            for (int j = 0; j < 3; j++) {
                sb.append(midSquares.get(i * 3 + j).toString()).append(";");
            }
            sb.append(rightSquares.get(i).toString()).append("\n");
        }

        // last row
        sb.append(leftDown.toString()).append(";");
        for (Square downSq : downSquares) {
            sb.append(downSq.toString()).append(";");
        }
        sb.append(rightDown.toString());

        System.out.println(sb.toString());
    }

    public static void main(String[] args) throws IOException {
        FastScanner scanner = new FastScanner();
        String[] squareStrings = new String[TOTAL_SQUARES];
        int i = 0;
        while (i < TOTAL_SQUARES) {
            squareStrings[i] = scanner.next();
            i++;
        }

        initializeSquares();
        Square[] squares = new Square[TOTAL_SQUARES];
        for (i = 0; i < TOTAL_SQUARES; i++) {
            String[] splits = squareStrings[i].split(",");
            String up = splits[0].substring(1);
            String left = splits[1];
            String down = splits[2];
            String right = splits[3].substring(0, splits[3].length() - 1);

            squares[i] = new Square(i, up, left, down, right);
            assignSquare(squares[i]);
        }

        find_Result_Permutation_Puzzle();
        print_Result();
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