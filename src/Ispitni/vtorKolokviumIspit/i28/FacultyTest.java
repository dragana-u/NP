package Ispitni.vtorKolokviumIspit.i28;

//package mk.ukim.finki.vtor_kolokvium;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class  OperationNotAllowedException extends Exception{
    public OperationNotAllowedException(String message) {
        super(message);
    }
}

class Faculty {

    Map<String,Student> students;
    List<String> logs;
    Map<String,Course> courses;

    public Faculty() {
        students = new HashMap<>();
        logs = new ArrayList<>();
        courses = new TreeMap<>();
    }

    void addStudent(String id, int yearsOfStudies) {
        students.putIfAbsent(id,new Student(id,yearsOfStudies));
    }

    void addGradeToStudent(String studentId, int term, String courseName, int grade) throws OperationNotAllowedException {
        Student s = students.get(studentId);
        if(s==null) return;
        s.addGradeToStudent(term, courseName, grade);
        Course course = courses.get(courseName);
        if(course == null){
            course = new Course(courseName);
        }
        course.addGrade(grade);
        courses.putIfAbsent(courseName,course);
        if(s.yearsOfStudies==3){
            if(s.getCourseCount()==18){
                logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.\n",
                        s.id,s.getAverageGrade(),s.yearsOfStudies));
                s.setGraduated(true);
                students.remove(s.id);
            }
        }else if(s.yearsOfStudies==4){
            if(s.getCourseCount()==24){
                logs.add(String.format("Student with ID %s graduated with average grade %.2f in %d years.\n",
                        s.id,s.getAverageGrade(),s.yearsOfStudies));
                s.setGraduated(true);
                students.remove(s.id);
            }
        }
    }

    String getFacultyLogs() {
        StringBuilder sb = new StringBuilder();
        logs.forEach(sb::append);
        sb.deleteCharAt(sb.lastIndexOf("\n"));
        return sb.toString();
    }

    String getDetailedReportForStudent(String id) {
        return students.get(id).toString();
    }

    void printFirstNStudents(int n) {
        List<Student> collect = students.values().stream().filter(i -> !i.graduated).sorted(Comparator.comparing(Student::getCourseCount).thenComparing(Student::getAverageGrade).thenComparing(Student::getId).reversed()).limit(n).collect(Collectors.toList());
        collect.forEach(s -> System.out.printf("Student: %s Courses passed: %d Average grade: %.2f\n",s.id,s.getCourseCount(),s.getAverageGrade()));
    }

    void printCourses() {
        Comparator<Course> c = Comparator.comparing(Course::getStudentCount).thenComparing(Course::getAverageGrade);
        courses.values().stream().sorted(c).forEach(v -> System.out.printf("%s %d %.2f\n",v.ime,v.studentCount,v.getAverageGrade()));
    }
}

class Course{
    String ime;
    int studentCount;
    List<Integer> grades;

    public Course(String ime) {
        this.ime = ime;
        studentCount=0;
        grades = new ArrayList<>();
    }

    public int getStudentCount() {
        return studentCount;
    }
    public void addGrade(int grade){
        grades.add(grade);
        studentCount++;
    }
    public double getAverageGrade(){
        return (double) grades.stream().mapToInt(i -> i).sum() /grades.size();
    }
}

class Student{
    String id;
    int yearsOfStudies;
    TreeMap<Integer,TreeMap<String,Integer>> grades; // term -> (courseName -> grade)
    boolean graduated;

    public Student(String id, int yearsOfStudies) {
        grades = new TreeMap<>();
        this.id = id;
        this.yearsOfStudies = yearsOfStudies;
        graduated = false;
    }
    void addGradeToStudent(int term, String courseName, int grade) throws OperationNotAllowedException {
        TreeMap<String, Integer> t = grades.get(term);
        if(t == null){
            t = new TreeMap<>();
        }
        if(yearsOfStudies==3){
            if(term>6){
                throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s",term,id));
            }
        }else{//4
            if(term>8){
                throw new OperationNotAllowedException(String.format("Term %d is not possible for student with ID %s",term,id));
            }
        }
        if(t.size()==3){
            throw new OperationNotAllowedException(String.format("Student %s already has 3 grades in term %d",id,term));
        }
        t.putIfAbsent(courseName,grade);
        grades.putIfAbsent(term,t);
    }

    public String getId() {
        return id;
    }

    public void setGraduated(boolean graduated) {
        this.graduated = graduated;
    }

    public double averageGradeByTerm(int term){
        double sum = 0;
        TreeMap<String, Integer> t = grades.get(term);
        for (Integer value : t.values()) {
            sum+=value;
        }
        return sum/t.size();
    }
    public int getCoursesForTermCount(int term){
        TreeMap<String, Integer> t = grades.get(term);
        return t.size();
    }

    public int getCourseCount(){
        int courses = 0;
        for (Integer i : grades.keySet()) {
            courses+=getCoursesForTermCount(i);
        }
        return courses;
    }

    public double getAverageGrade(){
        if(getCourseCount()==0){
            return 5.00;
        }
        double sum=0;
        int terms =0;
        for (Integer i : grades.keySet()) {
            sum+=averageGradeByTerm(i);
            terms++;
        }
        return sum/terms;
    }

    String addTermWithZeroCourses(int term){
        return String.format("Term: %d\nCourses: 0\nAverage grade for term: 5.00\n",
                term);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Student: %s\n",id));
        for (Integer i : grades.keySet()) {
            sb.append(String.format("Term %d\nCourses: %d\nAverage grade for term: %.2f\n",
                    i,
                    getCoursesForTermCount(i),
                    averageGradeByTerm(i)));

        }
        sb.append(addTermWithZeroCourses(yearsOfStudies==3 ? 6 : 8));
        sb.append(String.format("Average grade: %.2f\n",getAverageGrade()));
        sb.append("Courses attended: ");
        grades.values().forEach(i -> i.keySet().forEach(j -> sb.append(String.format("%s,",j))));
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("\n");
        return sb.toString();
    }
}

public class FacultyTest {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int testCase = sc.nextInt();

        if (testCase == 1) {
            System.out.println("TESTING addStudent AND printFirstNStudents");
            Faculty faculty = new Faculty();
            for (int i = 0; i < 10; i++) {
                faculty.addStudent("student" + i, (i % 2 == 0) ? 3 : 4);
            }
            faculty.printFirstNStudents(10);

        } else if (testCase == 2) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            try {
                faculty.addGradeToStudent("123", 7, "NP", 10);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
            try {
                faculty.addGradeToStudent("1234", 9, "NP", 8);
            } catch (OperationNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        } else if (testCase == 3) {
            System.out.println("TESTING addGrade and exception");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("123", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
            for (int i = 0; i < 4; i++) {
                try {
                    faculty.addGradeToStudent("1234", 1, "course" + i, 10);
                } catch (OperationNotAllowedException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else if (testCase == 4) {
            System.out.println("Testing addGrade for graduation");
            Faculty faculty = new Faculty();
            faculty.addStudent("123", 3);
            faculty.addStudent("1234", 4);
            int counter = 1;
            for (int i = 1; i <= 6; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("123", i, "course" + counter, (i % 2 == 0) ? 7 : 8);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            counter = 1;
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 3; j++) {
                    try {
                        faculty.addGradeToStudent("1234", i, "course" + counter, (j % 2 == 0) ? 7 : 10);
                    } catch (OperationNotAllowedException e) {
                        System.out.println(e.getMessage());
                    }
                    ++counter;
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("PRINT STUDENTS (there shouldn't be anything after this line!");
            faculty.printFirstNStudents(2);
        } else if (testCase == 5 || testCase == 6 || testCase == 7) {
            System.out.println("Testing addGrade and printFirstNStudents (not graduated student)");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), i % 5 + 6);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            if (testCase == 5)
                faculty.printFirstNStudents(10);
            else if (testCase == 6)
                faculty.printFirstNStudents(3);
            else
                faculty.printFirstNStudents(20);
        } else if (testCase == 8 || testCase == 9) {
            System.out.println("TESTING DETAILED REPORT");
            Faculty faculty = new Faculty();
            faculty.addStudent("student1", ((testCase == 8) ? 3 : 4));
            int grade = 6;
            int counterCounter = 1;
            for (int i = 1; i < ((testCase == 8) ? 6 : 8); i++) {
                for (int j = 1; j < 3; j++) {
                    try {
                        faculty.addGradeToStudent("student1", i, "course" + counterCounter, grade);
                    } catch (OperationNotAllowedException e) {
                        e.printStackTrace();
                    }
                    grade++;
                    if (grade == 10)
                        grade = 5;
                    ++counterCounter;
                }
            }
            System.out.println(faculty.getDetailedReportForStudent("student1"));
        } else if (testCase==10) {
            System.out.println("TESTING PRINT COURSES");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j < ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 3 : 2); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            faculty.printCourses();
        } else if (testCase==11) {
            System.out.println("INTEGRATION TEST");
            Faculty faculty = new Faculty();
            for (int i = 1; i <= 10; i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= ((j % 2 == 1) ? 2 : 3); k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }

            }

            for (int i=11;i<15;i++) {
                faculty.addStudent("student" + i, ((i % 2) == 1 ? 3 : 4));
                int courseCounter = 1;
                for (int j = 1; j <= ((i % 2 == 1) ? 6 : 8); j++) {
                    for (int k = 1; k <= 3; k++) {
                        int grade = sc.nextInt();
                        try {
                            faculty.addGradeToStudent("student" + i, j, ("course" + courseCounter), grade);
                        } catch (OperationNotAllowedException e) {
                            System.out.println(e.getMessage());
                        }
                        ++courseCounter;
                    }
                }
            }
            System.out.println("LOGS");
            System.out.println(faculty.getFacultyLogs());
            System.out.println("DETAILED REPORT FOR STUDENT");
            System.out.println(faculty.getDetailedReportForStudent("student2"));
            try {
                System.out.println(faculty.getDetailedReportForStudent("student11"));
                System.out.println("The graduated students should be deleted!!!");
            } catch (NullPointerException e) {
                System.out.println("The graduated students are really deleted");
            }
            System.out.println("FIRST N STUDENTS");
            faculty.printFirstNStudents(10);
            System.out.println("COURSES");
            faculty.printCourses();
        }
    }
}

