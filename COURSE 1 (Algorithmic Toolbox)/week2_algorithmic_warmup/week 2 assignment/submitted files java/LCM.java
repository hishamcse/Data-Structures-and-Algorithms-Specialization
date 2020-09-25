import java.util.*;

public class LCM {
  private static long lcm(int a, int b) {
    if(b==0){
      return a;
    }
    return lcm(b,a%b);
  }

  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int a = scanner.nextInt();
    int b = scanner.nextInt();
    long p=(long)a*b;

    if(a>=b) {
      long d=p/lcm(a,b);
      System.out.println(d);
    }else{
      long d=p/lcm(b,a);
      System.out.println(d);
    }
  }
}
