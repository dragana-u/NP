package Ispitni.vtorKolokviumIspit.i4;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

class FileSystem{
    Map<Character,Set<File>> files;

    public FileSystem() {
        files = new HashMap<>();
    }

    public void addFile(char folder, String name, int size, LocalDateTime createdAt){
        Set<File> f = files.get(folder);
        if(f==null){
            f = new TreeSet<>(Comparator.comparing(File::getVremeKreiranje).thenComparing(File::getIme).thenComparing(File::getGolemina));
        }
        f.add(new File(name,size,createdAt));
        files.putIfAbsent(folder,f);
    }
    public List<File> findAllHiddenFilesWithSizeLessThen(int size){
        List<File> res = new ArrayList<>();
        for (Set<File> value : files.values()) {
            for (File file : value) {
                if(file.ime.startsWith(".") && file.golemina<size){
                    res.add(file);
                }
            }
        }
        return res;
    }
    public int totalSizeOfFilesFromFolders(List<Character> folders){
        int sum=0;
        for (Character folder : folders) {
            Set<File> f = files.get(folder);
            for (File file : f) {
                sum+=file.golemina;
            }
        }
        return sum;
    }
        public Map<Integer, Set<File>> byYear() {
            return files.values().stream()
                    .flatMap(Set::stream)
                    .collect(Collectors.groupingBy(File::getYear, TreeMap::new, Collectors.toSet()));
        }
    public Map<String, Long> sizeByMonthAndDay(){
        return files
                .values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.groupingBy(i -> i.getMonth() + "-" + i.getDay(), Collectors.summingLong(File::getGolemina)));
    }
}

class File implements Comparable<File>{
    String ime;
    int golemina;
    LocalDateTime vremeKreiranje;

    public File(String ime, int golemina, LocalDateTime vremeKreiranje) {
        this.ime = ime;
        this.golemina = golemina;
        this.vremeKreiranje = vremeKreiranje;
    }

    public String getIme() {
        return ime;
    }

    public int getDay(){
        return vremeKreiranje.getDayOfMonth();
    }

    public String getMonth(){
        return vremeKreiranje.getMonth().toString();
    }

    public int getGolemina() {
        return golemina;
    }

    public LocalDateTime getVremeKreiranje() {
        return vremeKreiranje;
    }

    public int getYear(){
        return vremeKreiranje.getYear();
    }

    @Override
    public String toString() {
        return String.format("%-10s %5dB %s",ime,golemina,vremeKreiranje.toString());
    }

    @Override
    public int compareTo(File o) {
        return Comparator.comparing(File::getVremeKreiranje).thenComparing(File::getIme).thenComparing(File::getGolemina).compare(this,o);
    }
}

public class FileSystemTest {
    public static void main(String[] args) {
        FileSystem fileSystem = new FileSystem();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; i++) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            fileSystem.addFile(parts[0].charAt(0), parts[1],
                    Integer.parseInt(parts[2]),
                    LocalDateTime.of(2016, 12, 29, 0, 0, 0).minusDays(Integer.parseInt(parts[3]))
            );
        }
        int action = scanner.nextInt();
        if (action == 0) {
            scanner.nextLine();
            int size = scanner.nextInt();
            System.out.println("== Find all hidden files with size less then " + size);
            List<File> files = fileSystem.findAllHiddenFilesWithSizeLessThen(size);
            files.forEach(System.out::println);
        } else if (action == 1) {
            scanner.nextLine();
            String[] parts = scanner.nextLine().split(":");
            System.out.println("== Total size of files from folders: " + Arrays.toString(parts));
            int totalSize = fileSystem.totalSizeOfFilesFromFolders(Arrays.stream(parts)
                    .map(s -> s.charAt(0))
                    .collect(Collectors.toList()));
            System.out.println(totalSize);
        } else if (action == 2) {
            System.out.println("== Files by year");
            Map<Integer, Set<File>> byYear = fileSystem.byYear();
            byYear.keySet().stream().sorted()
                    .forEach(key -> {
                        System.out.printf("Year: %d\n", key);
                        Set<File> files = byYear.get(key);
                        files.stream()
                                .sorted()
                                .forEach(System.out::println);
                    });
        } else if (action == 3) {
            System.out.println("== Size by month and day");
            Map<String, Long> byMonthAndDay = fileSystem.sizeByMonthAndDay();
            byMonthAndDay.keySet().stream().sorted()
                    .forEach(key -> System.out.printf("%s -> %d\n", key, byMonthAndDay.get(key)));
        }
        scanner.close();
    }
}



