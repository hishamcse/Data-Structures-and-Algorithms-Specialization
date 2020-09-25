package com.company;

import java.util.Scanner;

public class Fibonacci {
    private static long calc_fib(int n) {
        long []f=new long[n+1];
        f[0]=0;
        f[1]=1;

        for(int i=2;i<=n;i++){
            f[i]=f[i-1]+f[i-2];
        }

        return f[n];
    }

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        int n = in.nextInt();

        if(n==0 || n==1){
            System.out.println(n);
        }else {
            System.out.println(calc_fib(n));
        }
    }
}
