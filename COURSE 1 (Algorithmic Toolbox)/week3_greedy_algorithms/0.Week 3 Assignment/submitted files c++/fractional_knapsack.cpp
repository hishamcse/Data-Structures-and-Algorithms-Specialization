#include <iostream>
#include <vector>
#include<algorithm>
using namespace std;

void swaping(vector<int> &a,int i,int j){
    int temp=a.at(i);
    a.at(i)=a.at(j);
    a.at(j)=temp;
}

void swaping(vector<double> &a,int i,int j){
    double temp=a.at(i);
    a.at(i)=a.at(j);
    a.at(j)=temp;
}

double get_optimal_value(int capacity, vector<int> weights, vector<int> values) {

   vector<double> allUnits(values.size());
   for(int i=0;i<values.size();i++){
      allUnits[i]=(double)values[i]/(double)weights[i];
   }
   for(int i = 0; i < allUnits.size()-1; i++) {
        for(int j = 0; j < allUnits.size()-i-1; j++) {
            if(allUnits.at(j) < allUnits.at(j+1)) {
                swaping(allUnits,j,j+1);
                swaping(values,j,j+1);
                swaping(weights,j,j+1);
            }
        }
   }

    double v = 0.0;

    int n=weights.size();

    for(int i=0;i<n;i++){
        if(capacity==0){
            return v;
        }
        int a= min(capacity,weights[i]);
        v=v+a*(double)allUnits[i];
        weights[i]=weights[i]-a;
        capacity-=a;
    }

    return v;
}

int main() {
  int n;
  int capacity;
  std::cin >> n >> capacity;
  vector<int> values(n);
  vector<int> weights(n);
  for (int i = 0; i < n; i++) {
    std::cin >> values[i] >> weights[i];
  }

  double optimal_value = get_optimal_value(capacity, weights, values);

  printf("%.4f",optimal_value);
  return 0;
}
