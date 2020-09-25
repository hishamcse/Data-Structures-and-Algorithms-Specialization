package com.company;

import java.time.Duration;
import java.time.Instant;
import java.util.*;
import java.io.*;

public class MaxPairwiseProduct {
    static long getMaxPairwiseProduct(int[] numbers) {
        long max_product = 0;
        int n = numbers.length;

        if(n>1) {
            Arrays.sort(numbers);
            max_product = (long)numbers[n - 1] * (long)numbers[n - 2];
        }else{
            max_product=numbers[0];
        }

        return max_product;
    }

    public static void main(String[] args) {

        FastScanner scanner = new FastScanner(System.in);
        int n = scanner.nextInt();
        int[] numbers = new int[n];
        for (int i = 0; i < n; i++) {
            numbers[i] = scanner.nextInt();
        }

        //stress test(very important for debug)

//        Random random=new Random();
//        int n=random.nextInt()%1000 + 2;
//        int[] numbers=new  int[n];
//        for(int i=0;i<n;i++){
//            numbers[i]=random.nextInt()%100000;
//            System.out.print(numbers[i]);
//            System.out.print(" ");
//        }
//        System.out.println();

        //time test
        
        Instant start = Instant.now();
        System.out.println(getMaxPairwiseProduct(numbers));
        Instant end = Instant.now();
        System.out.println("time took "+ Duration.between(start,end).toMillis());
    }

    static class FastScanner {
        BufferedReader br;
        StringTokenizer st;

        FastScanner(InputStream stream) {
            try {
                br = new BufferedReader(new
                        InputStreamReader(stream));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        String next() {
            while (st == null || !st.hasMoreTokens()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }

}
