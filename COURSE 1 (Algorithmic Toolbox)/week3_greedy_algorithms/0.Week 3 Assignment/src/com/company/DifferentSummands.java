package com.company;

import java.util.*;

public class DifferentSummands {

    //O(n).should use hashset.because arraylist will fail time limit
    private static Set<Integer> optimalSummands(long n) {
        Set<Integer> summands = new HashSet<>();

        if(n==0){
            return summands;
        }

        for(int i=1;;i++){

            if(!summands.contains((int)n-i)  && (n-i)!=i){
                summands.add(i);
                n-=i;
                if(n<=i){
                    break;
                }
            }
        }
        return summands;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long n = scanner.nextLong();
        Set<Integer> summands = optimalSummands(n);
        System.out.println(summands.size());
        for (Integer summand : summands) {
            System.out.print(summand + " ");
        }
    }
}

