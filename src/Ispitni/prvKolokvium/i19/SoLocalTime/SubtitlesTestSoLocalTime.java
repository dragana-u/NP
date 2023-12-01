package Ispitni.prvKolokvium.i19.SoLocalTime;

import java.io.InputStream;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SubtitlesTestSoLocalTime {
    public static void main(String[] args) {
        Subtitles subtitles = new Subtitles();
        int n = subtitles.loadSubtitles(System.in);
        System.out.println("+++++ ORIGINIAL SUBTITLES +++++");
        subtitles.print();
        int shift = n * 37;
        shift = (shift % 2 == 1) ? -shift : shift;
        System.out.println(String.format("SHIFT FOR %d ms", shift));
        subtitles.shift(shift);
        System.out.println("+++++ SHIFTED SUBTITLES +++++");
        subtitles.print();
    }
}
class Subtitles{
    List<SubtitlesFactory> list;

    public Subtitles() {
        list = new ArrayList<>();
    }
    int loadSubtitles(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        while(sc.hasNextLine()){
            int num = sc.nextInt();
            sc.nextLine();
            String vreme = sc.nextLine();
            StringBuilder sb = new StringBuilder();
            while(sc.hasNext()){
                String line = sc.nextLine();
                if(line.trim().isEmpty()) break;
                sb.append(line);
                sb.append("\n");
            }
            list.add(new SubtitlesFactory(num,sb.toString(),vreme));
        }
        return list.size();
    }
    void print(){
        list.forEach(System.out::println);
    }
    void shift(int ms){
        list.forEach(i -> i.shift(ms));
    }
}

class SubtitlesFactory{
    int redenBroj;
    String prevod;
    LocalTime timeStart;
    LocalTime timeFin;

    public SubtitlesFactory(int redenBroj, String prevod, String time) {
        this.redenBroj = redenBroj;
        this.prevod = prevod;
        String[] parts = time.split("-->");
        //00:00:48,321 00:00:50,837
        String[] vreme1 = parts[0].split(":");
        String[] vreme2 = parts[1].split(":");
        //00 00 48,321 // 00 00 50,837
        String[] nano1 = vreme1[2].split(",");
        String[] nano2 = vreme2[2].split(",");
        //00 00 48 321
        this.timeStart = LocalTime.of(Integer.parseInt(vreme1[0]),Integer.parseInt(vreme1[1]),Integer.parseInt(nano1[0]),Integer.parseInt(nano1[1].trim())*1000000);
        this.timeFin = LocalTime.of(Integer.parseInt(vreme2[0].trim()),Integer.parseInt(vreme2[1]),Integer.parseInt(nano2[0]),Integer.parseInt(nano2[1])*1000000);
    }
    public void shift(int ms){
      timeStart = timeStart.plusNanos(ms*1000000);
      timeFin = timeFin.plusNanos(ms*1000000);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(redenBroj)
                .append("\n")
                .append(String.format("%02d:%02d:%02d,%03d --> %02d:%02d:%02d,%03d",
                        timeStart.getHour(),
                        timeStart.getMinute(),
                        timeStart.getSecond(),
                        timeStart.getNano()/1000000,
                        timeFin.getHour(),
                        timeFin.getMinute(),
                        timeFin.getSecond(),
                        timeFin.getNano()/1000000))
                .append("\n")
                .append(prevod);
        return sb.toString();
    }
}
