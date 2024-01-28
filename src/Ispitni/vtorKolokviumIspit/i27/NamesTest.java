package Ispitni.vtorKolokviumIspit.i27;

import java.util.*;
import java.util.stream.Collectors;

class Names{
    List<Name> names;
    Names(){
        this.names = new ArrayList<>();
    }
    public void addName(String name){
        int pojavuvanje=1;
        for (Name ime : names) {
            if(ime.name.equals(name)){
                ime.brojPojavuvanja++;
                pojavuvanje = ime.brojPojavuvanja;
            }
        }
        Name toAdd = new Name(name);
        for (Name ime : names) {
            if(ime.name.equals(name)){
                ime.brojPojavuvanja = pojavuvanje;
            }
        }
        toAdd.brojPojavuvanja = pojavuvanje;
        names.add(toAdd);
    }
    public void printN(int n){
      names.stream().distinct().filter(i -> i.brojPojavuvanja>=n).sorted(Comparator.comparing(j -> j.name)).forEach(System.out::println);
    }
    public String findName(int len, int x){
        List<Name> afterDeletion = names.stream().distinct().filter(i -> i.name.length() < len).sorted(Comparator.comparing(j -> j.name)).collect(Collectors.toList());
        int index=0;
        int temp = x;
        while(temp>afterDeletion.size()-1){
            temp -= afterDeletion.size();
        }
        return afterDeletion.get(temp).name;
    }
}

class Name{
    String name;
    int brojPojavuvanja;
    int unikatniBukvi;

    public Name(String name) {
        this.name = name;
        this.brojPojavuvanja=0;
        this.unikatniBukvi=0;
    }

    int getUnikatniBukvi(){
       unikatniBukvi = (int) name.toLowerCase().chars().distinct().count();
        return unikatniBukvi;
    }
    @Override
    public String toString() {
        return String.format("%s (%d) %d",name,brojPojavuvanja,getUnikatniBukvi());
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Name name1 = (Name) o;
        return Objects.equals(name, name1.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
public class NamesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        Names names = new Names();
        for (int i = 0; i < n; ++i) {
            String name = scanner.nextLine();
            names.addName(name);
        }
        n = scanner.nextInt();
        System.out.printf("===== PRINT NAMES APPEARING AT LEAST %d TIMES =====\n", n);
        names.printN(n);
        System.out.println("===== FIND NAME =====");
        int len = scanner.nextInt();
        int index = scanner.nextInt();
        System.out.println(names.findName(len, index));
        scanner.close();

    }
}

