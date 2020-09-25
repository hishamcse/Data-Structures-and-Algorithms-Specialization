package com.company;

import java.util.*;

public class FibonacciLastDigit {
    private static int getFibonacciLastDigit(int n) {
        int []f=new int[n+1];
        f[0]=0;
        f[1]=1;

        for(int i=2;i<=n;i++){
            f[i]=(f[i-1]+f[i-2])%10;
        }

        return f[n];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int c = getFibonacciLastDigit(n);
        System.out.println(c);
    }
}