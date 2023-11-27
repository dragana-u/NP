package Ispitni.prvKolokvium.i8;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


class InvalidTime extends Exception{
    public InvalidTime(String message) {
        super(message);
    }
}
class NonExistingItemException extends Exception{
    public NonExistingItemException(int i) {
        super(String.format("Item with id %d doesn't exist",i));
    }
}
class ArchiveStore{
    List<Archive> archives;
    StringBuilder log;

    public ArchiveStore() {
        archives = new ArrayList<>();
        log = new StringBuilder();
    }
    void archiveItem(Archive item, LocalDate date){
        item.archive(date);
        archives.add(item);
        log.append(String.format("Item %d archived at %d-%02d-%02d\n",item.id,date.getYear(),date.getMonthValue(),date.getDayOfMonth()));
    }
    public Archive openItem(int id, LocalDate date) throws NonExistingItemException {
                for (Archive archive : archives) {
                    if (id == archive.getId()) {
                        try {
                            archive.open(date);
                        } catch (InvalidTime e) {
                            log.append(e.getMessage());
                            return archive;
                        }
                        log.append(String.format("Item %d opened at %s\n",
                                archive.getId(), date));
                        return archive;
                    }
                }
        throw new NonExistingItemException(id);
    }
    String getLog(){
        return log.toString();
    }
}
abstract class Archive{
    protected int id;
    protected LocalDate date;

    public void archive(LocalDate date) {
        this.date = date;
    }
    public int getId(){
        return id;
    }
    abstract void open(LocalDate ld) throws  InvalidTime;
}

class LockedArchive extends Archive{
    LocalDate dateToOpen;

    public LockedArchive(int id,  LocalDate dateToOpen) {
        this.dateToOpen = dateToOpen;
        this.id = id;
    }

    @Override
    void open(LocalDate ld) throws InvalidTime {
        if(ld.isBefore(dateToOpen)){
            throw new InvalidTime(String.format("Item %d cannot be opened before %d-%02d-%02d\n",id,dateToOpen.getYear(),dateToOpen.getMonthValue(),dateToOpen.getDayOfMonth()));
        }else{
        }
    }
}

class SpecialArchive extends Archive{
    int maxOpen;
    int counter;

    public SpecialArchive(int id,  int maxOpen) {
        this.maxOpen = maxOpen;
        this.id = id;
        counter=0;
    }
    public void increaseCounter(){
        counter++;
    }
    @Override
    void open(LocalDate ld) throws InvalidTime {
        if(counter>=maxOpen){
            throw new InvalidTime(String.format("Item %d cannot be opened more than %d times\n",
                    id,maxOpen));
        }else{
            increaseCounter();
        }
    }
}
public class ArchiveStoreTest {
    public static void main(String[] args) {
        ArchiveStore store = new ArchiveStore();
        LocalDate date = LocalDate.of(2013, 10, 7);
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();
        int n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        int i;
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            long days = scanner.nextLong();

            LocalDate dateToOpen = date.atStartOfDay().plusSeconds(days * 24 * 60 * 60).toLocalDate();
            LockedArchive lockedArchive = new LockedArchive(id, dateToOpen);
            store.archiveItem(lockedArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        n = scanner.nextInt();
        scanner.nextLine();
        scanner.nextLine();
        for (i = 0; i < n; ++i) {
            int id = scanner.nextInt();
            int maxOpen = scanner.nextInt();
            SpecialArchive specialArchive = new SpecialArchive(id, maxOpen);
            store.archiveItem(specialArchive, date);
        }
        scanner.nextLine();
        scanner.nextLine();
        while(scanner.hasNext()) {
            int open = scanner.nextInt();
            try {
                store.openItem(open, date);
            } catch(NonExistingItemException e) {
                System.out.println(e.getMessage());
            }
        }
        System.out.println(store.getLog());
    }
}
