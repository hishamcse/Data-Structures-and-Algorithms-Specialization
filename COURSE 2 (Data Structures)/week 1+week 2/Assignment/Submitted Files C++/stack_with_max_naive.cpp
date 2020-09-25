#include <iostream>
#include <vector>
#include <string>
#include <cassert>
#include <algorithm>
#include <stack>

using std::cin;
using std::string;
using std::vector;
using std::cout;
using std::max_element;
using namespace std;

class StackWithMax {
    stack<int> st;

  public:

    void Push(int value) {
        if(st.empty()){
            st.push(value);
        }
        else{
            if(value>st.top()){
                st.push(value);
            }else{
                st.push(st.top());
            }
        }
    }

    void Pop() {
        assert(st.size());
        st.pop();
    }

    int Max() const {
        assert(st.size());
        return st.top();
    }
};

int main() {
    int num_queries = 0;
    cin >> num_queries;

    string query;
    string value;

    StackWithMax stack;

    for (int i = 0; i < num_queries; ++i) {
        cin >> query;
        if (query == "push") {
            cin >> value;
            stack.Push(std::stoi(value));
        }
        else if (query == "pop") {
            stack.Pop();
        }
        else if (query == "max") {
            cout << stack.Max() << "\n";
        }
        else {
            assert(0);
        }
    }
    return 0;
}
