#include <iostream>
#include <cassert>
#include <string>
#include <vector>
#include <algorithm>
#include <iomanip>
#include <cstring>
#include <climits>

using namespace std;

typedef long long int ll;

ll eval(ll a,ll b, char op) {
  if (op == '*') {
    return a * b;
  } else if (op == '+') {
    return a + b;
  } else if (op == '-') {
    return a - b;
  } else {
    assert(0);
  }
}

ll findMin(ll a, ll b, ll c, ll d, ll e) {
    return min(a, min(b, min(c, min(d, e))));
}

ll findMax(ll a, ll b, ll c, ll d, ll e) {
    return max(a, max(b, max(c, max(d, e))));
}

ll get_maximum_value(const string &exp) {
        int len=exp.length()/2+1;
        ll d[exp.length()/2+1];
        char op[exp.length()/2];
        int n=exp.length();
        char char_array[n+1];
        strcpy(char_array, exp.c_str());

        int x = 0;
        for (int i = 0; i < n; i += 2) {
            d[x] = (ll)char_array[i] -48;
            x++;
        }
        x = 0;
        for (int i = 1; i < n; i += 2) {
            op[x] = char_array[i];
            x++;
        }

        ll m[len][len];
        ll M[len][len];

        for(int i=0;i<len;i++){
            for(int j=0;j<len;j++){
                m[i][j]=0;
                M[i][j]=0;
            }
        }

        for (int i = 0; i < len; i++) {
            m[i][i] = d[i];
            M[i][i] = d[i];
        }

        for (int s = 1; s < len; s++) {
            for (int i = 0; i < len- s; i++) {
                int j = i + s;
                 ll mi = LLONG_MAX;
                 ll ma = LLONG_MIN;
           for (int k = i; k < j; k++) {
            ll a = eval(M[i][k], M[k + 1][j], op[k]);
            ll b = eval(M[i][k], m[k + 1][j], op[k]);
            ll c = eval(m[i][k], m[k + 1][j], op[k]);
            ll d = eval(m[i][k], M[k + 1][j], op[k]);
            mi = findMin(mi, a, b, c, d);
            ma = findMax(ma, a, b, c, d);
         }
                m[i][j] = mi;
                M[i][j] = ma;
            }
        }
        return M[0][len - 1];
}

int main() {
  string s;
  std::cin >> s;
  std::cout << get_maximum_value(s) << '\n';
}
