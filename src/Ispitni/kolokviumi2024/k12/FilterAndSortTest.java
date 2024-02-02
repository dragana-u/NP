package Ispitni.kolokviumi2024.k12;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

class Student implements Comparable<Student> {
    String id;
    List<Integer> grades;

    public Student(String id, List<Integer> grades) {
        this.id = id;
        this.grades = grades;
    }

    public double average() {
        return grades.stream().mapToDouble(i -> i).average().getAsDouble();
    }

    public int getYear() {
        return (24 - Integer.parseInt(id.substring(0, 2)));
    }

    public int totalCourses() {
        return Math.min(getYear() * 10, 40);
    }

    public double labAssistantPoints() {
        return average() * ((double) grades.size() / totalCourses()) * (0.8 + ((getYear()-1)*0.2)/3.0);
    }

    @Override
    public int compareTo(Student o) {
        return Comparator.comparing(Student::labAssistantPoints)
                .thenComparing(Student::average)
                .compare(this, o);
    }

    @Override
    public String toString() {
        return String.format("Student %s (%d year) - %d/%d passed exam, average grade %.2f.\nLab assistant points: %.2f", id, getYear(), grades.size(), totalCourses(), average(), labAssistantPoints());
    }
}

class FilterAndSort {
    static <T extends Comparable<T>> List<T> execute(List<T> list, Predicate<T> predicate) throws EmptyResultException {
        List<T> res = list.stream().filter(predicate).sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        if(res.isEmpty()){
            throw new EmptyResultException("No element met the criteria");
        }
        return res;
    }
}

class EmptyResultException extends Exception{
    public EmptyResultException(String message) {
        super(message);
    }
}
public class FilterAndSortTest {
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        int testCase = Integer.parseInt(sc.nextLine());
        int n = Integer.parseInt(sc.nextLine());

        if (testCase == 1) { // students
            int studentScenario = Integer.parseInt(sc.nextLine());
            List<Student> students = new ArrayList<>();
            while (n > 0) {

                String line = sc.nextLine();
                String[] parts = line.split("\\s+");
                String id = parts[0];
                List<Integer> grades = Arrays.stream(parts).skip(1).map(Integer::parseInt).collect(Collectors.toList());
                students.add(new Student(id, grades));
                --n;
            }

            if (studentScenario == 1) {
                //TODO filter and sort all students who have at least 8.0 points and are at least 3rd year student
                try {
                    students = FilterAndSort.execute(students,(i) -> i.labAssistantPoints() >= 8.0 && i.getYear()>=3);
                } catch (EmptyResultException e) {
                    System.out.println(e.getMessage());
                    return;
                }

            } else {
                //TODO filter and sort all students who have passed at least 90% of their total courses with an average grade of at least 9.0
                try {
                    students = FilterAndSort.execute(students, (i) -> (double) i.grades.size() /i.totalCourses()>0.9 && i.average()>=9.0);
                } catch (EmptyResultException e) {
                    System.out.println(e.getMessage());
                    return;
                }
            }
            students.forEach(System.out::println);
        } else { //integers
            List<Integer> integers = new ArrayList<>();
            while (n > 0) {
                integers.add(Integer.parseInt(sc.nextLine()));
                --n;
            }

            //TODO filter and sort all even numbers divisible with 15
            try {
                integers = FilterAndSort.execute(integers,(i) -> i%2==0 && i%15==0);
            } catch (EmptyResultException e) {
                System.out.println(e.getMessage());
                return;
            }
            integers.forEach(System.out::println);
        }

    }
}

