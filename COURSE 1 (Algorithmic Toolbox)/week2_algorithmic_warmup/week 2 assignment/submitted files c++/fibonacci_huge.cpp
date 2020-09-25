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

long long get_fibonacci_huge_naive(long long n, long long m) {
    n=n%Pisano(m);

    if (n <= 1)
        return n;

    long long previous = 0;
    long long current  = 1;

    for (long long i = 0; i < n - 1; ++i) {
        long long tmp_previous = previous;
        previous = current;
        current = (tmp_previous + current)%m;
    }

    return current % m;
}

int main() {
    long long n, m;
    std::cin >> n >> m;
    std::cout << get_fibonacci_huge_naive(n, m) << '\n';
}
