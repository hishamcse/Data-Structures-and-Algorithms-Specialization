package com.company;

import java.util.*;

public class FibonacciSumLastDigit {
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

    private static long getFibonacciSum(long n) {
        n=n%Pisano(10);
        if (n <= 1)
            return n;

        long previous = 0;
        long current  = 1;
        long sum      = 1;

        for (long i = 0; i < n - 1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%10;
            sum += current;
        }

        return sum % 10;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        long s = getFibonacciSum(n);
        System.out.println(s);
    }
}