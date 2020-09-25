import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Stack;

class Bracket {

    char type;
    int position;

    Bracket(char type, int position) {
        this.type = type;
        this.position = position;
    }
}

class check_brackets {
    public static void main(String[] args) throws IOException {
        InputStreamReader input_stream = new InputStreamReader(System.in);
        BufferedReader reader = new BufferedReader(input_stream);
        String text = reader.readLine();

        Stack<Bracket> opening_brackets_stack = new Stack<>();
        int c = 1;
        for (int position = 0; position < text.length(); ++position) {
            char next = text.charAt(position);

            if (next == '(' || next == '[' || next == '{') {
                opening_brackets_stack.push(new Bracket(next, position + 1));
            }

            if (next == ')') {
                if (opening_brackets_stack.empty() || opening_brackets_stack.pop().type != '(') {
                    System.out.println(position + 1);
                    c = 0;
                    break;
                }
            }
            if (next == '}') {
                if (opening_brackets_stack.empty() || opening_brackets_stack.pop().type != '{') {
                    System.out.println(position + 1);
                    c = 0;
                    break;
                }
            }
            if (next == ']') {
                if (opening_brackets_stack.empty() || opening_brackets_stack.pop().type != '[') {
                    System.out.println(position + 1);
                    c = 0;
                    break;
                }
            }
        }

        if (c == 1 && opening_brackets_stack.empty()) {
            System.out.println("Success");
        } else if (c == 1) {
            System.out.println(opening_brackets_stack.pop().position);
        }
    }
}

