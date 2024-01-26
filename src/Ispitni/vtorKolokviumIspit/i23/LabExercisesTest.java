package Ispitni.vtorKolokviumIspit.i23;

import java.util.*;
import java.util.stream.Collectors;

class Student{
    String index;
    List<Integer> points;

    public Student(String index, List<Integer> points) {
        this.index = index;
        this.points = points;
    }

    double getAveragePoints(){
        double sum =0;
        for (Integer point : points) {
            sum+=point;
        }
        return sum/10.0;
    }

    public int getYear(){
        return 20 - Integer.parseInt(index.substring(0, 2));
    }

    public String getIndex() {
        return index;
    }

    public List<Integer> getPoints() {
        return points;
    }

    String hasEnoughForSignature(){
        return points.size() >= 8 ? "YES" : "NO";
    }

    @Override
    public String toString() {
        return String.format("%s %s %.2f", index, hasEnoughForSignature(), getAveragePoints());
    }
}

class LabExercises{
    List<Student> students;

    public LabExercises() {
        students = new ArrayList<>();
    }

    public void addStudent (Student student){
        students.add(student);
    }
    public void printByAveragePoints (boolean ascending, int n){
        Comparator<Student> comparator = Comparator.comparingDouble(Student::getAveragePoints).thenComparing(Student::getIndex);
        if(!ascending){
            comparator = comparator.reversed();
        }

        students.stream().sorted(comparator).limit(n).forEach(System.out::println);
    }
    public List<Student> failedStudents (){
        return students.stream().filter(i -> i.hasEnoughForSignature().equals("NO")).sorted(Comparator.comparing(Student::getIndex).thenComparing(Student::getAveragePoints)).collect(Collectors.toList());
    }
    public Map<Integer,Double> getStatisticsByYear(){
        return students
                .stream()
                .filter(i -> i.hasEnoughForSignature().equals("YES"))
                .collect(Collectors.groupingBy(Student::getYear,Collectors.averagingDouble(Student::getAveragePoints)));
    }
}
public class LabExercisesTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LabExercises labExercises = new LabExercises();
        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            String[] parts = input.split("\\s+");
            String index = parts[0];
            List<Integer> points = Arrays.stream(parts).skip(1)
                    .mapToInt(Integer::parseInt)
                    .boxed()
                    .collect(Collectors.toList());

            labExercises.addStudent(new Student(index, points));
        }

        System.out.println("===printByAveragePoints (ascending)===");
        labExercises.printByAveragePoints(true, 100);
        System.out.println("===printByAveragePoints (descending)===");
        labExercises.printByAveragePoints(false, 100);
        System.out.println("===failed students===");
        labExercises.failedStudents().forEach(System.out::println);
        System.out.println("===statistics by year");
        labExercises.getStatisticsByYear().entrySet().stream()
                .map(entry -> String.format("%d : %.2f", entry.getKey(), entry.getValue()))
                .forEach(System.out::println);

    }
}
