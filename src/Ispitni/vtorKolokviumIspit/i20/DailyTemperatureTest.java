package Ispitni.vtorKolokviumIspit.i20;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * I partial exam 2016
 */
public class DailyTemperatureTest {
    public static void main(String[] args) {
        DailyTemperatures dailyTemperatures = new DailyTemperatures();
        dailyTemperatures.readTemperatures(System.in);
        System.out.println("=== Daily temperatures in Celsius (C) ===");
        dailyTemperatures.writeDailyStats(System.out, 'C');
        System.out.println("=== Daily temperatures in Fahrenheit (F) ===");
        dailyTemperatures.writeDailyStats(System.out, 'F');
    }
}

// Vashiot kod ovde

class DailyTemperatures{
    List<Measurement> measurements;

    public DailyTemperatures() {
        measurements = new ArrayList<>();
    }
    void readTemperatures(InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        measurements = br.lines()
                .map(Measurement::create)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
    void writeDailyStats(OutputStream outputStream, char scale){
        PrintWriter pw = new PrintWriter(outputStream);
        List<Measurement> collect = measurements
                .stream()
                .sorted(Comparator.comparing(i -> i.day))
                .collect(Collectors.toList());
        collect.forEach(i -> System.out.print(i.pecati(scale)));
        pw.flush();
    }
}
enum type{
    C,
    F
}
class Measurement  {
    int day;
    List<Scale> temp;

    public Measurement(List<Scale> temp, int day) {
        this.temp = temp;
        this.day = day;
    }

    static Measurement create(String line){
        //317 24C 29C 28C 29C
        String[] parts = line.split("\\s+");
        int den = Integer.parseInt(parts[0]);
        List<Scale> tmp = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            String scale = parts[i].substring(parts[i].length()-1);
            String sub = parts[i].substring(0, parts[i].length()-1);
            if(type.valueOf(scale).equals(type.C)){
                tmp.add(new Celsius(Integer.parseInt(sub),type.C));
            }else{
                tmp.add(new Fahrenheit(Integer.parseInt(sub),type.F));
            }
        }
        return new Measurement(tmp,den);
    }
    public int getBrojMerenja(){
        return temp.size();
    }
    public String pecati(char scale){
        List<Scale> m = temp;
        if(type.valueOf(String.valueOf(scale)).equals(type.C)){
             m.forEach(i -> {
                if(i.tip.equals(type.F)){
                    i.setTemp(i.convert());
                }
            });
            DoubleSummaryStatistics dss = m.stream().mapToDouble(i -> i.temp).summaryStatistics();
            return String.format("%3d: Count: %3d Min: %6.2fC Max: %6.2fC Avg: %6.2fC\n",
                    day,
                    getBrojMerenja(),
                    dss.getMin(),
                    dss.getMax(),
                    dss.getAverage());
        }else{
            temp.forEach(i -> {
                if(i.tip.equals(type.F)){
                    i.setTemp(i.converted());
                }else{
                    i.setTemp(i.convert());
                }
            });
            DoubleSummaryStatistics dss = temp.stream().mapToDouble(i -> i.temp).summaryStatistics();
            return String.format("%3d: Count: %3d Min: %6.2fF Max: %6.2fF Avg: %6.2fF\n",
                    day,
                    getBrojMerenja(),
                    dss.getMin(),
                    dss.getMax(),
                    dss.getAverage());
        }
    }

}

abstract class Scale{
    double temp;
    type tip;

    public Scale(int temp, type tip) {
        this.temp = temp;
        this.tip = tip;
    }
    abstract double convert();

    public void setTemp(double temp) {
        this.temp = temp;
    }
    public double converted() {
        return (temp*(9.0/5)+32);
    }
}
class Fahrenheit extends Scale{

    public Fahrenheit(int temp, type tip) {
        super(temp, tip);
    }

    @Override
    public double convert() {
        return (temp-32)*(5.0/9);
    }
}
class Celsius extends Scale{

    public Celsius(int temp, type tip) {
        super(temp, tip);
    }

    @Override
    public double convert() {
        return (temp*(9.0/5)+32);
    }
}