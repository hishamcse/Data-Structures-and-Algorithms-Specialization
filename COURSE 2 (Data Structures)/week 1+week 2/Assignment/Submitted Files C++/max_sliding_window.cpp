#include <iostream>
#include <vector>
#include <algorithm>
#include <deque>
using namespace std;
using std::cin;
using std::cout;
using std::vector;
using std::max;

void max_sliding_window_naive(vector<int> const & A, int m) {
    int n=A.size();

    deque<int> deq;
        for (int i = 0; i < n; i++) {
            if (!deq.empty() && deq.front() == i - m) {
                deq.pop_front();
            }
            while (!deq.empty() && A[deq.back()] < A[i]) {
                deq.pop_back();
            }
            deq.push_back(i);
            if (i + 1 >= m) {
                cout<<A[deq.front()]<<" ";
            }
        }

    return;
}


int main() {
    int n = 0;
    cin >> n;

    vector<int> A(n);
    for (size_t i = 0; i < n; ++i)
        cin >> A.at(i);

    int w = 0;
    cin >> w;

    max_sliding_window_naive(A, w);

    return 0;
}
