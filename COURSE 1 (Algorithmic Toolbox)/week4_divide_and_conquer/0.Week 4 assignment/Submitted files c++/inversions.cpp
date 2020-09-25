#include <iostream>
#include <vector>
#include<algorithm>
using namespace std;
using std::vector;
//
//long long get_number_of_inversions(vector<int> &a, vector<int> &b, size_t left, size_t right) {
//  long long number_of_inversions = 0;
//  if (right <= left + 1) return number_of_inversions;
//  size_t ave = left + (right - left) / 2;
//  number_of_inversions += get_number_of_inversions(a, b, left, ave);
//  number_of_inversions += get_number_of_inversions(a, b, ave, right);
//  //write your code here
//  return number_of_inversions;
//}

long long merging(int *a, int *aux, int lo, int mid, int hi) {
    long long invers = 0;

    // copy to aux[]
//       if (hi + 1 - lo >= 0) System.arraycopy(a, lo, aux, lo, hi + 1 - lo);
    for(int i=lo;i<=hi;i++){
        aux[i]=a[i];
    }
//        copy(a.begin()+lo, a.begin()+hi+1-lo, back_inserter(aux));

        // merge back to a[]
    int i = lo,j = mid+1;
    for (int k = lo; k <= hi; k++) {
        if      (i > mid)           a[k] = aux[j++];
        else if (j > hi)            a[k] = aux[i++];
        else if (aux[j] < aux[i]) { a[k] = aux[j++]; invers += (mid - i+1); }
        else                        a[k] = aux[i++];
    }
    return invers;
}

    // return the number of inversions in the subarray b[lo..hi]
    // side effect b[lo..hi] is rearranged in ascending order
    long long counting(int *a, int *b, int *aux, int lo, int hi) {
        long long inversions = 0;
        if (hi <= lo) return 0;
        int mid = lo + (hi - lo) / 2;
        inversions += counting(a, b, aux, lo, mid);
        inversions += counting(a, b, aux, mid+1, hi);
        inversions += merging(b, aux, lo, mid, hi);
        return inversions;
    }

    long long counting(int *a,int n) {
        int *b=new int[n];
        int *aux=new int[n];
        for(int i=0;i<n;i++){
            b[i]=a[i];
        }
        return counting(a, b, aux, 0, n - 1);
    }

int main() {
  int n;
  std::cin >> n;
  int *a=new int[n];
//  vector<int> a(n);
  for (size_t i = 0; i < n; i++) {
    std::cin >> a[i];
  }
  std::cout << counting(a,n) << '\n';
}
