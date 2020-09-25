#include <iostream>
#include <cassert>
#include <vector>
using namespace std;

using std::vector;

long long binary_search(const vector<int> &a,int l,int h, int key) {

    if(l>h){
        return -1;
    }
    int mid=l+(h-l)/2;
    if(a[mid]==key)
    {
        return mid;
    }
    else if(a[mid]>key)
    {
        return binary_search(a,l,mid-1,key);
    }
    else
    {
        return binary_search(a,mid+1,h,key);
    }
}

int linear_search(const vector<int> &a, int x) {
  for (size_t i = 0; i < a.size(); ++i) {
    if (a[i] == x) return i;
  }
  return -1;
}

int main() {
  int n;
  std::cin >> n;
  vector<int> a(n);
  for (size_t i = 0; i < a.size(); i++) {
    std::cin >> a[i];
  }
  int m;
  std::cin >> m;
  vector<int> b(m);
  for (int i = 0; i < m; ++i) {
    std::cin >> b[i];
  }
  for (int i = 0; i < m; ++i) {
    //replace with the call to binary_search when implemented
    std::cout << binary_search(a,0,a.size()-1, b[i]) << ' ';
  }
}
