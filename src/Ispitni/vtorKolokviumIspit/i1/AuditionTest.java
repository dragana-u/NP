package Ispitni.vtorKolokviumIspit.i1;


import java.util.*;
import java.util.stream.Collectors;

class Audition{

    HashMap<String,HashSet<Participant>> audition;

    public Audition() {
        audition = new HashMap<>();
    }

    void addParticpant(String city, String code, String name, int age){
       HashSet<Participant> participants = audition.get(city);
       if(participants==null){
           participants = new HashSet<>();
       }
       if(participants.stream().noneMatch(i -> i.code.equals(code))){
           participants.add(new Participant(code, name, age));
           audition.put(city,participants);
       }
    }
    void listByCity(String city){
        HashSet<Participant> byCity = audition.get(city);
        byCity = byCity.stream().sorted(Comparator.comparing(Participant::getName).thenComparing(Participant::getAge).thenComparing(Participant::getCode)).collect(Collectors.toCollection(LinkedHashSet::new));
        byCity.forEach(i -> System.out.println(i.code+ " " + i.name + " " + i.age));
    }

}

class Participant{
    String code;
    String name;
    int age;

    public Participant(String code, String name, int age) {
        this.code = code;
        this.name = name;
        this.age = age;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

}

public class AuditionTest {
    public static void main(String[] args) {
        Audition audition = new Audition();
        List<String> cities = new ArrayList<String>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            if (parts.length > 1) {
                audition.addParticpant(parts[0], parts[1], parts[2],
                        Integer.parseInt(parts[3]));
            } else {
                cities.add(line);
            }
        }
        for (String city : cities) {
            System.out.printf("+++++ %s +++++\n", city);
            audition.listByCity(city);
        }
        scanner.close();
    }
}