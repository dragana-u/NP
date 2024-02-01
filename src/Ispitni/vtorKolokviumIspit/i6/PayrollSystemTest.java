package Ispitni.vtorKolokviumIspit.i6;



import java.io.*;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

class PayrollSystem {
    List<Employee> employees;
    Map<String, Double> hourlyRateByLevel;
    Map<String, Double> ticketRateByLevel;

    PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        employees = new ArrayList<>();
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
    }

    void readEmployees(InputStream is) {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        employees = br.lines().map(line -> EmployeeFactory.createEmployee(line, hourlyRateByLevel, ticketRateByLevel)).collect(Collectors.toList());
    }

    public Map<String, Set<Employee>> printEmployeesByLevels(OutputStream os, Set<String> levels) {
        return employees
                .stream()
                .filter(employee -> levels.contains(employee.level))
                .collect(Collectors.groupingBy(
                        Employee::getLevel,
                        TreeMap::new,
                        Collectors.toCollection(TreeSet::new)
                ));
    }
}

class EmployeeFactory {
    public static Employee createEmployee(String line, Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        //F;72926a;level7;5;6;8;1
        String[] parts = line.split(";");
        if (parts[0].equals("F")) {
            String iD = parts[1];
            String level = parts[2];
            List<Integer> points = new ArrayList<>();
            for (int i = 3; i < parts.length; i++) {
                points.add(Integer.parseInt(parts[i]));
            }
            return new FreelanceEmployee(iD, level, ticketRateByLevel.get(level), points);
        } else {
            //H;157f3d;level10;63.14
            String iD = parts[1];
            String level = parts[2];
            double hours = Double.parseDouble(parts[3]);
            return new HourlyEmployee(iD, level, hourlyRateByLevel.get(level), hours);
        }
    }
}

abstract class Employee implements Comparable<Employee>{
    String ID;
    String level;
    double rate;

    public Employee(String ID, String level, double rate) {
        this.ID = ID;
        this.level = level;
        this.rate = rate;
    }

    public String getLevel() {
        return level;
    }

    abstract double calculateSalary();
    @Override
    public int compareTo(Employee o) {
        return Comparator.comparing(Employee::calculateSalary).thenComparing(Employee::getLevel).reversed().compare(this, o);
    }
    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f ", ID, level, calculateSalary());
    }
}

class FreelanceEmployee extends Employee {
    List<Integer> ticketPoints;

    public FreelanceEmployee(String ID, String level, double rate, List<Integer> ticketPoints) {
        super(ID, level, rate);
        this.ticketPoints = ticketPoints;
    }

    @Override
    double calculateSalary() {
        return ticketPoints.stream().mapToInt(ticket -> ticket).sum() * rate;
    }

    int ticketCount() {
        return ticketPoints.size();
    }

    int ticketPoints() {
        return ticketPoints.stream().mapToInt(ticket -> ticket).sum();
    }

    @Override
    public String toString() {
        return String.format("%sTickets count: %d Tickets points: %d", super.toString(), ticketCount(), ticketPoints());
    }
}

class HourlyEmployee extends Employee {
    double hours;
    double overtime;
    double regular;

    public HourlyEmployee(String ID, String level, double rate, double hours) {
        super(ID, level, rate);
        this.hours = hours;
        this.overtime = Math.max(0, hours - 40);
        this.regular = hours - overtime;
    }

    @Override
    double calculateSalary() {
        return regular * rate + overtime * rate * 1.5;
    }

    @Override
    public String toString() {
        return String.format("%sRegular hours: %.2f Overtime hours: %.2f",super.toString(),regular,overtime);
    }

}

public class PayrollSystemTest {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 10 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5 + i * 2.5);
        }

        PayrollSystem payrollSystem = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);

        System.out.println("READING OF THE EMPLOYEES DATA");
        payrollSystem.readEmployees(System.in);

        System.out.println("PRINTING EMPLOYEES BY LEVEL");
        Set<String> levels = new LinkedHashSet<>();
        for (int i = 5; i <= 10; i++) {
            levels.add("level" + i);
        }
        Map<String, Set<Employee>> result = payrollSystem.printEmployeesByLevels(System.out, levels);
        result.forEach((level, employees) -> {
            System.out.println("LEVEL: " + level);
            System.out.println("Employees: ");
            employees.forEach(System.out::println);
            System.out.println("------------");
        });

    }
}
