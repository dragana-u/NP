package Ispitni.prvKolokvium.i7;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

class UnsupportedFormatException extends Exception {
    public UnsupportedFormatException(String message) {
        super(message);
    }
}

class InvalidTimeException extends Exception {
    public InvalidTimeException(String message) {
        super(message);
    }
}

class TimeTable {
    List<TimeFactory> times;

    public TimeTable() {
        times = new ArrayList<>();
    }

    public void readTimes(InputStream in) throws InvalidTimeException, UnsupportedFormatException {
        Scanner sc = new Scanner(in);
        while (sc.hasNextLine()) {
            String time = sc.nextLine();
            String[] parts = time.split(" ");
            for (String part : parts) {
                times.add(new TimeFactory(part));
            }

        }
    }

    void writeTimes(OutputStream outputStream, TimeFormat format) {
        PrintWriter pw = new PrintWriter(outputStream);
        if (TimeFormat.FORMAT_24 == format) {
            times.stream().sorted().forEach(t -> pw.println(t));
        } else {
            times.stream().sorted().forEach(t ->{
                if(t.h==0){
                    t.h=12;
                    t.p=" AM";
                }else if(t.h==12){
                    t.p=" PM";
                }else if(t.h>=1 && t.h<=11){
                    t.p=" AM";
                } else{
                    t.h=t.h-12;
                    t.p=" PM";
                }
                pw.println(t+t.p);
            });
        }
        pw.flush();
    }

}

class TimeFactory implements Comparable<TimeFactory> {
    int h;
    int min;
    String p;

    public TimeFactory(String line) throws UnsupportedFormatException, InvalidTimeException {
        if (line.contains(":") || line.contains(".")) {
            int hTemp, minTemp;
            if (line.contains(":")) {
                String[] parts = line.split(":");
                //23:12
                hTemp = Integer.parseInt(parts[0]);
                minTemp = Integer.parseInt(parts[1]);
            } else {
                String[] parts = line.split("\\.");
                hTemp = Integer.parseInt(parts[0]);
                minTemp = Integer.parseInt(parts[1]);
            }
            if (hTemp < 0 || hTemp > 23 || minTemp < 0 || minTemp > 59) {
                throw new InvalidTimeException(line);
            } else {
                min = minTemp;
                h = hTemp;
                p="";
            }
        } else {
            throw new UnsupportedFormatException(line);
        }
    }

    @Override
    public String toString() {
        return String.format("%2d:%02d", h, min);
    }

    @Override
    public int compareTo(TimeFactory o) {
        if (h == o.h) {
            return min - o.min;
        } else {
            return h - o.h;
        }
    }
}

public class TimesTest {

    public static void main(String[] args) {
        TimeTable timeTable = new TimeTable();
        try {
            timeTable.readTimes(System.in);
        } catch (UnsupportedFormatException e) {
            System.out.println("UnsupportedFormatException: " + e.getMessage());
        } catch (InvalidTimeException e) {
            System.out.println("InvalidTimeException: " + e.getMessage());
        }
        System.out.println("24 HOUR FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_24);
        System.out.println("AM/PM FORMAT");
        timeTable.writeTimes(System.out, TimeFormat.FORMAT_AMPM);
    }

}

enum TimeFormat {
    FORMAT_24, FORMAT_AMPM
}