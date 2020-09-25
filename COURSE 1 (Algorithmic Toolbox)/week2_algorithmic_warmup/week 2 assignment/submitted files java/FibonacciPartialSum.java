import java.util.*;

public class FibonacciPartialSum {

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

    private static long sum(long n){
        n=n%Pisano(10);

        if(n<=1){
            return n;
        }

        long previous = 0;
        long current  = 1;

        for (long i = 0; i < n-1; ++i) {
            long tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%10;
        }
        return current;
    }

    private static long getFibonacciPartialSum(long from, long to) {
        long s=sum(to+2)-1;
        long t=sum(from+1)-1;
        return (s-t+10)%10;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long from = scanner.nextLong();
        long to = scanner.nextLong();
        System.out.println(getFibonacciPartialSum(from, to));
    }
}