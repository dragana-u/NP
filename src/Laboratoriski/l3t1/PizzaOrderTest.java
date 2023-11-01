package Laboratoriski.l3t1;

import java.util.*;


interface Item {
    int getPrice();

    String getType();
}

class EmptyOrder extends Exception {
    public EmptyOrder() {
    }
}

class OrderLockedException extends Exception {
    public OrderLockedException() {
    }
}

class ItemOutOfStockException extends Exception {
    public ItemOutOfStockException(Item item) {
        super(String.format("%s out of stock!", item));
    }
}

class InvalidExtraTypeException extends Exception {
    public InvalidExtraTypeException() {
    }
}

class InvalidPizzaTypeException extends Exception {
    public InvalidPizzaTypeException() {
    }
}

class ExtraItem implements Item {
    private String type;

    public ExtraItem(String type) throws InvalidExtraTypeException {
        if (type.equalsIgnoreCase("coke")
                || type.equalsIgnoreCase("ketchup")) {
            this.type = type;
        } else {
            throw new InvalidExtraTypeException();
        }
    }

    @Override
    public int getPrice() {
        if (type.equalsIgnoreCase("coke")) {
            return 5;
        } else {
            return 3;
        }
    }

    @Override
    public String getType() {
        return type;
    }

}

class PizzaItem implements Item {
    private String type;

    @Override
    public String getType() {
        return type;
    }

    public PizzaItem(String type) throws InvalidPizzaTypeException {
        if (type.equalsIgnoreCase("standard")
                || type.equalsIgnoreCase("vegetarian")
                || type.equalsIgnoreCase("pepperoni")) {
            this.type = type;
        } else {
            throw new InvalidPizzaTypeException();
        }
    }

    @Override
    public int getPrice() {
        if (type.equalsIgnoreCase("standard")) {
            return 10;
        } else if (type.equalsIgnoreCase("vegetarian")) {
            return 8;
        } else {
            return 12;
        }
    }
}

class Order {

    private List<Item> items;
    private boolean isLocked;
    private int[] counts;

    public Order() {
        items = new ArrayList<>();
        isLocked = false;
        counts = new int[100];
    }

    public void addItem(Item item, int count) throws ItemOutOfStockException, OrderLockedException {
        if (count > 10) {
            throw new ItemOutOfStockException(item);
        }
        if (isLocked) {
            throw new OrderLockedException();
        }
        Optional<Item> first = items.stream()
                .filter(each -> each.getType().equals(item.getType()))
                .findFirst();
        if(first.isPresent()){
            first.ifPresent(i -> counts[items.indexOf(i)]=count);
            return;
        }
        items.add(item);
        int ind = items.indexOf(item);
        counts[ind] = count;
    }

    int getPrice() {
        int sum = 0;
        for (int i = 0; i < items.size(); i++) {
            sum += items.get(i).getPrice() * counts[i];
        }
        return sum;
    }

    public void displayOrder() {
        for (int i = 0; i < items.size(); i++) {
            System.out.printf("%3d.%-15sx%2d%5d$\n", i + 1, items.get(i).getType(), counts[i], items.get(i).getPrice() * counts[i]);
        }
        System.out.printf("%-22s%5d$", "Total:", getPrice());
        System.out.print("\n");
    }

    public void removeItem(int idx) throws OrderLockedException {
        if(isLocked){
            throw new OrderLockedException();
        }
        items.remove(idx);
    }

    public void lock() throws EmptyOrder {
        if (items.isEmpty()) {
            throw new EmptyOrder();
        }
        isLocked = true;
    }
}

public class PizzaOrderTest {

    public static void main(String[] args) {
        Scanner jin = new Scanner(System.in);
        int k = jin.nextInt();
        if (k == 0) { //test Item
            try {
                String type = jin.next();
                String name = jin.next();
                Item item = null;
                if (type.equals("Pizza")) item = new PizzaItem(name);
                else item = new ExtraItem(name);
                System.out.println(item.getPrice());
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
        if (k == 1) { // test simple order
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 2) { // test order with removing
            Order order = new Order();
            while (true) {
                try {
                    String type = jin.next();
                    String name = jin.next();
                    Item item = null;
                    if (type.equals("Pizza")) item = new PizzaItem(name);
                    else item = new ExtraItem(name);
                    if (!jin.hasNextInt()) break;
                    order.addItem(item, jin.nextInt());
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            jin.next();
            System.out.println(order.getPrice());
            order.displayOrder();
            while (jin.hasNextInt()) {
                try {
                    int idx = jin.nextInt();
                    order.removeItem(idx);
                } catch (Exception e) {
                    System.out.println(e.getClass().getSimpleName());
                }
            }
            System.out.println(order.getPrice());
            order.displayOrder();
        }
        if (k == 3) { //test locking & exceptions
            Order order = new Order();
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.addItem(new ExtraItem("Coke"), 1);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.lock();
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
            try {
                order.removeItem(0);
            } catch (Exception e) {
                System.out.println(e.getClass().getSimpleName());
            }
        }
    }

}
