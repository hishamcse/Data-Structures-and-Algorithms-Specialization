#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using std::vector;

int lcs3(vector<int> &a, vector<int> &b, vector<int> &c) {
    int d[a.size() + 1][b.size() + 1][c.size() + 1];
        for (int k = 0; k <= c.size(); k++) {
            for (int j = 0; j <= b.size(); j++) {
                for (int i = 0; i <= a.size(); i++) {
                    if (i == 0 || j == 0 || k == 0) {
                        d[i][j][k] = 0;
                    } else {
                        int insertion = d[i][j - 1][k];
                        int deletion = d[i - 1][j][k];
                        int extra = d[i][j][k - 1];
                        int match = d[i - 1][j - 1][k - 1];
                        if (a[i - 1] == b[j - 1] && a[i - 1] == c[k - 1]) {
                            d[i][j][k] = 1 + match;
                        } else {
                            d[i][j][k] = max(extra, max(insertion, deletion));
                        }
                    }
                }
            }
        }
        return d[a.size()][b.size()][c.size()];
}

int main() {
  size_t an;
  std::cin >> an;
  vector<int> a(an);
  for (size_t i = 0; i < an; i++) {
    std::cin >> a[i];
  }
  size_t bn;
  std::cin >> bn;
  vector<int> b(bn);
  for (size_t i = 0; i < bn; i++) {
    std::cin >> b[i];
  }
  size_t cn;
  std::cin >> cn;
  vector<int> c(cn);
  for (size_t i = 0; i < cn; i++) {
    std::cin >> c[i];
  }
  std::cout << lcs3(a, b, c) << std::endl;
}
