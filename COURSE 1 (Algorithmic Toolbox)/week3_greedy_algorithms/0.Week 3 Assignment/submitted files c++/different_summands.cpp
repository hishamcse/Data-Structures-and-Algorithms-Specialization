#include <iostream>
#include <vector>
#include<algorithm>

using std::vector;

vector<int> optimal_summands(int n) {
    vector<int> summands;

    if(n==0){
        return summands;
    }

    for(int i=1;;i++){

        if(find(summands.begin(), summands.end(), (int)n-i) == summands.end() && (n-i)!=i){
            summands.push_back(i);
            n-=i;
            if(n<=i){
                break;
            }
        }
    }
    return summands;
}

int main() {
  int n;
  std::cin >> n;
  vector<int> summands = optimal_summands(n);
  std::cout << summands.size() << '\n';
  for (size_t i = 0; i < summands.size(); ++i) {
    std::cout << summands[i] << ' ';
  }
}
