package com.company;

import java.util.Scanner;

public class LCS2 {

    //approach 1(perfect).simpler solution uncommented below
//    private static int lcs2(int[] a, int[] b) {
//        int[][] d=new int[a.length+1][b.length+1];
//        for(int j=0;j<=b.length;j++){
//            for(int i=0;i<=a.length;i++){
//                if(i==0){
//                    d[i][j]=j;
//                }
//                else if(j==0){
//                    d[i][j]=i;
//                }
//                else {
//                    int insertion = d[i][j - 1] +1;
//                    int deletion = d[i - 1][j] +1;
//                    int match = d[i - 1][j - 1];
//                    if (a[i-1] == b[j-1]) {
//                        d[i][j] = Math.min(insertion, Math.min(deletion, match));
//                    }else {
//                        d[i][j] = Math.min(insertion, deletion);
//                    }
//                }
//            }
//        }
//        return outputAlignment(a.length,b.length,d,a,b);
//    }
//
//    private static int s=0;
//    public static int outputAlignment(int i, int j, int[][] d, int[] a, int[] b){
//        if(i==0 && j==0){
//            return 0;
//        }
//        if(i>0 && d[i][j]==d[i-1][j]+1){
//            outputAlignment(i-1,j,d,a,b);
//        }else if(j>0 && d[i][j]==d[i][j-1]+1){
//            outputAlignment(i,j-1,d,a,b);
//        }else{
//            if(a[i-1]==b[j-1]){
//                s++;
//            }
//            outputAlignment(i-1,j-1,d,a,b);
//        }
//        return s;
//    }

    //approach 2(perfect & simple)
    private static int lcs2(int[] a, int[] b) {
        int[][] d = new int[a.length + 1][b.length + 1];
        for (int j = 0; j <= b.length; j++) {
            for (int i = 0; i <= a.length; i++) {
                if (i == 0) {
                    d[i][j] = 0;
                } else if (j == 0) {
                    d[i][j] = 0;
                } else {
                    int insertion = d[i][j - 1];
                    int deletion = d[i - 1][j];
                    int match = d[i - 1][j - 1];
                    if (a[i - 1] == b[j - 1]) {
                        d[i][j] = 1 + match;
                    } else {
                        d[i][j] = Math.max(insertion, deletion);
                    }
                }
            }
        }
        return d[a.length][b.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }
        System.out.println(lcs2(a, b));
    }
}
