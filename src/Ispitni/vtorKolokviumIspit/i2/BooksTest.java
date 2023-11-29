package Ispitni.vtorKolokviumIspit.i2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

class Book implements Comparable<Book>{
    String title;
    String category;
    float price;

    public Book(String title, String category, float price) {
        this.title = title;
        this.category = category;
        this.price = price;
    }

    @Override
    public int compareTo(Book o) {
        if(title.equalsIgnoreCase(o.title)){
            return (int) Math.floor(price-o.price);
        }
        return title.compareTo(o.title);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) %.2f",title,category,price);
    }
}

class BookCollection{
    List<Book> books;

    public BookCollection() {
        books = new ArrayList<>();
    }
    public void addBook(Book book){
        books.add(book);
    }
    public void printByCategory(String category){
        books.stream()
                .filter(book -> book.category.equalsIgnoreCase(category))
                .sorted()
                .forEach(System.out::println);
    }
    public List<Book> getCheapestN(int n){
        return books.stream()
                .sorted(Comparator.comparing(x -> x.title))
                .sorted(Comparator.comparing(x -> x.price))
                .limit(n).collect(Collectors.toList());
    }
}
public class BooksTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        BookCollection booksCollection = new BookCollection();
        Set<String> categories = fillCollection(scanner, booksCollection);
        System.out.println("=== PRINT BY CATEGORY ===");
        for (String category : categories) {
            System.out.println("CATEGORY: " + category);
            booksCollection.printByCategory(category);
        }
        System.out.println("=== TOP N BY PRICE ===");
        print(booksCollection.getCheapestN(n));
    }

    static void print(List<Book> books) {
        for (Book book : books) {
            System.out.println(book);
        }
    }

    static TreeSet<String> fillCollection(Scanner scanner,
                                          BookCollection collection) {
        TreeSet<String> categories = new TreeSet<String>();
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split(":");
            Book book = new Book(parts[0], parts[1], Float.parseFloat(parts[2]));
            collection.addBook(book);
            categories.add(parts[1]);
        }
        return categories;
    }
}

