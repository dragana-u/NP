package Ispitni.prvKolokvium.i5;

import java.util.Scanner;

class MinMax<T extends Comparable<T>>{
    T max;
    T min;
    int counter;
    int minCount;
    int maxCount;
    public MinMax() {
        counter=0;
        maxCount=0;
        minCount=0;
    }

    void update(T element){
        if(counter==0){
            min=element;
            max=element;
        }
        ++counter;
        if(max.compareTo(element)<0){
            maxCount=1;
            max = element;
        }else{
            if(max.compareTo(element)==0){
                maxCount++;
            }
        }
        if(min.compareTo(element)>0){
            minCount=1;
            min=element;
        }else{
            if(min.compareTo(element)==0){
                minCount++;
            }
        }
    }
    T max(){
        return max;
    }
    T min(){
        return min;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append(min)
                .append(" ")
                .append(max);
        sb.append(String.format(" %d\n",counter-(maxCount+minCount)));
        return sb.toString();
    }
}
public class MinAndMax {
    public static void main(String[] args) throws ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        MinMax<String> strings = new MinMax<String>();
        for(int i = 0; i < n; ++i) {
            String s = scanner.next();
            strings.update(s);
        }
        System.out.println(strings);
        MinMax<Integer> ints = new MinMax<Integer>();
        for(int i = 0; i < n; ++i) {
            int x = scanner.nextInt();
            ints.update(x);
        }
        System.out.println(ints);
    }
}
