//package Ispitni.vtorKolokviumIspit.i6;
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.util.*;
//import java.util.stream.Collectors;
//
//class PayrollSystem{
//    List<Employee> employees;
//    Map<String,Double> hourlyRateByLevel;
//    Map<String,Double> ticketRateByLevel;
//
//    PayrollSystem(Map<String,Double> hourlyRateByLevel, Map<String,Double> ticketRateByLevel){
//        employees = new ArrayList<>();
//        this.hourlyRateByLevel = hourlyRateByLevel;
//        this.ticketRateByLevel = ticketRateByLevel;
//    }
//
//    void readEmployees (InputStream is){
//        BufferedReader br = new BufferedReader(new InputStreamReader(is));
//        employees = br.lines().map(line -> EmployeeFactory.createEmployee(line,hourlyRateByLevel,ticketRateByLevel)).collect(Collectors.toList());
//
//
//    }
//}
//
//class EmployeeFactory{
//    public static Employee createEmployee(String line,Map<String,Double> hourlyRateByLevel,Map<String,Double> ticketRateByLevel){
//
//    }
//}
//
//abstract class Employee{
//    String ID;
//    String level;
//    double rate;
//
//    public Employee(String ID, String level, double rate) {
//        this.ID = ID;
//        this.level = level;
//        this.rate = rate;
//    }
//}
//
//class FreelanceEmployee extends Employee{
//    List<Integer> ticketPoints;
//
//    public FreelanceEmployee(String ID, String level, double rate, List<Integer> ticketPoints) {
//        super(ID, level, rate);
//        this.ticketPoints = ticketPoints;
//    }
//}
//class HourlyEmployee extends Employee{
//
//}
//public class PayrollSystemTest {
//
//    public static void main(String[] args) {
//
//        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
//        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
//        for (int i = 1; i <= 10; i++) {
//            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
//            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
//        }
//
//        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
//
//        System.out.println("READING OF THE EMPLOYEES DATA");
//        payrollSystem.readEmployees(System.in);
//
//        System.out.println("PRINTING EMPLOYEES BY LEVEL");
//        Set<String> levels = new LinkedHashSet<>();
//        for (int i=5;i<=10;i++) {
//            levels.add("level"+i);
//        }
//        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
//        result.forEach((level, employees) -> {
//            System.out.println("LEVEL: "+ level);
//            System.out.println("Employees: ");
//            employees.forEach(System.out::println);
//        });
//
//
//    }
//}
