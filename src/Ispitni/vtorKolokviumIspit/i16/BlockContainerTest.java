package Ispitni.vtorKolokviumIspit.i16;

import java.util.*;
import java.util.stream.Collectors;

class BlockContainer<T extends Comparable<T>>{
    int maxSize;
    List<Set<T>> elements;
    public BlockContainer(int n){
        maxSize=n;
        elements = new ArrayList<>();
    }
    public void add(T a){
        if(elements.isEmpty() || elements.get(elements.size()-1).size() == maxSize ){
             Set<T> set = new TreeSet<>();
            set.add(a);
            elements.add(set);
        }else {
            elements.get(elements.size()-1).add(a);
        }
    }
    public void remove(T a){
        elements.get(elements.size()-1).remove(a);
        if(elements.get(elements.size()-1).isEmpty()){
            elements.remove(elements.get(elements.size()-1));
        }
    }
    public void sort(){
//        elements = elements.stream().sorted(Comparator.comparing(Objects::toString)).collect(Collectors.toList());
        List<T> sorted = elements.stream().flatMap(Collection::stream).sorted().collect(Collectors.toList());
        elements.clear();
        sorted.forEach(this::add);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < elements.size(); i++) {
            sb.append(elements.get(i).toString());
            if(i != elements.size()-1){
                sb.append(",");
            }
        }
        return sb.toString();
    }
}

public class BlockContainerTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int size = scanner.nextInt();
        BlockContainer<Integer> integerBC = new BlockContainer<Integer>(size);
        scanner.nextLine();
        Integer lastInteger = null;
        for(int i = 0; i < n; ++i) {
            int element = scanner.nextInt();
            lastInteger = element;
            integerBC.add(element);
        }
        System.out.println("+++++ Integer Block Container +++++");
        System.out.println(integerBC);
        System.out.println("+++++ Removing element +++++");
        integerBC.remove(lastInteger);
        System.out.println("+++++ Sorting container +++++");
        integerBC.sort();
        System.out.println(integerBC);
        BlockContainer<String> stringBC = new BlockContainer<String>(size);
        String lastString = null;
        for(int i = 0; i < n; ++i) {
            String element = scanner.next();
            lastString = element;
            stringBC.add(element);
        }
        System.out.println("+++++ String Block Container +++++");
        System.out.println(stringBC);
        System.out.println("+++++ Removing element +++++");
        stringBC.remove(lastString);
        System.out.println("+++++ Sorting container +++++");
        stringBC.sort();
        System.out.println(stringBC);
    }
}

// Вашиот код овде




