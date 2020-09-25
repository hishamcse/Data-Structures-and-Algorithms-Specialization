#include <iostream>
#include <vector>
#include <algorithm>
#include <queue>

using std::vector;
using std::cin;
using std::cout;
using namespace std;

auto Comparator = [](const pair<int, long long> o1, const pair<int, long long> o2) {

        if ((o1.second) == (o2.second))
            return (o1.first > o2.first);
        else
            return ((o1.second) > (o2.second));

    };

class JobQueue {
 private:
  int num_workers_;
  vector<int> jobs_;

  vector<int> assigned_workers_;
  vector<long long> start_times_;

  void WriteResponse() const {
    for (int i = 0; i < jobs_.size(); ++i) {
      cout << assigned_workers_[i] << " " << start_times_[i] << "\n";
    }
  }

  void ReadData() {
    int m;
    cin >> num_workers_ >> m;
    jobs_.resize(m);
    for(int i = 0; i < m; ++i)
      cin >> jobs_[i];
  }

  void AssignJobs() {
    // TODO: replace this code with a faster algorithm.
    assigned_workers_.resize(jobs_.size());
    start_times_.resize(jobs_.size());
    priority_queue<pair<int,long long>,vector<pair<int,long long>>,decltype(Comparator)> q(Comparator);
    for (int i = 0; i < num_workers_; i++) {
        q.push(make_pair(i,0));
    }
    for (int i = 0; i < jobs_.size(); i++) {
        int time = jobs_[i];
        pair<int,long long> p;
        p= q.top();
        q.pop();
        assigned_workers_[i] = p.first;
        start_times_[i] = p.second;
        q.push(make_pair(p.first,p.second+time));
    }

  }

 public:
  void Solve() {
    ReadData();
    AssignJobs();
    WriteResponse();
  }
};

int main() {
  std::ios_base::sync_with_stdio(false);
  JobQueue job_queue;
  job_queue.Solve();
  return 0;
}
