#include <iostream>
#include <vector>
#include <algorithm>

using std::vector;
using std::cin;
using std::cout;
using std::swap;
using std::pair;
using std::make_pair;
using namespace std;

class HeapBuilder {
 private:
  vector<int> data_;
  vector< pair<int, int> > swaps_;
  int n;

  void WriteResponse() const {
    cout << swaps_.size() << "\n";
    for (int i = 0; i < swaps_.size(); ++i) {
      cout << swaps_[i].first << " " << swaps_[i].second << "\n";
    }
  }

  void ReadData() {
    cin >> n;
    data_.resize(n+1);
    for(int i = 1; i <= n; ++i)
      cin >> data_[i];
  }

  void sink(int k) {
    while (2 * k <= n) {
        int j = 2 * k;
        if (j < n && greater(j, j + 1)) {
            j++;
        }
        if (!greater(k, j)) {
            break;
        }
        swap(data_, k, j);
        k = j;
    }
  }

  bool greater(int i, int j) {
    return data_[i] > data_[j];
  }

    void swap(vector<int> &data, int i, int j) {
        int temp = data[i];
        data[i] = data[j];
        data[j] = temp;
        swaps_.push_back(make_pair(i-1,j-1));
    }

    void GenerateSwaps() {

        for (int i = n / 2; i >= 1; i--) {
            sink(i);
        }
    }

 public:
  void Solve() {
    ReadData();
    GenerateSwaps();
    WriteResponse();
  }
};

int main() {
  std::ios_base::sync_with_stdio(false);
  HeapBuilder heap_builder;
  heap_builder.Solve();
  return 0;
}
