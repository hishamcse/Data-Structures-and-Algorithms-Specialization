package com.company;

import java.util.Scanner;

public class Partition3 {

    //approach 1(though it passes the autograder.It is not the correct solution.
    // recommended approach is approach 3.see below)

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
        if (val[total][weight.length] == total) {
            return 1;
        }
        return 0;
    }

    //approach 2
    private static boolean isSubsetSum(int[] A,int n,int sum){
        if(sum==0){
            return true;
        }
        if(n==0 && sum!=0){
            return false;
        }
        if(A[n-1]>sum){
            return isSubsetSum(A,n-1,sum);
        }
        return isSubsetSum(A,n-1,sum) || isSubsetSum(A,n-1,sum-A[n-1]);
    }

    //approach 3(recommended)
    //This is a standard multi dimensional knapsack.
    //Let dp[i][j][k]=1 mean that we can distribute the first i objects, and give j value to person 1,
    // and k value to person 2. We don’t need the amount needed by person 3, as it is equal to the sum of
    // the first i objects, -j-k
    //dp[i][j][k] will be true if
    //dp[i-1][j][k] is true(Giving this object to person 3)
    //dp[i-1][j-value[i]][k](Giving this object to person 1)
    //dp[i-1][j][k-value[i]](Giving this object to person 2)

    private static int approach3(int[] A,int sum){
        int m=sum/3;
        int [][][]dp=new int[A.length+1][m+1][m+1];
        dp[0][0][0]=1;
        for(int i=0;i<A.length;i++){
            for(int j=0;j<=m;j++){
                for(int k=0;k<=m;k++){
                    dp[i+1][j][k]=dp[i][j][k];
                    if(j>=A[i]){
                        dp[i+1][j][k]=dp[i+1][j][k] | dp[i][j-A[i]][k];
                    }
                    if(k>=A[i]){
                        dp[i+1][j][k]=dp[i+1][j][k] | dp[i][j][k-A[i]];
                    }
                }
            }
        }
        return dp[A.length][m][m];
    }

    private static int partition3(int[] A) {
        int sum = 0;
        for (int value : A) {
            sum += value;
        }
        if (sum % 3 == 0) {
//            return knapsackWithoutRepeat(A, sum / 3);
            return approach3(A,sum);
//            if(isSubsetSum(A,A.length,sum/3)){
//                return 1;
//            }
        }
        return 0;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] A = new int[n];
        for (int i = 0; i < n; i++) {
            A[i] = scanner.nextInt();
        }
        System.out.println(partition3(A));
    }
}


