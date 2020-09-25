package com.company;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class PrimitiveCalculator {

    private static List<Integer> optimal_sequence(int n) {
        List<Integer> sequence = new ArrayList<>();
        int[] minNumOfOp = new int[n + 1];
        minNumOfOp[0] = 0;
        minNumOfOp[1] = 0;
        int num1, num2, num3;
        for (int i = 2; i <= n; i++) {
            num1 = minNumOfOp[i - 1] + 1;
            num2 = Integer.MAX_VALUE;
            num3 = Integer.MAX_VALUE;
            if (i % 2 == 0) {
                num2 = minNumOfOp[i / 2] + 1;
            }
            if (i % 3 == 0) {
                num3 = minNumOfOp[i / 3] + 1;
            }
            minNumOfOp[i] = Math.min(Math.min(num1, num2), num3);
        }

        int i = n;
        while (i >= 1) {
            sequence.add(i);
            if (i % 2 == 0 && minNumOfOp[i] == minNumOfOp[i / 2] + 1) {
                i /= 2;
            } else if (i % 3 == 0 && minNumOfOp[i] == minNumOfOp[i / 3] + 1) {
                i /= 3;
            } else {
                i -= 1;
            }
        }
        Collections.reverse(sequence);
        return sequence;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        List<Integer> sequence = optimal_sequence(n);
        System.out.println(sequence.size() - 1);
        for (Integer x : sequence) {
            System.out.print(x + " ");
        }
    }

}
