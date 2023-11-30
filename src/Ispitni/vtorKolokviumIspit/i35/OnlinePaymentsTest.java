package Ispitni.vtorKolokviumIspit.i35;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class OnlinePaymentsTest {
    public static void main(String[] args) {
        OnlinePayments onlinePayments = new OnlinePayments();

        onlinePayments.readItems(System.in);

        IntStream.range(151020, 151025).mapToObj(String::valueOf).forEach(id -> onlinePayments.printStudentReport(id, System.out));
    }
}

class OnlinePayments{
    List<Stavki> stavki;

    public OnlinePayments() {
        stavki = new ArrayList<>();
    }

    void readItems (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        stavki = br.lines().map(Stavki::new).collect(Collectors.toList());
    }
    void printStudentReport (String index, OutputStream os){
        List<Stavki> st = stavki.stream().filter(i -> i.indeks.equals(index)).collect(Collectors.toList());
        PrintWriter pw = new PrintWriter(os);
        if(st.isEmpty()){
            pw.printf("Student %s not found!\n",index);
            pw.flush();
            return;
        }
        pw.printf("Student: %s Net: ",index);
        int net = 0;
        for (Stavki student : st) {
            net+= student.price;
        }
        long fee = Math.round(net * (1.14/100));
        if(fee<3){
            fee = 3;
        }
        if(fee>300){
            fee=300;
        }
        long total = fee + net;
        pw.printf("%d Fee: %d Total: %d\n",net,fee,total);
        pw.println("Items:");
        st = st.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for (int i = 0; i < st.size(); i++) {
            pw.printf("%d. %s %d\n",i+1,st.get(i).itemName,st.get(i).price);
        }
        pw.flush();
    }
}

class Stavki implements Comparable<Stavki>{
    String indeks;
    String itemName;
    int price;

    public Stavki(String line) {
        //151020;Административно-материјални трошоци и осигурување;750
        String[] parts = line.split(";");
        indeks = parts[0];
        itemName = parts[1];
        price = Integer.parseInt(parts[2]);
    }

    @Override
    public int compareTo(Stavki o) {
        return price - o.price;
    }
}