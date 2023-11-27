package Ispitni.prvKolokvium.i19;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class SubtitlesTest {
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

// Вашиот код овде
class Subtitles{
    List<Element> elements;

    public Subtitles() {
        elements = new ArrayList<>();
    }

    public int loadSubtitles(InputStream in) {
        Scanner sc = new Scanner(in);
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            int number = Integer.parseInt(line);
            String time = sc.nextLine();
            StringBuilder text = new StringBuilder();
            while(true){
                if(!sc.hasNextLine()) break;
                line = sc.nextLine();
                if(line.trim().length()==0){
                    break;
                }
                text.append(line);
                text.append("\n");
            }
            Element element = new Element(number,time,text.toString());
            elements.add(element);
        }
        return elements.size();
    }

    public void print() {
        elements.forEach(System.out::print);
    }

    public void shift(int shift) {
        elements.forEach(i -> {
            i.vremePocetok +=shift;
            i.vremeKraj +=shift;
        });
    }
}

class Element{
    int redenBroj;
    int vremePocetok;
    int vremeKraj;
    String prevod;

    public Element(int redenBroj,String vreme , String prevod) {
        this.redenBroj = redenBroj;
        this.prevod = prevod;
        String[] parts = vreme.split("-->");
        vremePocetok =stringToTime(parts[0].trim());
        vremeKraj = stringToTime(parts[1].trim());
    }
    static int stringToTime(String time){
        //00:00:48,321
        String[] parts = time.split(",");
        int res = Integer.parseInt(parts[1]);
        //00:00:48(0)  321(1)
        String[] parts2 = parts[0].split(":");
        //00 00 48
        int sec = Integer.parseInt(parts2[2]);
        int min = Integer.parseInt(parts2[1]);
        int h = Integer.parseInt(parts2[0]);
        res+=sec*1000;
        res+=min*60*1000;
        res+=h*60*60*1000;
        return res;
    }
    static String timeToString(int time){
        int h = time/(60*60*1000);
        time = time%(60*60*1000);
        int min = time/(60*1000);
        time = time%(60*1000);
        int sec = time/1000;
        int ms = time%1000;
        return String.format("%02d:%02d:%02d,%03d",
                h,min,sec,ms);
    }

    @Override
    public String toString() {
        return String.format("%d\n%s --> %s\n%s\n",
                redenBroj,timeToString(vremePocetok),timeToString(vremeKraj),prevod);
    }
}
