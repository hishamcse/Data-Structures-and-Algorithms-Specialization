import java.util.Scanner;

public class FractionalKnapsack{

    private static double getOptimalValue(int capacity, int[] values, int[] weights) {

        double[] allUnits = new double[values.length];
        for(int i = 0; i < values.length; i++) {
            allUnits[i] = (double)values[i]/(double)weights[i];
        }
        //Sorting
        for(int i = 0; i < allUnits.length-1; i++) {
            for(int j = 0; j < allUnits.length-i-1; j++) {
                if(allUnits[j] < allUnits[j+1]) {
                    swap(allUnits,j,j+1);
                    swap(values,j,j+1);
                    swap(weights,j,j+1);
                }
            }
        }

        double v = 0;

        int n=weights.length;

        for(int i=0;i<n;i++){
            if(capacity==0){
                return v;
            }
            int a= Integer.min(capacity,weights[i]);
            v=v+a*allUnits[i];
            weights[i]=weights[i]-a;
            capacity-=a;
        }

        return v;
    }

    private static void swap(double[] a,int i,int j){
        double temp=a[i];
        a[i]=a[j];
        a[j]=temp;
    }

    private static void swap(int[] a,int i,int j){
        int temp=a[i];
        a[i]=a[j];
        a[j]=temp;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int capacity = scanner.nextInt();
        int[] values = new int[n];
        int[] weights = new int[n];
        for (int i = 0; i < n; i++) {
            values[i] = scanner.nextInt();
            weights[i] = scanner.nextInt();
        }
        System.out.format("%.4f",getOptimalValue(capacity, values, weights));
    }
}