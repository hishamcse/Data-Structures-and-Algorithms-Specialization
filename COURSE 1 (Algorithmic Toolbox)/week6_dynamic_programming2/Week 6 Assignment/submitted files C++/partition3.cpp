#include <iostream>
#include <vector>
#include <algorithm>
using namespace std;
using std::vector;

int approach3(vector<int> &A,int sum){
        int m=sum/3;
        vector<vector<vector<bool>>> dp(A.size()+1, vector<vector<bool>>(m+1, vector<bool>(m+1, 0)));
        dp[0][0][0]=1;
        for(int i=0;i<A.size();i++){
            for(int j=0;j<=m;j++){
                for(int k=0;k<=m;k++){
                    dp[i+1][j][k]=dp[i][j][k];
                    if(j>=A[i]){
                        dp[i+1][j][k]=dp[i+1][j][k] | dp[i][j-A[i]][k];
                    }
                    if(k>=A[i]){
                        dp[i+1][j][k]=dp[i+1][j][k] | dp[i][j][k-A[i]];
                    }
                }
            }
        }
        return dp[A.size()][m][m];
    }

int partition3(vector<int> &A) {
      int sum = 0;
        for (int i=0;i<A.size();i++) {
            sum += A[i];
        }
        if (sum % 3 == 0) {
            return approach3(A,sum);
        }
        return 0;
}

int main() {
  int n;
  std::cin >> n;
  vector<int> A(n);
  for (size_t i = 0; i < A.size(); ++i) {
    std::cin >> A[i];
  }
  std::cout << partition3(A) << '\n';
}
