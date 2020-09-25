#include <iostream>
#include <vector>
#include <cstdlib>
using namespace std;
using std::vector;
using std::swap;

int partition2(vector<int> &a, int l, int r) {
  int x = a[l];
  int j = l;
  for (int i = l + 1; i <= r; i++) {
    if (a[i] <= x) {
      j++;
      swap(a[i], a[j]);
    }
  }
  swap(a[l], a[j]);
  return j;
}

void swaping(vector<int> &a,int i,int j){
    int temp=a[i];
    a[i]=a[j];
    a[j]=temp;
}

int* partition3(vector<int> &a,int lo,int hi){

    if(hi<=lo){
            return nullptr;
    }

    int lt=lo;
    int gt=hi;
    int i=lo;
    int v=a[lo];

    while(i<=gt){
        if(a[i]<v){
            swaping(a,lt++,i++);
        }else if(a[i]>v){
            swaping(a,i,gt--);
        }else{
            i++;
        }
    }
    int m1=lt;
    int m2=gt;
    int *m=new int[2];
    m[0]=m1;
    m[1]=m2;
    return m;
}

void randomizedQuickSort(vector<int> &a, int l, int r) {
    if (l >= r) {
       return;
    }
    int k = rand()%(r - l + 1) + l;
    swaping(a,l,k);
    //use partition3
    int *m = partition3(a, l, r);
    randomizedQuickSort(a, l, m[0] - 1);   //lt-1
    randomizedQuickSort(a, m[1] + 1, r);  //gt+1
}

void randomized_quick_sort(vector<int> &a, int l, int r) {
  if (l >= r) {
    return;
  }

  int k = l + rand() % (r - l + 1);
  swap(a[l], a[k]);
  int m = partition2(a, l, r);

  randomized_quick_sort(a, l, m - 1);
  randomized_quick_sort(a, m + 1, r);
}

int main() {
  int n;
  std::cin >> n;
  vector<int> a(n);
  for (size_t i = 0; i < a.size(); ++i) {
    std::cin >> a[i];
  }
  randomizedQuickSort(a, 0, a.size() - 1);
  for (size_t i = 0; i < a.size(); ++i) {
    std::cout << a[i] << ' ';
  }
}
