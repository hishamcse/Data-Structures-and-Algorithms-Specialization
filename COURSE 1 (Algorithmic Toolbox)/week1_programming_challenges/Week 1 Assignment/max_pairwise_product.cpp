#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;

long MaxPairwiseProduct(std::vector<int>& numbers) {
    long max_product = 0;
    int n = numbers.size();

    std::sort(numbers.begin(),numbers.end());
    max_product=(long)numbers[n-1]*(long)numbers[n-2];

    return max_product;
}

int main() {
    int n;
    std::cin >> n;
    std::vector<int> numbers(n);
    for (int i = 0; i < n; ++i) {
        std::cin >> numbers[i];
    }

//    n=rand()%10+2;
//    std::vector<int> numbers(n);
//    for (int i = 0; i < n; ++i) {
//        numbers.push_back(rand()%100000);
//        cout<<numbers.at(i)<<endl;
//    }
    std::cout << MaxPairwiseProduct(numbers) << "\n";
    return 0;
}

