//package Ispitni.prvKolokvium.i14;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Scanner;
//
//public class WeatherStationTest {
//    public static void main(String[] args) throws ParseException {
//        Scanner scanner = new Scanner(System.in);
//        DateFormat df = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
//        int n = scanner.nextInt();
//        scanner.nextLine();
//        WeatherStation ws = new WeatherStation(n);
//        while (true) {
//            String line = scanner.nextLine();
//            if (line.equals("=====")) {
//                break;
//            }
//            String[] parts = line.split(" ");
//            float temp = Float.parseFloat(parts[0]);
//            float wind = Float.parseFloat(parts[1]);
//            float hum = Float.parseFloat(parts[2]);
//            float vis = Float.parseFloat(parts[3]);
//            line = scanner.nextLine();
//            Date date = df.parse(line);
//            ws.addMeasurment(temp, wind, hum, vis, date);
//        }
//        String line = scanner.nextLine();
//        Date from = df.parse(line);
//        line = scanner.nextLine();
//        Date to = df.parse(line);
//        scanner.close();
//        System.out.println(ws.total());
//        try {
//            ws.status(from, to);
//        } catch (RuntimeException e) {
//            System.out.println(e);
//        }
//    }
//}
//
//// vashiot kod ovde
//
//class WeatherStation{
//    int days;
//    float temp;
//    float wind;
//    float hum;
//    float vis;
//    Date date;
//    List<WeatherStation> ws;
//
//    public WeatherStation(int x) {
//        this.days = x;
//        ws = new ArrayList<>();
//    }
//
//    public void addMeasurment(float temp, float wind, float hum, float vis, Date date) {
//
//    }
//
//    public int total() {
//        return ws.size();
//    }
//    public void status(Date from, Date to){
//
//    }
//}
