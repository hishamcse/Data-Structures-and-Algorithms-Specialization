#include <iostream>
#include <string>
#include <algorithm>
using namespace std;
using std::string;

int edit_distance(const string &str1, const string &str2) {
        char A[str1.length()];
        char B[str2.length()];
        for(int i=0;i<str1.length();i++){
            A[i]=str1[i];
        }
        for(int i=0;i<str2.length();i++){
            B[i]=str2[i];
        }
        int d[str1.length() + 1][str2.length() + 1];
        for (int j = 0; j <= str2.length(); j++) {
            for (int i = 0; i <= str1.length(); i++) {
                if (i == 0) {
                    d[i][j] = j;
                } else if (j == 0) {
                    d[i][j] = i;
                } else {
                    int insertion = d[i][j - 1] + 1;
                    int deletion = d[i - 1][j] + 1;
                    int mism = d[i - 1][j - 1] + 1;
                    int match = d[i - 1][j - 1];
                    if (A[i - 1] == B[j - 1]) {
                        d[i][j] = min(insertion, min(deletion, match));
                    } else {
                        d[i][j] = min(insertion, min(deletion, mism));
                    }
                }
            }
        }
        return d[str1.length()][str2.length()];
}

int main() {
  string str1;
  string str2;
  std::cin >> str1 >> str2;
  std::cout << edit_distance(str1, str2) << std::endl;
  return 0;
}
