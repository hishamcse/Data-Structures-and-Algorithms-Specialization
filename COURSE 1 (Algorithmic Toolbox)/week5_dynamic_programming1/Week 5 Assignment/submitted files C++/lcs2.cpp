#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using std::vector;

int lcs2(vector<int> &a, vector<int> &b) {
     int d[a.size() + 1][b.size() + 1];
        for (int j = 0; j <= b.size(); j++) {
            for (int i = 0; i <= a.size(); i++) {
                if (i == 0) {
                    d[i][j] = 0;
                } else if (j == 0) {
                    d[i][j] = 0;
                } else {
                    int insertion = d[i][j - 1];
                    int deletion = d[i - 1][j];
                    int match = d[i - 1][j - 1];
                    if (a[i - 1] == b[j - 1]) {
                        d[i][j] = 1 + match;
                    } else {
                        d[i][j] = max(insertion, deletion);
                    }
                }
            }
        }
        return d[a.size()][b.size()];
}

int main() {
  size_t n;
  std::cin >> n;
  vector<int> a(n);
  for (size_t i = 0; i < n; i++) {
    std::cin >> a[i];
  }

  size_t m;
  std::cin >> m;
  vector<int> b(m);
  for (size_t i = 0; i < m; i++) {
    std::cin >> b[i];
  }

  std::cout << lcs2(a, b) << std::endl;
}
