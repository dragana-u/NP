package Ispitni.vtorKolokviumIspit.i22;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class EventCalendarTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        int year = scanner.nextInt();
        scanner.nextLine();
        EventCalendar eventCalendar = new EventCalendar(year);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            String name = parts[0];
            String location = parts[1];
            LocalDateTime date = LocalDateTime.parse(parts[2], formatter);

            try {
                eventCalendar.addEvent(name, location, date);
            } catch (WrongDateException e) {
                System.out.println(e.getMessage());
            }
        }

        LocalDateTime date = LocalDateTime.parse(scanner.nextLine(), formatter);
        eventCalendar.listEvents(date);
        eventCalendar.listByMonth();
    }
}

class EventCalendar implements Comparable<EventCalendar>{
    String ime;
    String lokacija;
    LocalDateTime date;
    long year;
    List<EventCalendar> events;

    public EventCalendar(long year) {
        this.year = year;
        this.events = new ArrayList<>();
    }

    public EventCalendar(String ime, String lokacija, LocalDateTime date) {
        this.ime = ime;
        this.lokacija = lokacija;
        this.date = date;
    }

    public void addEvent(String name, String location, LocalDateTime date) throws WrongDateException {
        if(year!=date.getYear()){
            throw new WrongDateException(String.format("Wrong date: %s", createDateForExp(date)));
        }
        events.add(new EventCalendar(name,location,date));
    }
    public void listEvents(LocalDateTime dateParam){
        if(events.stream().noneMatch(i -> i.date.getDayOfMonth() == dateParam.getDayOfMonth() && i.date.getMonth() == dateParam.getMonth())){
            System.out.println("No events on this day!");
        }
        events
                .stream().filter(i -> i.date.getDayOfMonth() == dateParam.getDayOfMonth() && i.date.getMonth()==dateParam.getMonth())
                .sorted()
                .forEach(System.out::println);
    }
    int byMonth(int month){
        return events.stream().filter(i -> i.date.getMonthValue() == month).collect(Collectors.toList()).size();
    }
    public void listByMonth(){
        for(int i=1;i<=12;i++){
            System.out.printf("%d : %d\n",i,byMonth(i));
        }
    }
    String createDateForExp(LocalDateTime ldt){
        String week = String.valueOf(ldt.getDayOfWeek()).substring(0,3).toLowerCase();
        String week1 = week.substring(0,1).toUpperCase();
        week = week1+week.substring(1,3);
        String month = String.valueOf(ldt.getMonth()).substring(0,3).toLowerCase();
        String month1 = month.substring(0,1).toUpperCase();
        month = month1+month.substring(1,3);

        return String.format("%s %s %d %02d:%02d:%02d UTC %d",
                week,month, ldt.getDayOfMonth(),
                ldt.getHour(),ldt.getMinute(),ldt.getSecond(),ldt.getYear());
    }

    @Override
    public String toString() {
        String month = String.valueOf(date.getMonth()).substring(0,3).toLowerCase();
        String month1 = month.substring(0,1).toUpperCase();
        month = month1+month.substring(1,3);
        return String.format("%d %s, %d %02d:%02d at %s, %s",
                date.getDayOfMonth(),month,date.getYear(),date.getHour(),date.getMinute(),lokacija,ime);
    }

    @Override
    public int compareTo(EventCalendar o) {
        if(Duration.between(date,o.date).toSeconds()==0){
            return ime.compareTo(o.ime);
        }
        return (int) Duration.between(o.date,date).toSeconds();
    }
}

class WrongDateException extends Exception{
    public WrongDateException(String message) {
        super(message);
    }
}