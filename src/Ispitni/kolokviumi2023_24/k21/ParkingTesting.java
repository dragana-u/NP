package Ispitni.kolokviumi2023_24.k21;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class DateUtil {
    public static long durationBetween(LocalDateTime start, LocalDateTime end) {
        return Duration.between(start, end).toMinutes();
    }
}

class ParkingFullException extends Exception{
    public ParkingFullException(String message) {
        super(message);
    }
}

interface IParking{
    void park(LocalDateTime start);
    void leave(LocalDateTime end);
}

abstract class ParkingState implements IParking{
    Car car;

    public ParkingState(Car parked) {
        this.car = parked;
    }
}

class ParkedState extends ParkingState{

    public ParkedState(Car car) {
        super(car);
    }

    @Override
    public void park(LocalDateTime start) {

    }

    @Override
    public void leave(LocalDateTime end) {
        car.end = end;
        car.parkingState = new UnParkedState(car);
    }
}
class UnParkedState extends ParkingState{

    public UnParkedState(Car car) {
        super(car);
    }

    @Override
    public void park(LocalDateTime start) {
        car.start = start;
        car.timesParked++;
        car.parkingState = new UnParkedState(car);
    }

    @Override
    public void leave(LocalDateTime end) {
        car.end = end;
    }
}

class Car {
    String registration;
    String spot;
    LocalDateTime start;
    LocalDateTime end;
    ParkingState parkingState = new UnParkedState(this);
    int timesParked = 0;

    public Car(String registration, String spot) {
        this.registration = registration;
        this.spot = spot;
    }

    void park(LocalDateTime start){
        parkingState.park(start);
    }
    void leave(LocalDateTime end){
        parkingState.leave(end);
    }

    @Override
    public String toString() {
       return String.format("Registration number: %s Spot: %s Start timestamp: %s",registration,spot,start);
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public String getRegistration() {
        return registration;
    }

    public long parkingDuration(){
        return DateUtil.durationBetween(start,end);
    }

    public String getSpot() {
        return spot;
    }

    public int getSpotCount() {
        return timesParked;
    }
}

class Parking{
    int maxCapacity;
    Map<String,Car> currentlyParkedCars;
    Set<String> spots;
    List<Car> carsLeft;

    public Parking(int maxCapacity) {
        this.maxCapacity = maxCapacity;
        currentlyParkedCars = new TreeMap<>();
        spots = new HashSet<>();
        carsLeft = new ArrayList<>();
    }
    void update (String registration, String spot, LocalDateTime timestamp, boolean entry) throws ParkingFullException {
        if (!currentlyParkedCars.containsKey(registration)) {
            if (this.maxCapacity == currentlyParkedCars.size()) {
                throw new ParkingFullException(String.format("Parking is full (Capacity: %d)", maxCapacity));
            }
            Car car = new Car(registration, spot);
            car.park(timestamp);
            currentlyParkedCars.put(registration, car);
        } else {
            Car car  = currentlyParkedCars.remove(registration);
            car.leave(timestamp);
            carsLeft.add(car);
        }

        spots.add(spot);
    }
    void currentState(){
        System.out.printf("Capacity filled: %.2f%%\n",(double) currentlyParkedCars.size()/maxCapacity * 100.0);
        Comparator<Car> cars = Comparator.comparing(Car::getStart).thenComparing(Car::getSpot).reversed();
        currentlyParkedCars.entrySet().stream().sorted(Map.Entry.comparingByValue(cars)).forEach(i -> System.out.println(i.getValue()));
    }
    void history(){
        carsLeft.stream().sorted(Comparator.comparingDouble(Car::parkingDuration).reversed())
                .forEach(i -> System.out.printf("%s End timestamp: %s Duration in minutes: %d\n",i,i.end,i.parkingDuration()));
    }
    Map<String, Integer> carStatistics(){
        Map<String, Integer> res = new TreeMap<>();
        res = carsLeft.stream().collect(Collectors.groupingBy(Car::getRegistration,TreeMap::new,Collectors.summingInt(Car::getSpotCount)));
        return res;
    }
    Map<String,Double> spotOccupancy (LocalDateTime start, LocalDateTime end){
        Map<String, Long> totalTimePerSpot = carsLeft.stream()
                .filter(entry -> entry.start.compareTo(start) >= 0 && entry.start.compareTo(end) < 0)
                .collect(Collectors.groupingBy(
                        Car::getSpot,
                        TreeMap::new,
                        Collectors.summingLong(entry -> entry.end.compareTo(end)<=0 ? entry.parkingDuration() : DateUtil.durationBetween(entry.start, end))
                ));

        Map<String, Double> occupancy = new TreeMap<>();

        totalTimePerSpot.entrySet().stream().forEach(
                entry -> occupancy.put(entry.getKey(), (100.0 * entry.getValue()) / DateUtil.durationBetween(start, end))
        );

        spots.stream().filter(spot -> !occupancy.containsKey(spot)).forEach(spot -> occupancy.put(spot,0.0));

        return occupancy;

    }
}

public class ParkingTesting {

    public static <K, V extends Comparable<V>> void printMapSortedByValue(Map<K, V> map) {
        map.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEach(entry -> System.out.println(String.format("%s -> %s", entry.getKey().toString(), entry.getValue().toString())));

    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int capacity = Integer.parseInt(sc.nextLine());

        Parking parking = new Parking(capacity);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");
            if (parts[0].equals("update")) {
                String registration = parts[1];
                String spot = parts[2];
                LocalDateTime timestamp = LocalDateTime.parse(parts[3]);
                boolean entrance = Boolean.parseBoolean(parts[4]);
                try {
                    parking.update(registration, spot, timestamp, entrance);
                } catch (ParkingFullException e) {
                    System.out.println(e.getMessage());
                }
            } else if (parts[0].equals("currentState")) {
                System.out.println("PARKING CURRENT STATE");
                parking.currentState();
            } else if (parts[0].equals("history")) {
                System.out.println("PARKING HISTORY");
                parking.history();
            } else if (parts[0].equals("carStatistics")) {
                System.out.println("CAR STATISTICS");
                printMapSortedByValue(parking.carStatistics());
            } else if (parts[0].equals("spotOccupancy")) {
                LocalDateTime start = LocalDateTime.parse(parts[1]);
                LocalDateTime end = LocalDateTime.parse(parts[2]);
                printMapSortedByValue(parking.spotOccupancy(start, end));
            }
        }
    }
}

