#include <iostream>

long long lcm_naive(long long a, long long b) {
   if(b==0){
    return a;
   }
   return lcm_naive(b,a%b);
}

int main() {
  long long a, b;
  std::cin >> a >> b;
  std::cout <<(long long) (a*b)/lcm_naive(a, b) << std::endl;
  return 0;
}
