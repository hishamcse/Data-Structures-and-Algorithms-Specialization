package com.company;

import java.util.*;

public class FibonacciHuge {
    private static int Pisano(long n){
        long previous = 0;
        long current  = 1;

        for (long i = 0; i < n*n; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%n;
            if(previous==0 && current==1){
                return (int) (i+1);
            }
        }
        return -1;
    }

    private static long getFibonacciHuge(long n, long m) {
        n=n%Pisano(m);

        if(n<=1){
            return n;
        }

        long previous = 0;
        long current  = 1;

        for (long i = 0; i < n-1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%m;
        }
        return current%m;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long m = scanner.nextLong();
        System.out.println(getFibonacciHuge(n, m));
    }
}
