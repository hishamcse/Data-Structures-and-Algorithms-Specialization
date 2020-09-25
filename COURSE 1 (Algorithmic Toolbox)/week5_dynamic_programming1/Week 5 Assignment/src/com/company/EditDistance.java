package com.company;

import java.util.Scanner;

public class EditDistance {

    public static int editDist(String a, String b) {
        char[] A = a.toCharArray();
        char[] B = b.toCharArray();
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int j = 0; j <= b.length(); j++) {
            for (int i = 0; i <= a.length(); i++) {
                if (i == 0) {
                    d[i][j] = j;
                } else if (j == 0) {
                    d[i][j] = i;
                } else {
                    int insertion = d[i][j - 1] + 1;
                    int deletion = d[i - 1][j] + 1;
                    int mismatch = d[i - 1][j - 1] + 1;
                    int match = d[i - 1][j - 1];
                    if (A[i - 1] == B[j - 1]) {
                        d[i][j] = Math.min(insertion, Math.min(deletion, match));
                    } else {
                        d[i][j] = Math.min(insertion, Math.min(deletion, mismatch));
                    }
                }
            }
        }
        return d[a.length()][b.length()];
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        String s = scan.next();
        String t = scan.next();

        System.out.println(editDist(s, t));
    }
}
