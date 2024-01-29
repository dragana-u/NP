package Ispitni.vtorKolokviumIspit.i31;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class Student{
    String index;
    String ime;
    int firstMidterm;
    int secondMidterm;
    int laboratory;

    public Student(String index, String ime, int firstMidterm, int secondMidterm, int laboratory) {
        this.index = index;
        this.ime = ime;
        this.firstMidterm = firstMidterm;
        this.secondMidterm = secondMidterm;
        this.laboratory = laboratory;
    }

    public Student(String id, String name) {
        this.ime = name;
        this.index = id;
        this.firstMidterm=0;
        this.secondMidterm=0;
        this.laboratory=0;
    }

    public void setFirstMidterm(int firstMidterm) {
        this.firstMidterm = firstMidterm;
    }

    public void setSecondMidterm(int secondMidterm) {
        this.secondMidterm = secondMidterm;
    }

    public void setLaboratory(int laboratory) {
        this.laboratory = laboratory;
    }
    public double sumarni(){
        return firstMidterm * 0.45 + secondMidterm * 0.45 + laboratory;
    }
    public int calculateGrade(){
        if(sumarni()<50){
            return 5;
        }else if(sumarni()>=50 && sumarni()<60){
            return 6;
        }else if(sumarni()>=60 && sumarni()<70){
            return 7;
        }else if(sumarni()>=70 && sumarni()<80){
            return 8;
        }else if(sumarni()>=80 && sumarni()<90){
            return 9;
        }else{
            return 10;
        }
    }
    @Override
    public String toString() {
        return String.format("ID: %s Name: %s First midterm: %d Second midterm %d Labs: %d Summary points: %.2f Grade: %d"
                ,index,ime,firstMidterm,secondMidterm,laboratory,sumarni(),calculateGrade());
    }
}

class InvalidPoints extends Exception{
    public InvalidPoints() {
    }
}

class AdvancedProgrammingCourse{
    Map<String,Student> students;
    List<Student> best;

    public AdvancedProgrammingCourse() {
        students = new HashMap<>();
        best = new ArrayList<>();
    }
    public void addStudent (Student s){
        Student student = new Student(s.index,s.ime,s.firstMidterm,s.secondMidterm,s.laboratory);
        students.putIfAbsent(s.index,student);
        best.add(student);
    }
    public void updateStudent (String idNumber, String activity, int points) throws InvalidPoints {
        Student st = students.get(idNumber);
        if(points<0 || points>100 || st==null){
            throw new InvalidPoints();
        }
        if(activity.equalsIgnoreCase("midterm1")){
            st.setFirstMidterm(points);
        }else if(activity.equalsIgnoreCase("midterm2")){
            st.setSecondMidterm(points);
        }else if(activity.equalsIgnoreCase("labs")){
            if(points>10) throw new InvalidPoints();
            st.setLaboratory(points);
        }
    }

    public List<Student> getFirstNStudents (int n){
        return best.stream().sorted(Comparator.comparing(Student::sumarni).reversed()).limit(n).collect(Collectors.toList());
    }

    public int getNumGrades(int grade){
        List<Map.Entry<String, Student>> grades = students
                .entrySet()
                .stream().filter(i -> i.getValue().calculateGrade() == grade).collect(Collectors.toList());
        return grades.size();
    }
    public Map<Integer,Integer> getGradeDistribution(){
        Map<Integer,Integer> res = new TreeMap<>();
        res.put(5,getNumGrades(5));
        res.put(6,getNumGrades(6));
        res.put(7,getNumGrades(7));
        res.put(8,getNumGrades(8));
        res.put(9,getNumGrades(9));
        res.put(10,getNumGrades(10));
        return res;
    }
    public void printStatistics(){
        DoubleSummaryStatistics ds = best.stream().filter(i -> i.calculateGrade()>=6).mapToDouble(Student::sumarni).summaryStatistics();
        System.out.printf("Count: %d Min: %.2f Average: %.2f Max: %.2f\n",ds.getCount(),ds.getMin(),ds.getAverage(),ds.getMax());
    }
}

public class CourseTest {

    public static void printStudents(List<Student> students) {
        students.forEach(System.out::println);
    }

    public static void printMap(Map<Integer, Integer> map) {
        map.forEach((k, v) -> System.out.printf("%d -> %d%n", k, v));
    }

    public static void main(String[] args) {
        AdvancedProgrammingCourse advancedProgrammingCourse = new AdvancedProgrammingCourse();

        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            String[] parts = line.split("\\s+");

            String command = parts[0];

            if (command.equals("addStudent")) {
                String id = parts[1];
                String name = parts[2];
                advancedProgrammingCourse.addStudent(new Student(id, name));
            } else if (command.equals("updateStudent")) {
                String idNumber = parts[1];
                String activity = parts[2];
                int points = Integer.parseInt(parts[3]);
                try {
                    advancedProgrammingCourse.updateStudent(idNumber, activity, points);
                } catch (InvalidPoints ignored) {

                }
            } else if (command.equals("getFirstNStudents")) {
                int n = Integer.parseInt(parts[1]);
                printStudents(advancedProgrammingCourse.getFirstNStudents(n));
            } else if (command.equals("getGradeDistribution")) {
                printMap(advancedProgrammingCourse.getGradeDistribution());
            } else {
                advancedProgrammingCourse.printStatistics();
            }
        }
    }
}

