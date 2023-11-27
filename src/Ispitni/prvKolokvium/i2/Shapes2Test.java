package Ispitni.prvKolokvium.i2;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;


class IrregularCanvasException extends Exception{
    public IrregularCanvasException(String id, double maxArea) {
        super(String.format("Canvas %s has a shape with area larger than %.2f",
                id,maxArea));
    }

}
abstract class  Shape{
    String id;
    double size;

    public Shape(String id, double size, double maxArea) throws IrregularCanvasException {
        this.id = id;
        this.size = size;
        if(area()>maxArea){
            throw new IrregularCanvasException(id,maxArea);
        }
    }
    abstract double area();
}

class Circle extends Shape{
    public Circle(String id, double size,double maxArea) throws IrregularCanvasException {
        super(id, size,maxArea);
    }

    @Override
    double area() {
        return Math.PI*size*size;
    }
}

class Square extends Shape{
    public Square(String id, double size,double maxArea) throws IrregularCanvasException {
        super(id,size,maxArea);
    }

    @Override
    double area() {
        return size*size;
    }
}

class Canvas implements Comparable<Canvas> {
    List<Shape> shapes;
    int count =0;
    String id;

    public Canvas(List<Shape> shapes, int count, String id) {
        this.shapes = shapes;
        this.count=count;
        this.id = id;
    }

    static Canvas createCanvas(String line, double maxArea) throws IrregularCanvasException {
        //184ef1d4 S 28 S 26 S 2001 S 28 C 30 C 16 S 18
        String[] parts = line.split("\\s+");
        String id = parts[0];
        int c = 0;
        List<Shape> list = new ArrayList<>();
        for(int i=1;i<parts.length;i++){
            if(i%2!=0){
                if(parts[i].equals("S")){
                    list.add(new Square(id,Integer.parseInt(parts[i+1]),maxArea));
                    c++;
                }else{
                    list.add(new Circle(id,Integer.parseInt(parts[i+1]),maxArea));
                }
            }
        }
        return new Canvas(list,c,id);
    }

    double sum(){
       return  shapes.stream().mapToDouble(Shape::area).sum();
    }
    @Override
    public int compareTo(Canvas o) {
        return Double.compare(sum(),o.sum());
    }

    @Override
    public String toString() {
       //ID total_shapes total_circles total_squares min_area max_area average_area
        DoubleSummaryStatistics dss = shapes.stream().mapToDouble(Shape::area).summaryStatistics();
       return  String.format("%s %d %d %d %.2f %.2f %.2f",id ,shapes.size(),shapes.size()-count,count,
                dss.getMin(),dss.getMax(),dss.getAverage());
    }
}
class ShapesApplication{
    double maxArea;
    List<Canvas> canvases;

    public ShapesApplication(double maxArea) {
        this.maxArea = maxArea;
        canvases = new ArrayList<>();
    }

    public void readCanvases(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        canvases = br.lines().map((String line) -> {
            try {
                return Canvas.createCanvas(line,maxArea);
            } catch (IrregularCanvasException e){
                System.out.println(e.getMessage());
                return null;
            }
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        br.close();
    }

    public void printCanvases(PrintStream out) {
        PrintWriter pw = new PrintWriter(out);
        canvases
                .stream()
                .sorted(Comparator.reverseOrder())
                .forEach(pw::println);
        pw.flush();
    }

}

public class Shapes2Test {

    public static void main(String[] args) throws IOException {

        ShapesApplication shapesApplication = new ShapesApplication(10000);

        System.out.println("===READING CANVASES AND SHAPES FROM INPUT STREAM===");
        shapesApplication.readCanvases(System.in);

        System.out.println("===PRINTING SORTED CANVASES TO OUTPUT STREAM===");
        shapesApplication.printCanvases(System.out);


    }
}
