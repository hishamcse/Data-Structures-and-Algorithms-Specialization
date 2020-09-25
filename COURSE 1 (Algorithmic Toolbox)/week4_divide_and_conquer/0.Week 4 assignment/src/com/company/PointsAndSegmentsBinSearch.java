package com.company;

import java.util.Arrays;
import java.util.Scanner;

public class PointsAndSegmentsBinSearch {

    //my another solution;significantly faster than my first solution (time:1.50/6.00)

    private static int binarysearchforleft(int[] a,int low,int high,int key) {
        int count=0;

        while (low<=high){
            int mid=low+(high-low)/2;
            if(a[mid]==key){
                count+=(mid-low+1);
                if(mid+1<a.length && a[mid]==a[mid+1]){
                    low=mid+1;
                }else {
                    break;
                }
            } else if(a[mid]>key){
                high=mid-1;
            }else{
                count+=(mid-low+1);
                low=mid+1;
            }
        }
        return count;
    }

    private static int binarysearchforright(int[] a,int low,int high,int key) {
        int count=0;

        while (low<=high){
            int mid=low+(high-low)/2;
            if(a[mid]==key){
                count+=(high-mid+1);
                if(mid-1>=0 && a[mid]==a[mid-1]){
                    high=mid-1;
                }else {
                    break;
                }
            } else if(a[mid]>key){
                count+=(high-mid+1);
                high=mid-1;
            }else{
                low=mid+1;
            }
        }
        return count;
    }

    private static int[] fastCountSegments(int[] starts, int[] ends, int[] points) {
        int[] cnt = new int[points.length];
        int n=starts.length;
        Arrays.sort(starts);
        Arrays.sort(ends);
        for(int i=0;i<points.length;i++){
            int l=binarysearchforleft(starts,0,starts.length-1,points[i]);
            int r=binarysearchforright(ends,0,ends.length-1,points[i]);
            cnt[i]=l+r-n;
        }
        return cnt;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n, m;
        n = scanner.nextInt();
        m = scanner.nextInt();
        int[] starts = new int[n];
        int[] ends = new int[n];
        int[] points = new int[m];
        for (int i = 0; i < n; i++) {
            starts[i] = scanner.nextInt();
            ends[i] = scanner.nextInt();
        }
        for (int i = 0; i < m; i++) {
            points[i] = scanner.nextInt();
        }
        //use fastCountSegments
        int[] cnt = fastCountSegments(starts, ends, points);
        for (int x : cnt) {
            System.out.print(x + " ");
        }
    }
}
