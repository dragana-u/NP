package Ispitni.vtorKolokviumIspit.i35;
import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class OnlinePayments{

    Map<String, Student> students = new TreeMap<>();
    void readItems (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<String> lines = br.lines().collect(Collectors.toList());
        for (String line : lines) {
            Item item = new Item(line);
            String[] id = line.split(";");
            students.putIfAbsent(id[0],new Student(id[0]));
            Student student = students.get(id[0]);
            student.addItem(item);
        }
    }
    void printStudentReport (String index, OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        Student student = students.get(index);
        if(student==null) System.out.printf("Student %s not found!\n",index);
        else {
            pw.println(student);
            AtomicInteger j= new AtomicInteger(1);
            pw.println("Items:");
            student.stavki.stream().sorted(Comparator.comparing(Item::getCena).reversed()).forEach(i -> pw.printf("%d. %s\n", j.getAndIncrement(),i));
        }

        pw.flush();
    }
}

class Student{
    String id;
    List<Item> stavki = new ArrayList<>();

    public Student(String id) {
        this.id = id;
    }

    void addItem(Item item){
        stavki.add(item);
    }
    public String getId() {
        return id;
    }

    public List<Item> getStavki() {
        return stavki;
    }
    public int netoIznost(){
        return stavki.stream().flatMapToInt(i -> IntStream.of(i.cena)).sum();
    }
    int bankarskaProvizija(){
        double p = Math.round(1.14/100*netoIznost());
        if(p<3){
            return 3;
        }else if(p>300){
            return 300;
        }else {
            return (int) p;
        }
    }
    int vkupenIznos(){
        return netoIznost()+bankarskaProvizija();
    }

    @Override
    public String toString() {
        return String.format("Student: %s Net: %d Fee: %d Total: %d",id,netoIznost(),bankarskaProvizija(),vkupenIznos());
    }
}
class Item{
    String description;
    int cena;

    public Item(String line) {
        //151020;Административно-материјални трошоци и осигурување;750
        String[] parts = line.split(";");
        description = parts[1];
        cena = Integer.parseInt(parts[2]);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(description, item.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description);
    }

    public String getDescription() {
        return description;
    }

    public int getCena() {
        return cena;
    }

    @Override
    public String toString() {
        return String.format("%s %d",description,cena);
    }
}
public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}


//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.Comparator;
//import java.util.List;
//import java.util.stream.Collectors;
//import java.util.stream.IntStream;
//
//public class OnlinePaymentsTest {
//    public static void main(String[] args) {
//        OnlinePayments onlinePayments = new OnlinePayments();
//
//        onlinePayments.readItems(System.in);
//
//        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
//    }
//}
//
//class OnlinePayments{
//    List<Stavki> stavki;
//
//    public OnlinePayments() {
//        stavki = new ArrayList<>();
//    }
//
//    void readItems (InputStream is){
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        stavki = br.lines().map(Stavki::new).collect(Collectors.toList());
//    }
//    void printStudentReport (String index, OutputStream os){
//        List<Stavki> st = stavki.stream().filter(i -> i.indeks.equals(index)).collect(Collectors.toList());
//        PrintWriter pw = new PrintWriter(os);
//        if(st.isEmpty()){
//            pw.printf("Student %s not found!\n",index);
//            pw.flush();
//            return;
//        }
//        pw.printf("Student: %s Net: ",index);
//        int net = 0;
//        for (Stavki student : st) {
//            net+= student.price;
//        }
//        long fee = Math.round(net * (1.14/100));
//        if(fee<3){
//            fee = 3;
//        }
//        if(fee>300){
//            fee=300;
//        }
//        long total = fee + net;
//        pw.printf("%d Fee: %d Total: %d\n",net,fee,total);
//        pw.println("Items:");
//        st = st.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
//        for (int i = 0; i < st.size(); i++) {
//            pw.printf("%d. %s %d\n",i+1,st.get(i).itemName,st.get(i).price);
//        }
//        pw.flush();
//    }
//}
//
//class Stavki implements Comparable<Stavki>{
//    String indeks;
//    String itemName;
//    int price;
//
//    public Stavki(String line) {
//        //151020;Административно-материјални трошоци и осигурување;750
//        String[] parts = line.split(";");
//        indeks = parts[0];
//        itemName = parts[1];
//        price = Integer.parseInt(parts[2]);
//    }
//
//    @Override
//    public int compareTo(Stavki o) {
//        return price - o.price;
//    }
//}