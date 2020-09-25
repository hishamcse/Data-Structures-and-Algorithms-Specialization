package com.company;

import java.util.Scanner;

public class ChangeDP {

    public static int dpChange(int[] options, int total) {
        int[] minNumOfOp = new int[total + 1];
        minNumOfOp[0] = 0;
        int num;
        for (int i = 1; i <= total; i++) {
            minNumOfOp[i] = Integer.MAX_VALUE;
            for (int j = 0; j < options.length; j++) {
                if (i >= options[j]) {
                    num = minNumOfOp[i - options[j]] + 1;
                    if (num < minNumOfOp[i]) {
                        minNumOfOp[i] = num;
                    }
                }
            }
        }
        return minNumOfOp[total];
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println(dpChange(new int[]{1, 3, 4}, scanner.nextInt()));
    }
}


