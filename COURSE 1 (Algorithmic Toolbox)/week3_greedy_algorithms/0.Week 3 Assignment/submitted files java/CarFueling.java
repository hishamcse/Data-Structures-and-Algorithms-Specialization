import java.util.*;
import java.io.*;

public class CarFueling {
    static int computeMinRefills(int dist, int tank, int[] stops) {

        int numofrefills=0;
        int currentrefill=0;

        int[] allstops=new int[stops.length+2];

        allstops[0]=0;
        allstops[allstops.length-1]=dist;

        for(int i=1;i<allstops.length-1;i++){
            allstops[i]=stops[i-1];
        }


        while (currentrefill<allstops.length-1){
            int lastrefill=currentrefill;

            while (currentrefill<allstops.length-1 && allstops[currentrefill+1]-allstops[lastrefill]<=tank){
                currentrefill+=1;
            }

            if(currentrefill==lastrefill){
                return -1;
            }

            if(currentrefill<=allstops.length-1){
                numofrefills+=1;
            }
        }
        return numofrefills-1;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int dist = scanner.nextInt();
        int tank = scanner.nextInt();
        int n = scanner.nextInt();
        int[] stops = new int[n];
        for (int i = 0; i < n; i++) {
            stops[i] = scanner.nextInt();
        }

        System.out.println(computeMinRefills(dist, tank, stops));
    }
}
