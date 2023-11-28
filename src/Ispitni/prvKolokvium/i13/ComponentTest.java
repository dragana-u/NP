//package Ispitni.prvKolokvium.i13;
//
//import java.util.Scanner;
//import java.util.Set;
//import java.util.TreeSet;
//
//public class ComponentTest {
//    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        String name = scanner.nextLine();
//        Window window = new Window(name);
//        Component prev = null;
//        while (true) {
//            try {
//                int what = scanner.nextInt();
//                scanner.nextLine();
//                if (what == 0) {
//                    int position = scanner.nextInt();
//                    window.addComponent(position, prev);
//                } else if (what == 1) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev = component;
//                } else if (what == 2) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev.addComponent(component);
//                    prev = component;
//                } else if (what == 3) {
//                    String color = scanner.nextLine();
//                    int weight = scanner.nextInt();
//                    Component component = new Component(color, weight);
//                    prev.addComponent(component);
//                } else if(what == 4) {
//                    break;
//                }
//
//            } catch (InvalidPositionException e) {
//                System.out.println(e.getMessage());
//            }
//            scanner.nextLine();
//        }
//
//        System.out.println("=== ORIGINAL WINDOW ===");
//        System.out.println(window);
//        int weight = scanner.nextInt();
//        scanner.nextLine();
//        String color = scanner.nextLine();
//        window.changeColor(weight, color);
//        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
//        System.out.println(window);
//        int pos1 = scanner.nextInt();
//        int pos2 = scanner.nextInt();
//        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
//        window.swichComponents(pos1, pos2);
//        System.out.println(window);
//    }
//}
//
//// вашиот код овде
//class Component implements Comparable<Component>{
//    String color;
//    int weight;
//    Set<Component> inner;
//
//    public Component(String color, int weight) {
//        this.color = color;
//        this.weight = weight;
//        this.inner = new TreeSet<Component>();
//    }
//    void addComponent(Component component){
//        inner.add(component);
//    }
//
//    @Override
//    public int compareTo(Component o) {
//        if(weight==o.weight){
//            return color.compareTo(o.color);
//        }else{
//            return weight - o.weight;
//        }
//    }
//}
//
//class Window{
//
//}