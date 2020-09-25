package com.company;

import java.util.*;

public class CoveringSegments {

    static final Comparator<Segment> COMPARISON_SEGMENTS= new Comparator<>() {
        @Override
        public int compare(Segment o1, Segment o2) {
            return Long.compare(o1.end, o2.end);
        }
    };

    //o(nlogn)
    private static long[] optimalPoints(Segment[] segments) {

        Arrays.sort(segments,COMPARISON_SEGMENTS);

        List<Long> result=new LinkedList<>();
        int i=0;
        result.add(segments[0].end);
        long p=segments[0].end;

        for (i = 1; i < segments.length; i++) {

            if(segments[i].start>p){
                p=segments[i].end;
                result.add(p);
            }
        }

        long[] points=new long[result.size()];

        for(i=0;i<result.size();i++){
            points[i]=result.get(i);
        }

        return points;
    }

    private static class Segment {
        long start, end;

        Segment(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        Segment[] segments = new Segment[n];
        for (int i = 0; i < n; i++) {
            long start, end;
            start = scanner.nextLong();
            end = scanner.nextLong();
            segments[i] = new Segment(start, end);
        }
        long[] points = optimalPoints(segments);
        System.out.println(points.length);
        for (long point : points) {
            System.out.print(point + " ");
        }
    }
}
