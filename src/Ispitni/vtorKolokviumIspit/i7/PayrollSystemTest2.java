package Ispitni.vtorKolokviumIspit.i7;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

class BonusNotAllowedException extends Exception{
    public BonusNotAllowedException(String message) {
        super(message);
    }
}

class PayrollSystem{
    Map<String,Double> hourlyRateByLevel;
    Map<String,Double> ticketRateByLevel;
    List<Employee> employees;
    Map<String,Set<Employee>> overtimePay;

    public PayrollSystem(Map<String, Double> hourlyRateByLevel, Map<String, Double> ticketRateByLevel) {
        this.hourlyRateByLevel = hourlyRateByLevel;
        this.ticketRateByLevel = ticketRateByLevel;
        employees = new ArrayList<>();
        overtimePay = new HashMap<>();
    }
    Employee createEmployee (String line) throws BonusNotAllowedException {
        //H;ID;level;hours; 100
        //F;ID;level;ticketPoints1;ticketPoints2;...;ticketPointsN; 10%
        String[] parts = line.split("\\s+");
        double fixBonus=0,procentBonus=0;
        String[] fix = parts[0].split(";");
        String type = fix[0];
        String id = fix[1];
        String level = fix[2];
        if(parts.length==2){
            if(parts[1].contains("%")){
                procentBonus=Double.parseDouble(parts[1].replace("%",""));
                if(procentBonus>20){
                    throw new BonusNotAllowedException(String.format("Bonus of %.2f%% is not allowed",procentBonus));
                }
            }else{
                fixBonus = Double.parseDouble(parts[1]);
                if(fixBonus>1000){
                    throw new BonusNotAllowedException(String.format("Bonus of %.0f$ is not allowed",fixBonus));
                }
            }
        }
        if(type.equals("F")){
            List<Integer> points = new ArrayList<>();
            for (int i = 3; i < fix.length; i++) {
                points.add(Integer.parseInt(fix[i]));
            }
            FreelanceEmployee e = new FreelanceEmployee(id,level,ticketRateByLevel.get(level),procentBonus,fixBonus,points);
            employees.add(e);
          return e;
        }else{
            HourlyEmployee e = new HourlyEmployee(id,level,hourlyRateByLevel.get(level),procentBonus,fixBonus,Double.parseDouble(fix[3]));
            Set<Employee> overtime = overtimePay.get(level);
            if(overtime==null){
                overtime = new HashSet<>();
            }
            overtime.add(e);
            overtimePay.putIfAbsent(level,overtime);
            employees.add(e);
            return e;
        }
    }
    double calculateOvertimeByLevel(String level){
        return overtimePay
                .get(level)
                .stream()
                .flatMapToDouble(i -> DoubleStream.of(i.getOvertimePay()))
                .sum();

    }
    Map<String, Double> getOvertimeSalaryForLevels (){
//        List<String> levels = overtimePay.keySet().stream().collect(Collectors.toList());
//        Map<String,Double> res = new HashMap<>();
//        for (String level : levels) {
//            res.putIfAbsent(level,calculateOvertimeByLevel(level));
//        }
//        return res;
        Map<String, Double> result = employees.stream().collect(Collectors.groupingBy(
                Employee::getLevel,
                Collectors.summingDouble(Employee::getOvertimePay)
        ));
        List<String> keysWithZeros = result.keySet().stream().filter(key -> result.get(key) == -1).collect(Collectors.toList());
        keysWithZeros.forEach(result::remove);
        return result;
    }
    void printStatisticsForOvertimeSalary (){
        Map<String, Double> overtimeSalaryForLevels = getOvertimeSalaryForLevels();
        DoubleSummaryStatistics ds = overtimeSalaryForLevels.values().stream().mapToDouble(i -> i).summaryStatistics();
        System.out.printf("Statistics for overtime salary: Min: %.2f Average: %.2f Max: %.2f Sum: %.2f\n",ds.getMin(),ds.getAverage(),ds.getMax(),ds.getSum());
    }
    Map<String, Integer> ticketsDoneByLevel(){
        Map<String, Integer> collect = employees.stream().collect(Collectors.groupingBy(Employee::getLevel, Collectors.summingInt(Employee::ticketCount)));
        List<String> keysWithZeros = collect.keySet().stream().filter(key -> collect.get(key) == -1).collect(Collectors.toList());;
        keysWithZeros.forEach(collect::remove);
        return collect;
    }
    Collection<Employee> getFirstNEmployeesByBonus (int n){
        return employees.stream().sorted(Comparator.comparing(Employee::getBonus).reversed()).limit(n).collect(Collectors.toList());
    }
}

abstract class Employee{
    String id;
    String level;
    double rate;
    double procentBonus;
    double fixBonus;

    public Employee(String id, String level, double rate, double procentBonus, double fixBonus) {
        this.id = id;
        this.level = level;
        this.rate = rate;
        this.procentBonus = procentBonus;
        this.fixBonus = fixBonus;
    }
    abstract double calculateBaseSalary();
    abstract double getSalaryWithBonus();
    abstract String getType();
    abstract double getOvertimePay();
    abstract double getBonus();
    abstract int ticketCount();
    @Override
    public String toString() {
        return String.format("Employee ID: %s Level: %s Salary: %.2f ", id, level, getSalaryWithBonus());
    }

    public String getId() {
        return id;
    }

    public String getLevel() {
        return level;
    }

    public double getRate() {
        return rate;
    }

    public double getProcentBonus() {
        return procentBonus;
    }

    public double getFixBonus() {
        return fixBonus;
    }
}

class HourlyEmployee extends Employee{
    double hours;
    double overtime;
    double regular;

    public HourlyEmployee(String id, String level, double rate, double procentBonus, double fixBonus, double hours) {
        super(id, level, rate, procentBonus, fixBonus);
        this.hours = hours;
        this.overtime = Math.max(0,hours-40);
        this.regular = hours - overtime;
    }

    @Override
    double calculateBaseSalary() {
        return regular * rate + overtime * rate * 1.5;
    }

    @Override
    double getSalaryWithBonus() {
       double procent = calculateBaseSalary()*(procentBonus/100);
       return calculateBaseSalary() + procent + fixBonus;
    }

    @Override
    String getType() {
        return "H";
    }

    @Override
    public String toString() {
        String s = "";
        if(fixBonus!=0){
            s = s.concat(String.format(" Bonus: %.2f",fixBonus));
        }
        if(procentBonus!=0){
            s = s.concat(String.format(" Bonus: %.2f",calculateBaseSalary()*(procentBonus/100.0)));
        }
        return String.format("%sRegular hours: %.2f Overtime hours: %.2f%s",super.toString(),regular,overtime,s);
    }
    double getOvertimePay(){
        return overtime*rate*1.5;
    }

    @Override
    double getBonus() {
        if(fixBonus!=0){
            return fixBonus;
        }else if (procentBonus!=0){
            return calculateBaseSalary()*(procentBonus/100);
        }
        return 0;
    }

    @Override
    int ticketCount() {
        return -1;
    }
}

class FreelanceEmployee extends Employee{
    List<Integer> ticketPoints;

    public FreelanceEmployee(String id, String level, double rate, double procentBonus,double fixBonus, List<Integer> ticketPoints) {
        super(id, level, rate, procentBonus, fixBonus);
        this.ticketPoints = ticketPoints;
    }

    @Override
    double calculateBaseSalary() {
        return ticketPoints.stream().mapToInt(ticket -> ticket).sum() * rate;
    }

    @Override
    double getSalaryWithBonus() {
        double procent = calculateBaseSalary()*(procentBonus/100);
        return calculateBaseSalary() + procent + fixBonus;
    }

    @Override
    String getType() {
        return "F";
    }

    @Override
    double getOvertimePay() {
        return -1;
    }

    @Override
    int ticketCount() {
        return ticketPoints.size();
    }

    int ticketPoints() {
        return ticketPoints.stream().mapToInt(ticket -> ticket).sum();
    }
    @Override
    double getBonus() {
        if(fixBonus!=0){
            return fixBonus;
        }else if (procentBonus!=0){
            return calculateBaseSalary()*(procentBonus/100);
        }
        return 0;
    }
    @Override
    public String toString() {
        String s = "";
        if(fixBonus!=0){
            s = s.concat(String.format(" Bonus: %.2f",fixBonus));
        }
        if(procentBonus!=0){
            s = s.concat(String.format(" Bonus: %.2f",calculateBaseSalary()*(procentBonus/100.0)));
        }
        return String.format("%sTickets count: %d Tickets points: %d%s", super.toString(), ticketCount(), ticketPoints(),s);
    }

}

public class PayrollSystemTest2 {

    public static void main(String[] args) {

        Map<String, Double> hourlyRateByLevel = new LinkedHashMap<>();
        Map<String, Double> ticketRateByLevel = new LinkedHashMap<>();
        for (int i = 1; i <= 10; i++) {
            hourlyRateByLevel.put("level" + i, 11 + i * 2.2);
            ticketRateByLevel.put("level" + i, 5.5 + i * 2.5);
        }

        Scanner sc = new Scanner(System.in);

        int employeesCount = Integer.parseInt(sc.nextLine());

        PayrollSystem ps = new PayrollSystem(hourlyRateByLevel, ticketRateByLevel);
        Employee emp = null;
        for (int i = 0; i < employeesCount; i++) {
            try {
                emp = ps.createEmployee(sc.nextLine());
            } catch (BonusNotAllowedException e) {
                System.out.println(e.getMessage());
            }
        }

        int testCase = Integer.parseInt(sc.nextLine());

        switch (testCase) {
            case 1: //Testing createEmployee
                if (emp != null)
                    System.out.println(emp);
                break;
            case 2: //Testing getOvertimeSalaryForLevels()
                ps.getOvertimeSalaryForLevels().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Overtime salary: %.2f\n", level, overtimeSalary);
                });
                break;
            case 3: //Testing printStatisticsForOvertimeSalary()
                ps.printStatisticsForOvertimeSalary();
                break;
            case 4: //Testing ticketsDoneByLevel
                ps.ticketsDoneByLevel().forEach((level, overtimeSalary) -> {
                    System.out.printf("Level: %s Tickets by level: %d\n", level, overtimeSalary);
                });
                break;
            case 5: //Testing getFirstNEmployeesByBonus (int n)
                ps.getFirstNEmployeesByBonus(Integer.parseInt(sc.nextLine())).forEach(System.out::println);
                break;
        }

    }
}
