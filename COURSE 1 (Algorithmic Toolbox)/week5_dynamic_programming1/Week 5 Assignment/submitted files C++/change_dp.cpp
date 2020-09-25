#include <iostream>
#include <vector>
using namespace std;

int dpchange(int total) {
    vector<int> options{1,3,4};
    int minnumofop[total + 1];
    minnumofop[0] = 0;
    int num;
    for (int i = 1; i <= total; i++) {
        minnumofop[i] = 2147483647;
        for (int j = 0; j < options.size(); j++) {
            if (i >= options[j]) {
                num = minnumofop[i - options[j]] + 1;
                if (num < minnumofop[i]) {
                    minnumofop[i] = num;
                }
            }
        }
    }
    return minnumofop[total];
}

int main() {
  int m;
  std::cin >> m;
  std::cout << dpchange(m) << '\n';
}
