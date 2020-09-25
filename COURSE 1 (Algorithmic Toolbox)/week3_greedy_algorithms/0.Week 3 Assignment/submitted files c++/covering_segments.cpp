#include <algorithm>
#include <iostream>
#include <climits>
#include <vector>

using namespace std;
using std::vector;

struct Segment {
  int start, end;
};

bool comparison(const Segment &a,const Segment &b){
  return a.end<b.end;
}

vector<int> optimal_points(vector<Segment> &segments) {
    sort(segments.begin(),segments.end(),comparison);
    vector<int> points;
    vector<int> result;
    result.push_back(segments[0].end);
    int p=segments[0].end;

    for (int i = 1; i < segments.size(); i++) {
       if(segments[i].start>p){
        p=segments[i].end;
        result.push_back(p);
     }
  }

  for(int i = 0; i < result.size(); i++){
    points.push_back(result[i]);
  }
  return points;
}

int main() {
  int n;
  std::cin >> n;
  vector<Segment> segments(n);
  for (int i = 0; i < segments.size(); i++) {
    std::cin >> segments[i].start >> segments[i].end;
  }
  vector<int> points = optimal_points(segments);
  std::cout << points.size() << "\n";
  for (int i = 0; i < points.size(); i++) {
    std::cout << points[i] << " ";
  }
}
