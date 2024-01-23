package Ispitni.vtorKolokviumIspit.i3;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Discounts{

    List<Store> stores;

    public Discounts() {
        stores = new ArrayList<>();
    }

    public int readStores(InputStream in) {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        stores = br.lines().map(Store::new).collect(Collectors.toList());
        return stores.size();
    }

    public List<Store> byAverageDiscount(){
        return stores.stream().sorted(Comparator.comparing(Store::averageDiscount).thenComparing(Store::getName).reversed()).limit(3).collect(Collectors.toList());
    }
    public List<Store> byTotalDiscount(){
        return stores.stream().sorted(Comparator.comparing(Store::totalDiscount).thenComparing(Store::getName)).limit(3).collect(Collectors.toList());
    }
}

class Store{
    String name;
    List<Product> products;

    public Store(String line) {
        //Desigual 5967:9115  5519:9378  3978:5563  7319:13092  8558:10541
        products = new ArrayList<>();
        String[] parts = line.split("\\s+");
        name = parts[0];
        for(int i=1;i<parts.length;i++){
            String[] ceni = parts[i].split(":");
            products.add(new Product(Double.parseDouble(ceni[1]),Double.parseDouble(ceni[0])));
        }
    }

    public String getName() {
        return name;
    }

    int totalDiscount(){
        int sum=0;
        for (Product product : products) {
            sum+=product.getDiff();
        }
        return sum;
    }

    double averageDiscount(){
        double sum=0;
        for (Product product : products) {
            sum+= product.totalDiscount();
        }
        return sum/products.size();
    }
    @Override
    public String toString() {
        String line = String.format("%s\nAverage discount: %.1f%%\nTotal discount: %d\n",name,averageDiscount(),totalDiscount());
        products = products.stream().sorted(Comparator.comparing(Product::totalDiscount).thenComparing(Product::getDiff).reversed()).collect(Collectors.toList());
        for (int i=0;i<products.size();i++) {
            line = line.concat(String.format("%2d%% %.0f/%.0f", products.get(i).totalDiscount(), products.get(i).discountPrices, products.get(i).prices));
            if(i!=products.size()-1){
                line = line.concat("\n");
            }
        }
        return line;
    }
}

class Product{

    double prices;
    double discountPrices;
    public Product(double prices,double discountPrices) {
        this.prices = prices;
        this.discountPrices = discountPrices;
    }
    public int totalDiscount(){
        return (int) ((prices - discountPrices)/prices*100);
    }
    public int getDiff(){
        return (int) (prices - discountPrices);
    }
}
public class DiscountsTest {
    public static void main(String[] args) {
        Discounts discounts = new Discounts();
        int stores = discounts.readStores(System.in);
        System.out.println("Stores read: " + stores);
        System.out.println("=== By average discount ===");
        discounts.byAverageDiscount().forEach(System.out::println);
        System.out.println("=== By total discount ===");
        discounts.byTotalDiscount().forEach(System.out::println);
    }
}
