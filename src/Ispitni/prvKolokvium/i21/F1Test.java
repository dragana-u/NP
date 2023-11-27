package Ispitni.prvKolokvium.i21;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class F1Test {

    public static void main(String[] args) {
        F1Race f1Race = new F1Race();
        f1Race.readResults(System.in);
        f1Race.printSorted(System.out);
    }

}

class F1Race {
    // vashiot kod ovde
    List<Driver> drivers;

    public F1Race() {
        drivers = new ArrayList<>();
    }

    public void readResults(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        drivers = br.lines().map(Driver::createDriver)
                .filter(Objects::nonNull)
                .collect(Collectors.toCollection(ArrayList::new));
    }
    public void printSorted(OutputStream out) {
        PrintWriter pw = new PrintWriter(out);
        drivers.sort(Driver::compareTo);
        int c = 1;
        for(Driver d : drivers){
            pw.printf("%d. %s\n",c++,d);
        }
        pw.flush();
    }

}

class Driver implements Comparable<Driver>{
    String driverName;
    List<String> laps;
    int best;

    public Driver(String driverName, List<String> laps, int best) {
        this.driverName = driverName;
        this.laps = laps;
        this.best=best;
    }

   public static int timeToInt(String time){
        String[] t = time.split(":");
        int mm = Integer.parseInt(t[0]);
        int ss = Integer.parseInt(t[1]);
        int nnn = Integer.parseInt(t[2]);
        return mm*60*1000 + ss*1000 + nnn;
   }
    public static Driver createDriver(String line){
        String[] split = line.split("\\s+");
        String tmpName = split[0];
        List<String> tmpDriver = new ArrayList<>();
        for(int i=0;i<split.length-1;i++){
            tmpDriver.add(split[i+1]);
        }
        int min = Math.min(Math.min(timeToInt(tmpDriver.get(0)), timeToInt(tmpDriver.get(1))
        ), timeToInt(tmpDriver.get(2)));
        return new Driver(tmpName,tmpDriver,min);
    }

    public static String timeToString(int time){
        int mm = (time/1000)/60;
        int ss = (time-mm*1000*60)/1000;
        int nnn = time%1000;
        return String.format("%d:%02d:%03d",mm,ss,nnn);
    }

    @Override
    public String toString() {
        return String.format("%-10s%10s",driverName,timeToString(best));
    }

    @Override
    public int compareTo(Driver o) {
        return this.best - o.best;
    }
}


