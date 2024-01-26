package Ispitni.vtorKolokviumIspit.i21;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class DuplicateNumberException extends Exception {
    public DuplicateNumberException(String message) {
        System.out.printf("Duplicate number: %s", message);
    }
}

class PhoneBook {

    HashMap<String, HashSet<Contact>> contacts;

    public PhoneBook() {
        contacts = new HashMap<>();
    }

    void addContact(String name, String number) throws DuplicateNumberException {
        HashSet<Contact> contact = contacts.get(name);
        Contact c = new Contact(name, number);
        if (contact == null) {
            contact = new HashSet<>();
        }
        if (contact.contains(c)) {
            throw new DuplicateNumberException(number);
        }
        contact.add(c);
        contacts.put(name, contact);
    }

    void contactsByNumber(String number) {
        TreeSet<Contact> tr = contacts.values()
                .stream()
                .flatMap(Set::stream)
                .filter(i -> i.num.contains(number))
                .collect(Collectors.toCollection(() ->
                        new TreeSet<>(Comparator.comparing(Contact::getName).thenComparing(Contact::getNum))));
        if(tr.isEmpty()){
            System.out.println("NOT FOUND");
        }else{
            tr.forEach(i -> System.out.printf("%s %s\n",i.name,i.num));
        }

    }

    void contactsByName(String name) {
        HashSet<Contact> byName = contacts.get(name);
        if (byName == null) {
            System.out.println("NOT FOUND");
        } else {
            LinkedHashSet<Contact> collect = byName.stream().sorted(Comparator.comparing(Contact::getNum)).collect(Collectors.toCollection(LinkedHashSet::new));
            for (Contact contact : collect) {
                System.out.printf("%s %s\n", contact.name, contact.num);
            }
        }
    }
}

class Contact {
    String name;
    String num;

    public Contact(String name, String num) {
        this.name = name;
        this.num = num;
    }

    public String getName() {
        return name;
    }

    public String getNum() {
        return num;
    }

}

public class PhoneBookTest {

    public static void main(String[] args) {
        PhoneBook phoneBook = new PhoneBook();
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            try {
                phoneBook.addContact(parts[0], parts[1]);
            } catch (DuplicateNumberException e) {
                System.out.println(e.getMessage());
            }
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            System.out.println(line);
            String[] parts = line.split(":");
            if (parts[0].equals("NUM")) {
                phoneBook.contactsByNumber(parts[1]);
            } else {
                phoneBook.contactsByName(parts[1]);
            }
        }
    }

}

// Вашиот код овде


