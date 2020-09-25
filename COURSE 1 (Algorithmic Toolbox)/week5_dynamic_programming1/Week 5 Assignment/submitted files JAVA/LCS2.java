import java.util.Scanner;

public class LCS2 {

    private static int lcs2(int[] a, int[] b) {
        int[][] d=new int[a.length+1][b.length+1];
        for(int j=0;j<=b.length;j++){
            for(int i=0;i<=a.length;i++){
                if(i==0){
                    d[i][j]=0;
                }
                else if(j==0){
                    d[i][j]=0;
                }
                else {
                    int insertion = d[i][j - 1] ;
                    int deletion = d[i - 1][j] ;
                    int match = d[i - 1][j - 1];
                    if (a[i-1] == b[j-1]) {
                        d[i][j] = 1+match;
                    }else {
                        d[i][j] = Math.max(insertion, deletion);
                    }
                }
            }
        }
        return d[a.length][b.length];
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = scanner.nextInt();
        }

        int m = scanner.nextInt();
        int[] b = new int[m];
        for (int i = 0; i < m; i++) {
            b[i] = scanner.nextInt();
        }
        System.out.println(lcs2(a, b));
    }
}
