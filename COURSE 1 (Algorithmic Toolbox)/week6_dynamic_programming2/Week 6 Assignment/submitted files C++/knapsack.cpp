#include <iostream>
#include <vector>
using namespace std;
using std::vector;

int optimal_weight(int total, const vector<int> &weight) {
     vector<vector<int>> val(total+1,vector<int>(weight.size()+1));
        for (int i = 0; i <= weight.size(); i++) {
            for (int w = 0; w <= total; w++) {
                if (i == 0 || w == 0) {
                    val[w][i] = 0;
                } else {
                    val[w][i] = val[w][i - 1];
                    if (weight[i - 1] <= w) {
                        int value = val[w - weight[i - 1]][i - 1] + weight[i - 1];
                        if (value > val[w][i]) {
                            val[w][i] = value;
                        }
                    }
                }
            }
        }
        return val[total][weight.size()];
}

int main() {
  int n, W;
  std::cin >> W >> n;
  vector<int> w(n);
  for (int i = 0; i < n; i++) {
    std::cin >> w[i];
  }
  std::cout << optimal_weight(W, w) << '\n';
}
