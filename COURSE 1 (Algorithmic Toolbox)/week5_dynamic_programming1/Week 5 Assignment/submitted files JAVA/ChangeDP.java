import java.util.Scanner;

public class ChangeDP {

    public static int dpchange(int[] options, int total) {
        int[] minnumofop = new int[total + 1];
        minnumofop[0] = 0;
        int num;
        for (int i = 1; i <= total; i++) {
            minnumofop[i] = Integer.MAX_VALUE;
            for (int j = 0; j < options.length; j++) {
                if (i >= options[j]) {
                    num = minnumofop[i - options[j]] + 1;
                    if (num < minnumofop[i]) {
                        minnumofop[i] = num;
                    }
                }
            }
        }
        return minnumofop[total];
    }

    public static void main(String[] args) {
        Scanner scanner=new Scanner(System.in);
        System.out.println(dpchange(new int[]{1, 3, 4}, scanner.nextInt()));
    }
}

