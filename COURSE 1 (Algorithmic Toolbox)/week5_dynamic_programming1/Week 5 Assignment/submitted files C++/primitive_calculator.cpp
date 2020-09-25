#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using std::vector;

vector<int> optimal_sequence(int n) {
      vector<int> sequence;
        int minNumOfOp[n + 1];
        minNumOfOp[0] = 0;
        minNumOfOp[1] = 0;
        int num1, num2, num3;
        for (int i = 2; i <= n; i++) {
            num1 = minNumOfOp[i - 1] + 1;
            num2 = INT8_MAX;
            num3 = INT8_MAX;
            if (i % 2 == 0) {
                num2 = minNumOfOp[i / 2] + 1;
            }
            if (i % 3 == 0) {
                num3 = minNumOfOp[i / 3] + 1;
            }
            minNumOfOp[i] = min(min(num1, num2), num3);
        }
  while (n >= 1) {
    sequence.push_back(n);
    if (n % 3 == 0 && minNumOfOp[n]==minNumOfOp[n/3]+1) {
      n /= 3;
    } else if (n % 2 == 0 && minNumOfOp[n]==minNumOfOp[n/2]+1) {
      n /= 2;
    } else {
      n = n - 1;
    }
  }
  reverse(sequence.begin(), sequence.end());
  return sequence;
}

int main() {
  int n;
  std::cin >> n;
  vector<int> sequence = optimal_sequence(n);
  std::cout << sequence.size() - 1 << std::endl;
  for (size_t i = 0; i < sequence.size(); ++i) {
    std::cout << sequence[i] << " ";
  }
}
