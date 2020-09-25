#include <iostream>
#include <vector>
using std::vector;

long long Pisano(long long n){
        long long previous = 0;
        long long current  = 1;

        for (long long i = 0; i < n*n; ++i) {
            long long tmp_previous = previous;
            previous = current;
            current = (tmp_previous + current)%n;
            if(previous==0 && current==1){
                return (i+1);
            }
        }
        return -1;
    }

int sum(long long n) {
    n=n%Pisano(10);

    if (n <= 1)
        return n;

    long long previous = 0;
    long long current  = 1;

    for (long long i = 0; i < n - 1; ++i) {
        long long tmp_previous = previous;
        previous = current;
        current = (tmp_previous + current)%10;
    }

    return current % 10;
}

long long get_fibonacci_partial_sum_naive(long long from, long long to) {
    long long s=sum(to+2)-1;
    long long t=sum(from+1)-1;
    return (s-t+10)%10;
}

int main() {
    long long from, to;
    std::cin >> from >> to;
    std::cout << get_fibonacci_partial_sum_naive(from, to) << '\n';
}
