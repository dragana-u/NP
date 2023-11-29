package Ispitni.prvKolokvium.i14;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static java.lang.Math.abs;

public class WeatherStationTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        int n = scanner.nextInt();
        scanner.nextLine();
        WeatherStation ws = new WeatherStation(n);

        while (true) {
            String line = scanner.nextLine();
            if (line.equals("=====")) {
                break;
            }
            String[] parts = line.split(" ");
            float temp = Float.parseFloat(parts[0]);
            float wind = Float.parseFloat(parts[1]);
            float hum = Float.parseFloat(parts[2]);
            float vis = Float.parseFloat(parts[3]);

            line = scanner.nextLine();
            LocalDateTime dateTime = LocalDateTime.parse(line, formatter);
            ws.addMeasurement(temp, wind, hum, vis, dateTime);
        }

        String fromLine = scanner.nextLine();
        LocalDateTime from = LocalDateTime.parse(fromLine, formatter);

        String toLine = scanner.nextLine();
        LocalDateTime to = LocalDateTime.parse(toLine, formatter);

        scanner.close();

        System.out.println(ws.total());

        try {
            ws.status(from, to);
        } catch (RuntimeException e) {
            System.out.println(e);
        }
    }
}

class WeatherStation {
     int days;
     float avgtemp=0;
     List<Measurments> measurmentsList;

    public WeatherStation(int days) {
        this.days = days;
        measurmentsList = new ArrayList<>();
    }

    public void addMeasurement(float temp, float wind, float hum, float vis, LocalDateTime dateTime) {
        Measurments m = new Measurments(dateTime);
        for (Measurments measurments : measurmentsList) {
            if(measurments.compareTo(m)==0){
                return;
            }
        }
        measurmentsList.removeIf(i -> i.ldt.compareTo(dateTime.minusDays(days))<=0);
        measurmentsList.add(new Measurments(temp,wind,hum,vis,dateTime));

    }

    public void status(LocalDateTime from, LocalDateTime to) {
         measurmentsList = measurmentsList
                .stream()
                .sorted()
                .filter(measurments -> {
                    return !measurments.ldt.isBefore(from) && !measurments.ldt.isAfter(to);
                })
                .collect(Collectors.toList());
         if(measurmentsList.isEmpty()){
             throw new RuntimeException();
         }
         measurmentsList.forEach(i -> avgtemp+=i.temp);
        measurmentsList.forEach(System.out::println);
        System.out.printf("Average temperature: %.2f\n",avgtemp/measurmentsList.size());
    }

    public int total() {
        return measurmentsList.size();
    }
}

class Measurments implements Comparable<Measurments>{
    float temp;
    float wind;
    float hum;
    float vis;
    LocalDateTime ldt;

    public Measurments(LocalDateTime localDateTime){
        ldt = localDateTime;
    }

    public float getTemp() {
        return temp;
    }

    public Measurments(float temp, float wind, float hum, float vis, LocalDateTime ldt) {
        this.temp = temp;
        this.wind = wind;
        this.hum = hum;
        this.vis = vis;
        this.ldt = ldt;
    }

    @Override
    public int compareTo(Measurments o) {
        long t1 = ldt.getSecond(); //1h 5m 30s //1h  3m 0s
        long t2 = o.ldt.getSecond();
        long tt = ldt.getMinute();
        long tt2 = o.ldt.getMinute();
        if((abs(t1-t2)) < 30 && abs(tt-tt2) <= 2 && ldt.getHour()-o.ldt.getHour()==0 && abs(ldt.getDayOfMonth()-o.ldt.getDayOfMonth())==0){
            return 0;
        }
        return ldt.compareTo(o.ldt);
    }
    @Override
    public String toString() {
        String str = String.format("%.1f %.1f km/h %.1f%% %.1f km ", temp,
                wind, hum, vis);
        //Tue Dec 17 23:30:15 GMT 2013
        String week = String.valueOf(ldt.getDayOfWeek()).substring(0,3).toLowerCase();
        String week1 = week.substring(0,1).toUpperCase();
        week = week1+week.substring(1,3);
        String month = String.valueOf(ldt.getMonth()).substring(0,3).toLowerCase();
        String month1 = month.substring(0,1).toUpperCase();
        month = month1+month.substring(1,3);

        return str + String.format("%s %s %d %02d:%02d:%02d GMT %d",
                week,month, ldt.getDayOfMonth(),
                ldt.getHour(),ldt.getMinute(),ldt.getSecond(),ldt.getYear());
    }
}