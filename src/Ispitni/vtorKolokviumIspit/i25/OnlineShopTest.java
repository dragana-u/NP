package Ispitni.vtorKolokviumIspit.i25;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

enum COMPARATOR_TYPE {
    NEWEST_FIRST,
    OLDEST_FIRST,
    LOWEST_PRICE_FIRST,
    HIGHEST_PRICE_FIRST,
    MOST_SOLD_FIRST,
    LEAST_SOLD_FIRST
}

class ProductNotFoundException extends Exception {
    ProductNotFoundException(String message) {
        super(message);
    }
}


class Product {
    String category;
    String id;
    String name;
    LocalDateTime createdAt;
    double price;

    int sold;

    public Product(String category, String id, String name, LocalDateTime createdAt, double price) {
        this.category = category;
        this.id = id;
        this.name = name;
        this.createdAt = createdAt;
        this.price = price;
        sold = 0;
    }

    public String getCategory() {
        return category;
    }

    public int getSold() {
        return sold;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public  double getPrice() {
        return price;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Product.class.getSimpleName() + "{", "}")
                .add("id='" + id + "'")
                .add("name='" + name + "'")
                .add("createdAt=" + createdAt)
                .add("price=" + price)
                .add("quantitySold=" + sold)
                .toString();
    }
}


class OnlineShop {
    Map<String,Set<Product>> shops;
    OnlineShop() {
        shops = new HashMap<>();
    }

    void addProduct(String category, String id, String name, LocalDateTime createdAt, double price){
        Product p = new Product(category, id, name, createdAt, price);
        Set<Product> products = shops.get(id);
        if(products == null){
            products = new HashSet<>();
        }
        products.add(p);
        shops.putIfAbsent(id,products);
    }

    double buyProduct(String id, int quantity) throws ProductNotFoundException{
        Set<Product> product = shops.get(id);
        if(product == null){
            throw new ProductNotFoundException(String.format("Product with id %s does not exist in the online shop!",id));
        }
        double cena = 0;
        for (Product pr : product) {
            cena = pr.price*quantity;
            pr.setSold(quantity);
        }
        return cena;
    }

    List<List<Product>> listProducts(String category, COMPARATOR_TYPE comparatorType, int pageSize) {
        List<List<Product>> result = new ArrayList<>();
        result.add(new ArrayList<>());
        List<Product> vnatresna = new ArrayList<>();
        List<Set<Product>> pr = shops.values().stream().collect(Collectors.toList());
        for (Set<Product> productSet : pr) {
            vnatresna.addAll(productSet);
        }
        if(category!=null){
            vnatresna = vnatresna.stream().filter(i -> i.category.equals(category)).collect(Collectors.toList());
        }
        if(comparatorType.compareTo(COMPARATOR_TYPE.NEWEST_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getCreatedAt).reversed()).collect(Collectors.toList());
        }else if(comparatorType.compareTo(COMPARATOR_TYPE.OLDEST_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getCreatedAt)).collect(Collectors.toList());
        }else if(comparatorType.compareTo(COMPARATOR_TYPE.LOWEST_PRICE_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getPrice)).collect(Collectors.toList());
        }else if(comparatorType.compareTo(COMPARATOR_TYPE.HIGHEST_PRICE_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getPrice).reversed()).collect(Collectors.toList());
        }else if(comparatorType.compareTo(COMPARATOR_TYPE.MOST_SOLD_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getSold).reversed()).collect(Collectors.toList());
        }else if(comparatorType.compareTo(COMPARATOR_TYPE.LEAST_SOLD_FIRST) == 0){
            vnatresna = vnatresna.stream().sorted(Comparator.comparing(Product::getSold)).collect(Collectors.toList());
        }
        int idx = 0;
        for(Product p : vnatresna) {
            if(result.get(idx).size() == pageSize) {
                result.add(new ArrayList<>());
                idx++;
            }
            result.get(idx).add(p);
        }

        return result;
    }

}

public class OnlineShopTest {

    public static void main(String[] args) {
        OnlineShop onlineShop = new OnlineShop();
        double totalAmount = 0.0;
        Scanner sc = new Scanner(System.in);
        String line;
        while (sc.hasNextLine()) {
            line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equalsIgnoreCase("addproduct")) {
                String category = parts[1];
                String id = parts[2];
                String name = parts[3];
                LocalDateTime createdAt = LocalDateTime.parse(parts[4]);
                double price = Double.parseDouble(parts[5]);
                onlineShop.addProduct(category, id, name, createdAt, price);
            } else if (parts[0].equalsIgnoreCase("buyproduct")) {
                String id = parts[1];
                int quantity = Integer.parseInt(parts[2]);
                try {
                    totalAmount += onlineShop.buyProduct(id, quantity);
                } catch (ProductNotFoundException e) {
                    System.out.println(e.getMessage());
                }
            } else {
                String category = parts[1];
                if (category.equalsIgnoreCase("null"))
                    category=null;
                String comparatorString = parts[2];
                int pageSize = Integer.parseInt(parts[3]);
                COMPARATOR_TYPE comparatorType = COMPARATOR_TYPE.valueOf(comparatorString);
                printPages(onlineShop.listProducts(category, comparatorType, pageSize));
            }
        }
        System.out.println("Total revenue of the online shop is: " + totalAmount);

    }

    private static void printPages(List<List<Product>> listProducts) {
        for (int i = 0; i < listProducts.size(); i++) {
            System.out.println("PAGE " + (i + 1));
            listProducts.get(i).forEach(System.out::println);
        }
    }
}


