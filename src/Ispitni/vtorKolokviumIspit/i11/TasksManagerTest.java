package Ispitni.vtorKolokviumIspit.i11;


import java.io.*;
import java.text.DateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

class DeadlineNotValidException extends Exception{
    public DeadlineNotValidException(String message) {
        super(message);
    }
}

class TaskManager{
    List<TaskFactory> tasks;

    public TaskManager() {
        tasks = new ArrayList<>();
    }

    void readTasks (InputStream inputStream){
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        tasks = br.lines().map(line -> {
            try {
                return new TaskFactory(line);
            } catch (DeadlineNotValidException e) {
                System.out.println(e.getMessage());
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());
    }
    void printTasks(OutputStream os, boolean includePriority, boolean includeCategory){
        PrintWriter pw = new PrintWriter(os);
        if(includePriority){
            tasks = tasks.stream().sorted(Comparator.comparing(TaskFactory::getPriority).thenComparing(TaskFactory::getLocalDateTime)).collect(Collectors.toList());
        }else{
            tasks = tasks.stream().sorted(Comparator.comparing(TaskFactory::getLocalDateTime)).collect(Collectors.toList());
        }
        if(includeCategory){
           Map<String,List<TaskFactory>> t = tasks.stream().collect(Collectors.groupingBy(TaskFactory::getCategory));
            for (Map.Entry<String, List<TaskFactory>> stringListEntry : t.entrySet()) {
                pw.println(stringListEntry.getKey().toUpperCase());
                for (TaskFactory tf : stringListEntry.getValue()) {
                    pw.printf(tf.toString());
                }
            }
        }else {
            tasks.forEach(t -> pw.printf(t.toString()));
        }
        pw.flush();
    }
}

class TaskFactory{
    String category;
    String name;
    String description;
    LocalDateTime localDateTime;
    int priority;
    boolean deadline;

    public TaskFactory(String line) throws DeadlineNotValidException {
        //School,NP,lab 1 po NP,2020-06-23T23:59:59.000,1
        String[] parts = line.split(",");
        this.category = parts[0];
        this.name = parts[1];
        this.description = parts[2];
        boolean flag=true;
        deadline=true;
        if(parts.length>=4) {
            if (parts[3].length() == 23) {
                LocalDateTime ld = LocalDateTime.parse(parts[3]);
                if (ld.isBefore(LocalDateTime.parse("2020-06-02T00:00:00"))) {
                    throw new DeadlineNotValidException(String.format("The deadline %s has already passed", ld));
                }
                this.localDateTime = ld;
            } else {
                localDateTime = LocalDateTime.now();
                deadline = false;
                this.priority = Integer.parseInt(parts[3]);
                flag = false;
            }
        }else{
            localDateTime=LocalDateTime.now();
            deadline=false;
        }
        if(parts.length==5){
            this.priority=Integer.parseInt(parts[4]);
        }else if(flag){
            priority=10000;
        }
    }

    @Override
    public String toString() {
        String s = String.format("Task{name='%s', description='%s'",name,description);
        if(deadline){
            s = s.concat(", deadline=");
            s = s.concat(localDateTime.toString());
        }
        if(priority!=10000){
            s = s.concat(String.format(", priority=%d}\n",priority));
        }else{
            s = s.concat("}\n");
        }
        return s;
    }

    public String getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getLocalDateTime() {
        return localDateTime;
    }

    public int getPriority() {
        return priority;
    }
}

public class TasksManagerTest {

    public static void main(String[] args) {

        TaskManager manager = new TaskManager();

        System.out.println("Tasks reading");
        manager.readTasks(System.in);
        System.out.println("By categories with priority");
        manager.printTasks(System.out, true, true);
        System.out.println("-------------------------");
        System.out.println("By categories without priority");
        manager.printTasks(System.out, false, true);
        System.out.println("-------------------------");
        System.out.println("All tasks without priority");
        manager.printTasks(System.out, false, false);
        System.out.println("-------------------------");
        System.out.println("All tasks with priority");
        manager.printTasks(System.out, true, false);
        System.out.println("-------------------------");

    }
}

