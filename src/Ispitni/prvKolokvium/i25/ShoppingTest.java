package Ispitni.prvKolokvium.i25;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ShoppingTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        ShoppingCart cart = new ShoppingCart();

        int items = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < items; i++) {
            try {
                cart.addItem(sc.nextLine());
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }

        List<Integer> discountItems = new ArrayList<>();
        int discountItemsCount = Integer.parseInt(sc.nextLine());
        for (int i = 0; i < discountItemsCount; i++) {
            discountItems.add(Integer.parseInt(sc.nextLine()));
        }

        int testCase = Integer.parseInt(sc.nextLine());
        if (testCase == 1) {
            cart.printShoppingCart(System.out);
        } else if (testCase == 2) {
            try {
                cart.blackFridayOffer(discountItems, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}
class ShoppingCart{
    List<Stavka> stavki;

    public ShoppingCart() {
        stavki = new ArrayList<>();
    }

    void addItem(String itemData) throws InvalidOperationException {
        String[] parts = itemData.split(";");
        //0   1         2           3            4
        //WS;productID;productName;productPrice;quantity
        if(Double.parseDouble(parts[4])==0){
            throw new InvalidOperationException(String.format("The quantity of the product with id %s can not be 0.",parts[1]));
        }
        stavki.add(Stavka.createStavka(itemData));
    }
    void printShoppingCart(OutputStream os){
        PrintWriter printWriter = new PrintWriter(os);
        stavki
                .stream().
                sorted(Comparator.reverseOrder())
                .forEach(printWriter::println);
        printWriter.flush();
    }
    void blackFridayOffer(List<Integer> discountItems, OutputStream os) throws InvalidOperationException {
        if(discountItems.isEmpty()){
            throw new InvalidOperationException("There are no products with discount.");
        }
        PrintWriter pw = new PrintWriter(os);
        for(int i=0;i<discountItems.size();i++){
            int id = discountItems.get(i);
            List<Stavka> st = new ArrayList<>();
            if(!stavki.stream().anyMatch(x -> x.getiD()==id)){
                break;
            }
             st  = stavki
                    .stream()
                    .filter(j -> j.getiD() == id).collect(Collectors.toList());
            pw.printf(String.format("%d - %.2f\n",id,st.get(0).getPopust()));
        }
        pw.flush();
    }
}

enum typeStavka{
    WS,
    PS
}
abstract class Stavka implements Comparable<Stavka>{
    typeStavka tip;
    int iD;
    String name;
    int price;

    public Stavka(typeStavka tip, int iD, String name,int price) {
        this.tip = tip;
        this.iD = iD;
        this.name = name;
        this.price = price;
    }

    public int getiD() {
        return iD;
    }

    abstract double getQuantity();

    abstract double vkupnaCena();

    static Stavka createStavka(String line){
        String[] parts = line.split(";");
        //0   1         2           3             4
        //WS;productID;productName;productPrice;quantity
        int id = Integer.parseInt(parts[1]);
        String name = parts[2];
        int price = Integer.parseInt(parts[3]);
        if(typeStavka.WS == typeStavka.valueOf(parts[0])){
            int quantity = Integer.parseInt(parts[4]);
            return new WS(quantity,id,name,price);
        }else{
            double quantity = Double.parseDouble(parts[4]);
            return new PS(quantity,id,name,price);
        }
    }

    double getPopust() {
        return vkupnaCena()*0.1;
    }

    @Override
    public int compareTo(Stavka o) {
        return (int) (vkupnaCena()-o.vkupnaCena());
    }

    @Override
    public String toString() {
        return String.format("%d - %.2f",iD,vkupnaCena());
    }
}

class WS extends Stavka{
    int quantity;

    public WS(int quantity, int iD, String name,int price) {
        super(typeStavka.WS, iD, name,price);
        this.quantity = quantity;
    }

    @Override
    double getQuantity() {
        return quantity;
    }

    @Override
    double vkupnaCena() {
        return quantity*price;
    }

}

class PS extends Stavka{
    double quantity;

    public PS(double quantity,int iD, String name, int price) {
        super(typeStavka.PS, iD, name, price);
        this.quantity = quantity;
    }

    @Override
    double getQuantity() {
        return quantity;
    }

    @Override
    double vkupnaCena() {
        return quantity*price/1000;
    }
}

class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}