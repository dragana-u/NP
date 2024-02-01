package Ispitni.prvKolokvium.i13;

import java.util.*;

class InvalidPositionException extends Exception{
    public InvalidPositionException(String message) {
        super(message);
    }
}

class Component{
    String color;
    int weight;
    Set<Component> vnatresni;

    public Component(String color, int weight) {
        this.color = color;
        this.weight = weight;
        vnatresni = new TreeSet<>(Comparator.comparing(Component::getWeight).thenComparing(Component::getColor));
    }

    void addComponent(Component component){
        vnatresni.add(component);
    }

    public String getColor() {
        return color;
    }

    public int getWeight() {
        return weight;
    }

    public Set<Component> getVnatresni() {
        return vnatresni;
    }
    void changeColor(String color, int weight){
        if(this.weight < weight){
            this.color = color;
        }
//        vnatresni.stream().filter(v -> v.weight<weight).forEach(c -> c.color = color);
        for (Component component : vnatresni) {
            change(component,color,weight);
        }
    }
    static void change(Component component,String color,int weight) {
        if(component.weight < weight) {
            component.color = color;
        }
        for (Component c : component.vnatresni) {
            change(c, color,weight);
        }
    }

    public static void outputString(StringBuilder sb, Component c, int level){
        for(int i=0;i<level;i++){
            sb.append("---");
        }
        sb.append(String.format("%d:%s\n",c.weight,c.color));
        c.vnatresni.forEach(v -> outputString(sb,v,level+1));
    }
}

class Window{
    String ime;
    Map<Integer,Component> components; //pos->Component

    public Window(String ime) {
        this.ime = ime;
        components = new TreeMap<>();
    }
    void addComponent(int position, Component component) throws InvalidPositionException {
        Component componentToAdd = components.get(position);
        if(componentToAdd == null){
            components.put(position,component);
        }else{
            throw new InvalidPositionException(String.format("Invalid position %d, alredy taken!",position));
        }
    }

    void changeColor(int weight, String color){
//        components.values().stream().filter(i -> i.weight<weight).forEach(j -> j.changeColorToInner(color,weight));
        components.values().forEach(c -> c.changeColor(color,weight));
    }
    void swichComponents(int pos1, int pos2){
        Component c1 = components.get(pos1);
        Component c2 = components.get(pos2);
        components.put(pos1,c2);
        components.put(pos2,c1);
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("WINDOW %s\n",ime));
        for (Map.Entry<Integer, Component> integerComponentEntry : components.entrySet()) {
            sb.append(String.format("%d:",integerComponentEntry.getKey()));
            Component.outputString(sb,integerComponentEntry.getValue(),0);
        }
        return sb.toString();
    }
}
public class ComponentTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String name = scanner.nextLine();
        Window window = new Window(name);
        Component prev = null;
        while (true) {
            try {
                int what = scanner.nextInt();
                scanner.nextLine();
                if (what == 0) {
                    int position = scanner.nextInt();
                    window.addComponent(position, prev);
                } else if (what == 1) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev = component;
                } else if (what == 2) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                    prev = component;
                } else if (what == 3) {
                    String color = scanner.nextLine();
                    int weight = scanner.nextInt();
                    Component component = new Component(color, weight);
                    prev.addComponent(component);
                } else if(what == 4) {
                    break;
                }

            } catch (InvalidPositionException e) {
                System.out.println(e.getMessage());
            }
            scanner.nextLine();
        }

        System.out.println("=== ORIGINAL WINDOW ===");
        System.out.println(window);
        int weight = scanner.nextInt();
        scanner.nextLine();
        String color = scanner.nextLine();
        window.changeColor(weight, color);
        System.out.println(String.format("=== CHANGED COLOR (%d, %s) ===", weight, color));
        System.out.println(window);
        int pos1 = scanner.nextInt();
        int pos2 = scanner.nextInt();
        System.out.println(String.format("=== SWITCHED COMPONENTS %d <-> %d ===", pos1, pos2));
        window.swichComponents(pos1, pos2);
        System.out.println(window);
    }
}

// вашиот код овде