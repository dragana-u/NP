package Ispitni.vtorKolokviumIspit.i33;


import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class LogCollector{
    Map<String,Service> services;

    public LogCollector() {
        services = new HashMap<>();
    }

    void addLog (String log){
        Log l = Log.createLog(log);
        services.putIfAbsent(l.service,new Service(l.service));
        services.get(l.service).addLog(l);
    }
    void printServicesBySeverity(){
        services.values().stream().sorted(Comparator.comparing(Service::getAverageSeverity).reversed()).forEach(System.out::println);
    }
    Map<Integer, Long> getSeverityDistribution (String service, String microservice){
        Service s = services.get(service);
        Microservice m = s.microservices.get(microservice);
        List<Integer> severities = new ArrayList<>();
        if(m==null){
            severities = s.microservices.values().stream().flatMap(i -> i.logs.stream().map(Log::getSeverity)).collect(Collectors.toList());
        }else{
            severities = m.logs.stream().map(Log::getSeverity).collect(Collectors.toList());
        }
        return severities.stream().collect(Collectors.groupingBy(i -> i, TreeMap::new, Collectors.counting()));
    }
    void displayLogs(String service, String microservice, String order){
        Service s = services.get(service);
        Microservice m = s.microservices.get(microservice);
        Comparator<Log> byTime = Comparator.comparing(Log::getTimestamp);
        Comparator<Log> bySeverity = Comparator.comparing(Log::getSeverity).thenComparing(Log::getTimestamp);
        Comparator<Log> comparator;
        if(order.equalsIgnoreCase("NEWEST_FIRST")){
            comparator = byTime.reversed();
        }else if(order.equalsIgnoreCase("OLDEST_FIRST")){
            comparator = byTime;
        }else if(order.equalsIgnoreCase("MOST_SEVERE_FIRST")){
            comparator = bySeverity.reversed();
        }else{
            comparator = bySeverity;
        }
        if(m==null){
             s.microservices.values().stream().flatMap(ms -> ms.logs.stream()).sorted(comparator).forEach(System.out::println);
        }else{
            m.logs.stream().sorted(comparator).forEach(System.out::println);
        }
    }
}
abstract class Log{
    String service;
    String microservice;
    String type;
    String message;
    long timestamp;

    public Log(String service, String microservice, String type, String message, long timestamp) {
        this.service = service;
        this.microservice = microservice;
        this.type = type;
        this.message = message;
        this.timestamp = timestamp;
    }

    abstract int getSeverity();
    public static Log createLog(String line){
        String[] parts = line.split("\\s+");
        String servName = parts[0];
        String microServName = parts[1];
        String type = parts[2];
        String mess = Arrays.stream(parts).skip(3).limit(parts.length - 1).collect(Collectors.joining(" "));
        long time = Long.parseLong(parts[parts.length - 1]);
        if(type.equalsIgnoreCase("INFO")){
            return new InfoLog(servName,microServName,type,mess,time);
        }else if(type.equalsIgnoreCase("WARN")){
            return new WarnLog(servName,microServName,type,mess,time);
        }else{
            return new ErrorLog(servName,microServName,type,mess,time);
        }
    }

    public String getService() {
        return service;
    }

    public String getMicroservice() {
        return microservice;
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        return String.format("%s|%s [%s] %s T:%d",service,microservice,type,message,timestamp);
    }
}

class InfoLog extends Log{

    public InfoLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int getSeverity() {
        return 0;
    }
}
class WarnLog extends Log{

    public WarnLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int getSeverity() {
        return message.toLowerCase().contains("might cause error") ? 2 : 1;
    }
}
class ErrorLog extends Log{

    public ErrorLog(String service, String microservice, String type, String message, long timestamp) {
        super(service, microservice, type, message, timestamp);
    }

    @Override
    int getSeverity() {
        int severity = 3;
        if(message.contains("fatal")){
            severity+=2;
        }
        if(message.contains("exception")){
            severity+=3;
        }
        return severity;
    }
}
class Service{
    String name;
    Map<String,Microservice> microservices;
    public Service(String name) {
        this.name = name;
        microservices = new HashMap<>();
    }

    public void addLog(Log log){
        microservices.putIfAbsent(log.microservice,new Microservice(log.microservice));
        microservices.get(log.microservice).addLog(log);
    }
    double getAverageSeverity(){
        return microservices.values().stream().flatMap(microservice -> microservice.logs.stream()).mapToInt(Log::getSeverity).average().orElse(0.0);
    }
    int getLogCount(){
        AtomicInteger m= new AtomicInteger();
        microservices.forEach((key, value) -> m.addAndGet(value.logsSize()));
        return m.intValue();
    }
    @Override
    public String toString() {
       return String.format("Service name: %s Count of microservices: %d Total logs in service: %d Average severity for all logs: %.2f Average number of logs per microservice: %.2f",
               name,microservices.size(),getLogCount(),getAverageSeverity(),getLogCount()/(float) microservices.size());
    }
}
class Microservice{
    String name;
    List<Log> logs;

    public Microservice(String name) {
        this.name = name;
        logs = new ArrayList<>();
    }
    int logsSize(){
       return logs.size();
    }
    void addLog(Log log){
        logs.add(log);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
public class LogsTester {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        LogCollector collector = new LogCollector();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            if (line.startsWith("addLog")) {
                collector.addLog(line.replace("addLog ", ""));
            } else if (line.startsWith("printServicesBySeverity")) {
                collector.printServicesBySeverity();
            } else if (line.startsWith("getSeverityDistribution")) {
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                if (parts.length == 3) {
                    microservice = parts[2];
                }
                collector.getSeverityDistribution(service, microservice).forEach((k,v)-> System.out.printf("%d -> %d%n", k,v));
            } else if (line.startsWith("displayLogs")){
                String[] parts = line.split("\\s+");
                String service = parts[1];
                String microservice = null;
                String order = null;
                if (parts.length == 4) {
                    microservice = parts[2];
                    order = parts[3];
                } else {
                    order = parts[2];
                }
                System.out.println(line);

                collector.displayLogs(service, microservice, order);
            }
        }
    }
}
