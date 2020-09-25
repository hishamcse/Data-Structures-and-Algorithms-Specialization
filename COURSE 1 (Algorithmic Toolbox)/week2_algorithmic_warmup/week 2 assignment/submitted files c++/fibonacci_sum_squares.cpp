#include <iostream>

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

int fibonacci_sum_squares_naive(long long n) {
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

    current=(current+previous)*current;
    return current % 10;
}

int main() {
    long long n = 0;
    std::cin >> n;
    std::cout << fibonacci_sum_squares_naive(n);
}
