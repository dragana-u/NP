package Ispitni.vtorKolokviumIspit.i18;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * January 2016 Exam problem 2
 */

interface ICluster{
    long getId();
    double getRastojanie(Object o);
}

class Cluster<T extends ICluster> {

    Map<Long,T> elements;
    public Cluster() {
        elements = new TreeMap<>();
    }

    void addItem(T element){
        elements.putIfAbsent(element.getId(),element);
    }
    void near(long id, int top){
        T p1 = elements.get(id);
        AtomicInteger brojac= new AtomicInteger(1);
        elements
                .entrySet()
                .stream()
                .filter(i -> i.getValue().getId()!=id)
                .sorted(Comparator.comparing((v) -> v.getValue().getRastojanie(p1)))
                .limit(top)
                .forEach(i -> System.out.printf("%d. %d -> %.3f\n", brojac.getAndIncrement(),i.getKey(),i.getValue().getRastojanie(p1)) );
    }
}

class Point2D implements ICluster{
    long id;
    float x;
    float y;

    public Point2D(long id, float x, float y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public double getRastojanie(Object o) {
        return Math.sqrt((Math.pow(x - ((Point2D) o).x,2) + (Math.pow(y - ((Point2D) o).y,2))));
    }

}

public class ClusterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Cluster<Point2D> cluster = new Cluster<>();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            long id = Long.parseLong(parts[0]);
            float x = Float.parseFloat(parts[1]);
            float y = Float.parseFloat(parts[2]);
            cluster.addItem(new Point2D(id, x, y));
        }
        int id = scanner.nextInt();
        int top = scanner.nextInt();
        cluster.near(id, top);
        scanner.close();
    }
}

