package Ispitni.prvKolokvium.CakeShop;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CakeShopApplicationTest2 {
    public static void main(String[] args) {
        CakeShopApplication cakeShopApplication = new CakeShopApplication(4);

        System.out.println("--- READING FROM INPUT STREAM ---");
        cakeShopApplication.readCakeOrders(System.in);

        System.out.println("--- PRINTING TO OUTPUT STREAM ---");
        cakeShopApplication.printAllOrders(System.out);
    }
}

class CakeShopApplication {
    int minOrder;
    List<CakeFactory> cakes;

    public CakeShopApplication(int minOrder) {
        this.minOrder = minOrder;
        cakes = new ArrayList<>();
    }

    void readCakeOrders(InputStream inputStream) {
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        cakes = br.lines().map(line -> {
            try {
                return CakeFactory.create(line, minOrder);
            } catch (InvalidOrderException e) {
                System.out.println(e.getMessage());
                return null;
            }
        }).filter(Objects::nonNull).collect(Collectors.toList());
        int m=0;
    }

    void printAllOrders(OutputStream outputStream) {
        PrintWriter pw = new PrintWriter(outputStream);
        cakes = cakes.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for(int i=0;i<cakes.size();i++){
            System.out.printf("%s %d %d %d %d\n",
                    cakes.get(i).iD,
                    cakes.get(i).orders.size(),
                    cakes.get(i).orders.size() - cakes.get(i).countCake,
                    cakes.get(i).countCake,
                    cakes.get(i).total);
        }
        pw.flush();
    }
}
enum type {
    C,
    P
}

class CakeFactory implements Comparable<CakeFactory>{
    String iD;
    List<Order> orders;
    int countCake = 0;
    int total =0;

    public CakeFactory(String iD, List<Order> orders, int countCake, int total) {
        this.iD = iD;
        this.orders = orders;
        this.countCake = countCake;
        this.total = total;
    }
    static CakeFactory create(String line, int min) throws InvalidOrderException {
        //0  1   2   3   4   5   6
        //10 C4 528 P49 556 P35 638
        //orderId item1Name item1Price item2Name item2Price
        String[] parts = line.split("\\s+");
        if ((parts.length - 1) / 2.0 < min) {
            throw new InvalidOrderException(String.format("The order with id %s has less items than the minimum allowed.", parts[0]));
        }
        int count = 0;
        int total = 0;
        String iD = parts[0];
        List<Order> orderList = new ArrayList<>();
        for (int i = 1; i < parts.length; i++) {
            if (i % 2 != 0) {
                if (parts[i].startsWith("C")) {
                    count++;
                    Cake cake = new Cake((Integer.parseInt(parts[i + 1])));
                    orderList.add(cake);
                   total+=cake.getPrice();
                } else {
                    Pita pita = new Pita((Integer.parseInt(parts[i + 1])));
                    orderList.add(pita);
                    total+=pita.getPrice();
                }
            }
        }
        return new CakeFactory(iD, orderList, count,total);
    }

    @Override
    public int compareTo(CakeFactory o) {
        return total - o.total;
    }
}

abstract class Order  {
    type tip;

    public Order(type tip) {
        this.tip = tip;
    }
    abstract int getPrice();

}

class Pita extends Order {
    int price;
    int counter=0;

    public Pita(int price) {
        super(type.P);
        this.price = price;
    }

    @Override
    public int getPrice() {
        return price+50;
    }
}

class Cake extends Order {
    int price;


    public Cake(int price) {
        super(type.C);
        this.price = price;
    }

    @Override
    int getPrice() {
        return price;
    }
}

class InvalidOrderException extends Exception {
    public InvalidOrderException(String message) {
        super(message);
    }
}