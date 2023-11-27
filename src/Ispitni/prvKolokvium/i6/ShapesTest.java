package Ispitni.prvKolokvium.i6;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum Color {
    RED, GREEN, BLUE
}
class Canvas{

    List<Shape> shapes;

    public Canvas() {
       shapes = new ArrayList<>();
    }

    int findIndx(float plostina){
        for(int i=0;i<shapes.size();i++){
            if(shapes.get(i).weight()<plostina){
                return i;
            }
        }
        return shapes.size();
    }
    public void add(String id, Color color, float radius) {
        Cirlce c = new Cirlce(id,color,radius);
        int indx = findIndx(c.weight());
        shapes.add(indx,c);
    }
    void add(String id, Color color, float width, float height){
        Rectangle r = new Rectangle(id,color,width,height);
        int indx = findIndx(r.weight());
        shapes.add(indx,r);
    }
    void scale(String id, float scaleFactor){
        Shape s = null;
        for (int i = shapes.size() - 1; i >= 0; i--) {
            if (shapes.get(i).id.equals(id)) {
                s = shapes.get(i);
                shapes.remove(i);
                break;
            }
        }
        s.scale(scaleFactor);
        int index = findIndx(s.weight());
        shapes.add(index, s);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Shape shape : shapes) {
            sb.append(shape);
        }
        return sb.toString();
    }
}

interface Scalable{
    void scale (float scaleFactor);
}
interface Stackable{
    float weight();
}
abstract class Shape implements Scalable,Stackable{
    String id;
    Color boja;

    public Shape(String id, Color boja) {
        this.id = id;
        this.boja = boja;
    }
}

class Cirlce extends Shape{
    float rad;

    public Cirlce(String id, Color boja, float rad) {
        super(id, boja);
        this.rad = rad;
    }

    @Override
    public void scale(float scaleFactor) {
        rad *= scaleFactor;
    }

    @Override
    public float weight() {
        return (float) (Math.PI*rad*rad);
    }

    @Override
    public String toString() {
        return String.format("C: %-5s%-10s%10.2f\n",id,boja,weight());
    }
}

class Rectangle extends Shape{
    float a;
    float b;

    public Rectangle(String id, Color boja, float a, float b) {
        super(id, boja);
        this.a = a;
        this.b = b;
    }

    @Override
    public void scale(float scaleFactor) {
        a*=scaleFactor;
        b*=scaleFactor;
    }

    @Override
    public float weight() {
        return a*b;
    }

    @Override
    public String toString() {
        return String.format("R: %-5s%-10s%10.2f\n",id,boja,weight());
    }
}
public class ShapesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Canvas canvas = new Canvas();
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            String[] parts = line.split(" ");
            int type = Integer.parseInt(parts[0]);
            String id = parts[1];
            if (type == 1) {
                Color color = Color.valueOf(parts[2]);
                float radius = Float.parseFloat(parts[3]);
                canvas.add(id, color, radius);
            } else if (type == 2) {
                Color color = Color.valueOf(parts[2]);
                float width = Float.parseFloat(parts[3]);
                float height = Float.parseFloat(parts[4]);
                canvas.add(id, color, width, height);
            } else if (type == 3) {
                float scaleFactor = Float.parseFloat(parts[2]);
                System.out.println("ORIGNAL:");
                System.out.print(canvas);
                canvas.scale(id, scaleFactor);
                System.out.printf("AFTER SCALING: %s %.2f\n", id, scaleFactor);
                System.out.print(canvas);
            }

        }
    }
}

