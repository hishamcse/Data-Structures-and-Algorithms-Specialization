#include <iostream>
#include <stack>
#include <string>
#include <algorithm>
using namespace std;
struct Bracket {
    Bracket(char type, int position):
        type(type),
        position(position)
    {}

    bool Matchc(char c) {
        if (type == '[' && c == ']')
            return true;
        if (type == '{' && c == '}')
            return true;
        if (type == '(' && c == ')')
            return true;
        return false;
    }

    char type;
    int position;
};

int main() {
    std::string text;
    getline(std::cin, text);

    std::stack <Bracket> opening_brackets_stack;
    int c=0;
    for (int position = 0; position < text.length(); ++position) {
        char next = text[position];

        if (next == '(' || next == '[' || next == '{') {
            Bracket *b=new Bracket(next,position+1);
            opening_brackets_stack.push(*b);
        }

        if (next == ')' || next == ']' || next == '}') {
            if(opening_brackets_stack.empty()==true || opening_brackets_stack.top().Matchc(next)==false){
                opening_brackets_stack.pop();
                c=1;
                cout<<position+1<<endl;
                break;
            }
            opening_brackets_stack.pop();
        }
    }

    if(c==0 && opening_brackets_stack.empty()==true){
        cout<<"Success"<<endl;
    }else if(c==0){
        cout<<opening_brackets_stack.top().position<<endl;
    }

    return 0;
}
