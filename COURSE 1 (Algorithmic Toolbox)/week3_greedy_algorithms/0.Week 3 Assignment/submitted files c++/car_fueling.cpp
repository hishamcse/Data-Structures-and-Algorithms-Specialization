#include <iostream>
#include <vector>
using namespace std;
using std::cin;
using std::cout;
using std::vector;
using std::max;

int compute_min_refills(int dist, int tank, vector<int> & stops) {

        int numofrefills=0;
        int currentrefill=0;

        vector<int> allstops(stops.size()+2);

        allstops[0]=0;
        allstops[allstops.size()-1]=dist;

        for(int i=1;i<allstops.size()-1;i++){
            allstops[i]=stops[i-1];
        }


        while (currentrefill<allstops.size()-1){
            int lastrefill=currentrefill;

            while (currentrefill<allstops.size()-1 && allstops[currentrefill+1]-allstops[lastrefill]<=tank){
                currentrefill+=1;
            }

            if(currentrefill==lastrefill){
                return -1;
            }

            if(currentrefill<=allstops.size()-1){
                numofrefills+=1;
            }
        }
        return numofrefills-1;
}


int main() {
    int d = 0;
    cin >> d;
    int m = 0;
    cin >> m;
    int n = 0;
    cin >> n;

    vector<int> stops(n);
    for (size_t i = 0; i < n; ++i)
        cin >> stops.at(i);

    cout << compute_min_refills(d, m, stops) << "\n";

    return 0;
}
