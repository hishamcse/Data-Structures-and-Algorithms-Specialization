#include <algorithm>
#include <sstream>
#include <iostream>
#include <vector>
#include <string>
using namespace std;

using std::vector;
using std::string;

bool compare(string o1,string o2){
   return (o1+o2)<(o2+o1);
}
string largest_number(vector<string> a) {

    int n=a.size();
    sort(a.begin(),a.end(),compare);
    string res1 = "";
    string res2 = "";
    for (int i = 0; i < a.size(); i++) {
        res1 += a[n-i-1];
        res2 += a[i];
    }

  std::stringstream ret1,ret2;
  for (size_t i = 0; i < a.size(); i++) {
    ret1 << a[n-i-1];
    ret2 << a[i];
  }
  string result1;
  string result2;
  ret1>>result1;
  ret2>>result2;
  if(result1.compare(result2)>0){
    return result1;
  }
  return result2;
}

int main() {
  int n;
  std::cin >> n;
  vector<string> a(n);
  for (size_t i = 0; i < a.size(); i++) {
    std::cin >> a[i];
  }
  std::cout << largest_number(a);
}
