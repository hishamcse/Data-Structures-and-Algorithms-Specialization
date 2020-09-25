package com.company;

import java.math.BigInteger;
import java.util.*;

public class LargestNumber {

    static final Comparator<String> COMPARISON_DIGIT=new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            return (o1+o2).compareTo(o2+o1);
        }
    };

    //O(nlogn)
    private static String largestNumber(String[] a) {
        //write your code here
        int n=a.length;

        Arrays.sort(a,COMPARISON_DIGIT);

        String res1 = "";
        String res2 = "";
        for (int i = 0; i < a.length; i++) {
            res1 += a[n-i-1];
            res2 += a[i];
        }
        BigInteger r1=new BigInteger(res1);
        BigInteger r2=new BigInteger(res2);

        if(r1.compareTo(r2)>=0){
            return res1;
        }
        return res2;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        String[] a = new String[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.next();
        }
        System.out.println(largestNumber(a));
    }
}
