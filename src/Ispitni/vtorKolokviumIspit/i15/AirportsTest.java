package Ispitni.vtorKolokviumIspit.i15;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class Airports{
    Map<String,Airport> airports;
    public Airports() {
        airports = new TreeMap<>();
    }

    public void addAirport(String name, String country, String code, int passengers){
        airports.put(code,new Airport(name, country, code, passengers));
    }
    public void addFlights(String from, String to, int time, int duration) {
        Airport ap = airports.get(from);
        ap.addFlight(from, to, time, duration);
    }
    public void showFlightsFromAirport(String code){
        Airport flightsFromAirport = airports.get(code);
        System.out.println(flightsFromAirport);
    }
    public void showDirectFlightsFromTo(String from, String to){
        Airport fr = airports.get(from);
        if(fr.flights.containsKey(to)){
            List<Flight> flights = fr.flights.values().stream().flatMap(Set::stream).filter(i -> i.to.equals(to)).collect(Collectors.toList());
            for (Flight flight : flights) {
                System.out.println(flight);
            }
        }else{
            System.out.printf("No flights from %s to %s\n",from,to);
        }

    }
    public void showDirectFlightsTo(String to) {
        Set<Flight> flights = new TreeSet<>(Comparator.comparing(Flight::getTime));
        for (Airport airport : airports.values()) {
            Set<Flight> flightsTo = airport.flights.get(to);
            if (flightsTo != null) {
                flights.addAll(flightsTo);
            }
        }
        for (Flight flight : flights) {
            System.out.println(flight);
        }
    }
}

class Airport{
    String name;
    String country;
    String code;
    int passengers;
    Map<String,Set<Flight>> flights;

    public Airport(String name, String country, String code, int passengers) {
        this.name = name;
        this.country = country;
        this.code = code;
        this.passengers = passengers;
        flights = new TreeMap<>();
    }
    void addFlight(String from, String to, int time, int duration){
        Set<Flight> flight = flights.get(to);
        Flight fl = new Flight(from, to, time, duration);
        if(flight == null){
            flight = new TreeSet<>(Comparator.comparing(Flight::getTo).thenComparing(Flight::getTime));
        }
        flight.add(fl);
        flights.putIfAbsent(to,flight);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        AtomicInteger i= new AtomicInteger(1);
        sb.append(String.format("%s (%s)\n%s\n%d\n",name,code,country,passengers));
        flights.values()
                .stream()
                .flatMap(Set::stream)
                .forEach(obj -> sb.append(String.format("%d. %s\n", i.getAndIncrement(),obj)));
        int len = sb.toString().length();
        sb.deleteCharAt(len-1);
        return sb.toString();
    }
}

class Flight{
    String from;
    String to;
    int time;
    int duration;

    public Flight(String from, String to, int time, int duration) {
        this.from = from;
        this.to = to;
        this.time = time;
        this.duration = duration;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public int getTime() {
        return time;
    }

    public int getDuration() {
        return duration;
    }



    @Override
    public String toString() {
        int end = time + duration;

        return String.format("%s-%s %02d:%02d-%02d:%02d%s%2dh%02dm",
                from,
                to,
                time/60%24,
                time%60,
                end/60%24,
                end%60,
                end/60>=24 ? " +1d" : "",
                duration/60%1440,
                duration%60%1440);
    }
}



public class AirportsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Airports airports = new Airports();
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] codes = new String[n];
        for (int i = 0; i < n; ++i) {
            String al = scanner.nextLine();
            String[] parts = al.split(";");
            airports.addAirport(parts[0], parts[1], parts[2], Integer.parseInt(parts[3]));
            codes[i] = parts[2];
        }
        int nn = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < nn; ++i) {
            String fl = scanner.nextLine();
            String[] parts = fl.split(";");
            airports.addFlights(parts[0], parts[1], Integer.parseInt(parts[2]), Integer.parseInt(parts[3]));
        }
        int f = scanner.nextInt();
        int t = scanner.nextInt();
        String from = codes[f];
        String to = codes[t];
        System.out.printf("===== FLIGHTS FROM %S =====\n", from);
        airports.showFlightsFromAirport(from);
        System.out.printf("===== DIRECT FLIGHTS FROM %S TO %S =====\n", from, to);
        airports.showDirectFlightsFromTo(from, to);
        t += 5;
        t = t % n;
        to = codes[t];
        System.out.printf("===== DIRECT FLIGHTS TO %S =====\n", to);
        airports.showDirectFlightsTo(to);
    }
}

// vashiot kod ovde


