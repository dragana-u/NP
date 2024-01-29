package Ispitni.vtorKolokviumIspit.i26;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * January 2016 Exam problem 1
 */

class StudentRecords{
    Map<String,Set<Student>> studentiPoNasoka;
    StudentRecords(){
        studentiPoNasoka = new TreeMap<>();
    }
    int readRecords(InputStream inputStream){
        Scanner sc = new Scanner(inputStream);
        int count=0;
        while(sc.hasNextLine()){
            String line = sc.nextLine();
            count++;
            String[] parts = line.split("\\s+");
            Set<Student> k = studentiPoNasoka.get(parts[1]);
            List<Integer> oceni = new ArrayList<>();
            for(int i=2;i<parts.length;i++){
                oceni.add(Integer.parseInt(parts[i]));
            }
            Student st = new Student(parts[0],parts[1],oceni);
            if(k==null){
                k = new TreeSet<>(Comparator.comparing(Student::getProsek).reversed().thenComparing(Student::getIndex));
            }
            k.add(st);
            studentiPoNasoka.putIfAbsent(parts[1],k);
        }
        return count;
    }
    void writeTable(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        for (Map.Entry<String, Set<Student>> stringSetEntry : studentiPoNasoka.entrySet()) {
            pw.println(stringSetEntry.getKey());
            for(Student st : stringSetEntry.getValue()){
                pw.println(st);
            }
        }
        pw.flush();
    }

    String distribucija(String key){
        StringBuilder sb = new StringBuilder();
        Set<Student> l = studentiPoNasoka.get(key);
        int deset=0,devet=0,osum=0,sedum=0,shest=0;
        for (Student student : l) {
            deset+=student.getBrojOcena(10);
            devet+=student.getBrojOcena(9);
            osum+=student.getBrojOcena(8);
            sedum+=student.getBrojOcena(7);
            shest+=student.getBrojOcena(6);
        }
        String zv6=getZvezdicki(shest),zv7=getZvezdicki(sedum),
                zv8=getZvezdicki(osum),zv9=getZvezdicki(devet),zv10=getZvezdicki(deset);
        sb.append(String.format("%2d | %s(%d)\n",6,zv6,shest));
        sb.append(String.format("%2d | %s(%d)\n",7,zv7,sedum));
        sb.append(String.format("%2d | %s(%d)\n",8,zv8,osum));
        sb.append(String.format("%2d | %s(%d)\n",9,zv9,devet));
        sb.append(String.format("%2d | %s(%d)",10,zv10,deset));
        return sb.toString();
    }
    String getZvezdicki(int broj){
        String res="";
        for(int i=0;i<broj;i+=10) {
           res = res.concat("*");
        }
        return res;
    }
    void writeDistribution(OutputStream outputStream){
        PrintWriter pw = new PrintWriter(outputStream);
        Comparator<Set<Student>> c = Comparator.comparingInt(students -> students.stream().mapToInt(Student::get10).sum());
        studentiPoNasoka
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(c.reversed()))
                .forEach(i -> pw.printf("%s\n%s\n",i.getKey(),distribucija(i.getKey())));
        pw.flush();
    }
}

class Student{
    String index;
    String nasoka;
    List<Integer> oceni;

    public Student(String index, String nasoka, List<Integer> oceni) {
        this.index = index;
        this.nasoka = nasoka;
        this.oceni = oceni;
    }

    public String getIndex() {
        return index;
    }

    public String getNasoka() {
        return nasoka;
    }

    public List<Integer> getOceni() {
        return oceni;
    }
    public double getProsek(){
        return oceni.stream().mapToDouble(i -> i).sum()/oceni.size();
    }

    @Override
    public String toString() {
        return String.format("%s %.2f",index,getProsek());
    }
    public int getBrojOcena(int o){
        return oceni.stream().filter(i -> i==o).collect(Collectors.toList()).size();
    }
    public int get10(){
        return oceni.stream().filter(i -> i==10).collect(Collectors.toList()).size();
    }
}

public class StudentRecordsTest {
    public static void main(String[] args) {
        System.out.println("=== READING RECORDS ===");
        StudentRecords studentRecords = new StudentRecords();
        int total = studentRecords.readRecords(System.in);
        System.out.printf("Total records: %d\n", total);
        System.out.println("=== WRITING TABLE ===");
        studentRecords.writeTable(System.out);
        System.out.println("=== WRITING DISTRIBUTION ===");
        studentRecords.writeDistribution(System.out);
    }
}

// your code here
