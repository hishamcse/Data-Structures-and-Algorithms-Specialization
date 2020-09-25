package com.company;

import java.util.Scanner;

public class LCS3 {

    //my first solution(it fails on test 29/29.don't know why.correct solution is uncommented below)

//    private static int lcs3(int[] a, int[] b, int[] c) {
//        Vector<Integer> list=new Vector<>();
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
//                        list.add(a[i-1]);
//                    }else {
//                        d[i][j] = Math.min(insertion, deletion);
//                    }
//                }
//            }
//        }
//        int[] n=new int[list.size()];
//        for(int i=0;i<n.length;i++){
//            n[i]=list.get(i);
//        }
//        int[][] e=new int[c.length+1][n.length+1];
//        for(int j=0;j<=n.length;j++){
//            for(int i=0;i<=c.length;i++){
//                if(i==0){
//                    e[i][j]=j;
//                }
//                else if(j==0){
//                    e[i][j]=i;
//                }
//                else {
//                    int insertion = e[i][j - 1] +1;
//                    int deletion = e[i - 1][j] +1;
//                    int match = e[i - 1][j - 1];
//                    if (c[i-1] == n[j-1]) {
//                        e[i][j] = Math.min(insertion, Math.min(deletion, match));
//                    }else {
//                        e[i][j] = Math.min(insertion, deletion);
//                    }
//                }
//            }
//        }
//        return outputAlignment(c.length,n.length,e,c,n);
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

    //my solution 2(perfect solution.max time:.20/1.50)

    private static int lcs3(int[] a, int[] b, int[] c) {
        int[][][] d = new int[a.length + 1][b.length + 1][c.length + 1];
        for (int k = 0; k <= c.length; k++) {
            for (int j = 0; j <= b.length; j++) {
                for (int i = 0; i <= a.length; i++) {
                    if (i == 0 || j == 0 || k == 0) {
                        d[i][j][k] = 0;
                    } else {
                        int insertion = d[i][j - 1][k];
                        int deletion = d[i - 1][j][k];
                        int extra = d[i][j][k - 1];
                        int match = d[i - 1][j - 1][k - 1];
                        if (a[i - 1] == b[j - 1] && a[i - 1] == c[k - 1]) {
                            d[i][j][k] = 1 + match;
                        } else {
                            d[i][j][k] = Math.max(extra, Math.max(insertion, deletion));
                        }
                    }
                }
            }
        }
        return d[a.length][b.length][c.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int an = scanner.nextInt();
        int[] a = new int[an];
        for (int i = 0; i < an; i++) {
            a[i] = scanner.nextInt();
        }
        int bn = scanner.nextInt();
        int[] b = new int[bn];
        for (int i = 0; i < bn; i++) {
            b[i] = scanner.nextInt();
        }
        int cn = scanner.nextInt();
        int[] c = new int[cn];
        for (int i = 0; i < cn; i++) {
            c[i] = scanner.nextInt();
        }
        System.out.println(lcs3(a, b, c));
    }
}


