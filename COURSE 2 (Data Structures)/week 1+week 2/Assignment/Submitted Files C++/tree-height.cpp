#include <algorithm>
#include <iostream>
#include <vector>
#include <queue>
#include <map>
#if defined(__unix__) || defined(__APPLE__)
#include <sys/resource.h>
#endif
using namespace std;

class Node;

class Node {
public:
    int value;
    std::vector<int> children;

    Node(int value) : value(value) {}

    void add_child(int child) {
        this->children.push_back(child);
    }

    int get_size() {
        return this->children.size();
    }
};
 int get_max_height(map<int, Node *> &nodes,Node *node) {
        if (node == NULL) return 0;
        if (node->get_size() == 0) return 1; // no children

        int height = 0;
        for (int i = 0; i < node->get_size(); i++) {
            int height_of_this_child = get_max_height(nodes,nodes[node->children[i]]);
            height = max(height, height_of_this_child);
        }

        return height + 1;
    }

int main_with_large_stack_space() {
  std::ios_base::sync_with_stdio(0);
  int n;
  std::cin >> n;
  Node *root;
  std::vector<int> parents;
  std::map<int, Node *> nodes;

  for (int i = 0; i < n; i++) {
        nodes[i] = new Node(i);
  }
  for (int child_index = 0; child_index < n; child_index++) {
    int parent_index;
    std::cin >> parent_index;
    if(parent_index==-1){
        root=nodes[child_index];
    }else{
      nodes[parent_index]->add_child(child_index);
    }
  }

  // Replace this code with a faster implementation
  cout<<get_max_height(nodes,root);
}

int main (int argc, char **argv)
{
#if defined(__unix__) || defined(__APPLE__)
  // Allow larger stack space
  const rlim_t kStackSize = 16 * 1024 * 1024;   // min stack size = 16 MB
  struct rlimit rl;
  int result;

  result = getrlimit(RLIMIT_STACK, &rl);
  if (result == 0)
  {
      if (rl.rlim_cur < kStackSize)
      {
          rl.rlim_cur = kStackSize;
          result = setrlimit(RLIMIT_STACK, &rl);
          if (result != 0)
          {
              std::cerr << "setrlimit returned result = " << result << std::endl;
          }
      }
  }

#endif
  return main_with_large_stack_space();
}
