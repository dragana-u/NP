package Ispitni.vtorKolokviumIspit.i36;

import java.util.*;
import java.util.stream.Collectors;

/*
YOUR CODE HERE
DO NOT MODIFY THE interfaces and classes below!!!
*/

class DeliveryApp{
    String name;
    Map<String,DeliveryPerson> deliveryPeople;
    Map<String,Restaurant> restaurants;
    Map<String,User> users;

    public DeliveryApp(String name) {
        this.name = name;
        deliveryPeople = new HashMap<>();
        restaurants = new HashMap<>();
        users = new HashMap<>();
    }
    void registerDeliveryPerson (String id, String name, Location currentLocation){
        deliveryPeople.put(id,new DeliveryPerson(id,name,currentLocation));
    }
    void addRestaurant (String id, String name, Location location){
        restaurants.put(id,new Restaurant(id, name, location));
    }
    void addUser (String id, String name){
        users.put(id,new User(id, name));
    }
    void addAddress (String id, String addressName, Location location){
        User u = users.get(id);
        if(u!=null){
            u.addAddress(addressName,location);
        }
    }


    void orderFood(String userId, String userAddressName, String restaurantId, float cost){
        User u = users.get(userId);
        Restaurant r = restaurants.get(restaurantId);
        u.setTotalSpent(cost);
        r.setTotalEarned(cost);
        deliveryPeople
                .values()
                .stream()
                .sorted((del1,del2) -> del1.compareTo(del2,r.location))
                .collect(Collectors.toList()).get(0).addOrder(r.location,u.addresses.get(userAddressName));
    }
    void printUsers(){
        users
                .values()
                .stream().sorted(Comparator.comparing(User::getTotalSpent).thenComparing(User::getName).reversed())
                .forEach(System.out::println);
    }
    void printRestaurants(){
        restaurants
                .values()
                .stream().sorted(Comparator.comparing(Restaurant::getAverageEarned).thenComparing(Restaurant::getName).reversed())
                .forEach(System.out::println);
    }
    void printDeliveryPeople(){
        deliveryPeople
                .values()
                .stream().sorted(Comparator.comparing(DeliveryPerson::getTotalEarned).thenComparing(DeliveryPerson::getName).reversed())
                .forEach(System.out::println);
    }
}

class User{
    String id;
    String name;
    float totalSpent;
    int totalOrders;
    Map<String,Location> addresses;
    public User(String id, String name) {
        this.id = id;
        this.name = name;
        addresses = new HashMap<>();
        totalSpent=0;
        totalOrders=0;
    }
    void addAddress (String addressName, Location location){
        addresses.putIfAbsent(addressName,location);
    }

    public void setTotalSpent(float totalSpent) {
        this.totalSpent +=totalSpent;
        totalOrders++;
    }

    public float getTotalSpent() {
        return totalSpent;
    }
    public float getAverageSpent(){
        if(totalOrders==0){
            return 0;
        }else{
            return totalSpent/totalOrders;
        }
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total orders: %d Total amount spent: %.2f Average amount spent: %.2f",
                id,name,totalOrders,totalSpent,getAverageSpent());
    }

    public String getName() {
        return name;
    }
}

class DeliveryPerson{
    String id;
    String name;
    Location currentLocation;
    float totalEarned;
    int totalDeliveries;

    public DeliveryPerson(String id, String name, Location currentLocation) {
        this.id = id;
        this.name = name;
        this.currentLocation = currentLocation;
        this.totalEarned=0;
        this.totalDeliveries=0;
    }


    public String getName() {
        return name;
    }

    public void setTotalEarned(float totalEarned) {
        this.totalEarned += totalEarned;
        totalDeliveries++;
    }

    public void setCurrentLocation(Location newLocation) {
        this.currentLocation = newLocation;
    }

    public void addOrder(Location restaurantLocation, Location userLocation){
        int fee = 90 + 10*(restaurantLocation.distance(currentLocation)/10);
        setCurrentLocation(userLocation);
        setTotalEarned(fee);
    }

    public float getTotalEarned() {
        return totalEarned;
    }

    public float getAverageEarned(){
        if(totalDeliveries==0){
            return 0;
        }else{
            return totalEarned/totalDeliveries;
        }
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s Total deliveries: %d Total delivery fee: %.2f Average delivery fee: %.2f",
                id,name,totalDeliveries,totalEarned,getAverageEarned());
    }
    public int compareTo(DeliveryPerson other, Location location){
        if(currentLocation.distance(location) == other.currentLocation.distance(location) ){
            return Integer.compare(totalDeliveries,other.totalDeliveries);
        }else{
            return Integer.compare(currentLocation.distance(location),other.currentLocation.distance(location));
        }
    }
}

class Restaurant{
    String id;
    String name;
    Location location;
    float totalEarned;
    int numOrders;

    public String getName() {
        return name;
    }

    public Restaurant(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
        totalEarned=0;
        numOrders=0;
    }

    public void setTotalEarned(float totalEarned) {
        this.totalEarned += totalEarned;
        numOrders++;
    }
    public float getAverageEarned(){
        if(numOrders==0){
            return 0;
        }else {
            return totalEarned/numOrders;
        }
    }

    @Override
    public String toString() {
        return  String.format("ID: %s Name: %s Total orders: %d Total amount earned: %.2f Average amount earned: %.2f",
                id,name,numOrders,totalEarned,getAverageEarned());
    }
}

interface Location {
    int getX();

    int getY();

    default int distance(Location other) {
        int xDiff = Math.abs(getX() - other.getX());
        int yDiff = Math.abs(getY() - other.getY());
        return xDiff + yDiff;
    }
}

class LocationCreator {
    public static Location create(int x, int y) {

        return new Location() {
            @Override
            public int getX() {
                return x;
            }

            @Override
            public int getY() {
                return y;
            }
        };
    }
}

public class DeliveryAppTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String appName = sc.nextLine();
        DeliveryApp app = new DeliveryApp(appName);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split(" ");

            if (parts[0].equals("addUser")) {
                String id = parts[1];
                String name = parts[2];
                app.addUser(id, name);
            } else if (parts[0].equals("registerDeliveryPerson")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.registerDeliveryPerson(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addRestaurant")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addRestaurant(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("addAddress")) {
                String id = parts[1];
                String name = parts[2];
                int x = Integer.parseInt(parts[3]);
                int y = Integer.parseInt(parts[4]);
                app.addAddress(id, name, LocationCreator.create(x, y));
            } else if (parts[0].equals("orderFood")) {
                String userId = parts[1];
                String userAddressName = parts[2];
                String restaurantId = parts[3];
                float cost = Float.parseFloat(parts[4]);
                app.orderFood(userId, userAddressName, restaurantId, cost);
            } else if (parts[0].equals("printUsers")) {
                app.printUsers();
            } else if (parts[0].equals("printRestaurants")) {
                app.printRestaurants();
            } else {
                app.printDeliveryPeople();
            }

        }
    }
}

