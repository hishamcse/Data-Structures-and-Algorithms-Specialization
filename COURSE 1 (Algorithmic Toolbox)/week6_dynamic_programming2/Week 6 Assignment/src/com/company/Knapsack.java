package com.company;

import java.util.Scanner;

public class Knapsack {

    public static int knapsackWithoutRepeat(int[] weight, int total) {
        int[][] val = new int[total + 1][weight.length + 1];
        for (int i = 0; i <= weight.length; i++) {
            for (int w = 0; w <= total; w++) {
                if (i == 0 || w == 0) {
                    val[w][i] = 0;
                } else {
                    val[w][i] = val[w][i - 1];
                    if (weight[i - 1] <= w) {
                        int value = val[w - weight[i - 1]][i - 1] + weight[i - 1];
                        if (value > val[w][i]) {
                            val[w][i] = value;
                        }
                    }
                }
            }
        }
        return val[total][weight.length];
    }

//    static int optimalWeight(int W, int[] w) {
//        //write you code here
//        int result = 0;
//        for (int i = 0; i < w.length; i++) {
//            if (result + w[i] <= W) {
//                result += w[i];
//            }
//        }
//        return result;
//    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int W, n;
        W = scanner.nextInt();
        n = scanner.nextInt();
        int[] w = new int[n];
        for (int i = 0; i < n; i++) {
            w[i] = scanner.nextInt();
        }
        System.out.println(knapsackWithoutRepeat(w, W));
    }
}


