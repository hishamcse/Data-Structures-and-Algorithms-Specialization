import java.util.*;

public class GCD {
  private static int gcd(int a, int b) {
    if(b==0){
      return a;
    }
    return gcd(b,a%b);
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();

    if(a>=b) {
      System.out.println(gcd(a, b));
    }else{
      System.out.println(gcd(b,a));
    }
  }
}
