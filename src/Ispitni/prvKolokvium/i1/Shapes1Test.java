package Ispitni.prvKolokvium.i1;

import java.io.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

class Shape{
    String id;
    List<Integer> sizes;

    public Shape(String id, List<Integer> sizes) {
        this.id = id;
        this.sizes = sizes;
    }
    public static Shape createCanvas(String line){
        //0        1  2  3  4  5  6  7  8  9  10 11 12 13 14
        //364fbe94 24 30 22 33 32 30 37 18 29 27 33 21 27 26
        String[] split = line.split("\\s+");
        List<Integer> list = new ArrayList<>();
        String canvasId = split[0];
        for(int i=0;i< split.length-1;i++){
            list.add(Integer.parseInt(split[i+1]));
        }
        return new Shape(canvasId,list);
    }


    public int getPerim(){
        return sizes.stream().mapToInt(i -> 4 * i).sum();
    }

    public int getLen() {
        return sizes.size();
    }

    @Override
    public String toString() {
        return String.format("%s %d %d",id,sizes.size(),getPerim());
    }
}

class ShapesApplication{
    List<Shape> shapes;

    public ShapesApplication() {
        shapes = new ArrayList<>();
    }
    public int readCanvases(InputStream in) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        shapes = br.lines().map(line -> Shape.createCanvas(line)).collect(Collectors.toList());
        return shapes.stream().mapToInt(i -> i.getLen()).sum();
    }

    public void printLargestCanvasTo(OutputStream out) {
        PrintWriter printWriter=new PrintWriter(out);
        Shape max = shapes.stream().max(Comparator.comparing(i -> i.getPerim())).get();
        printWriter.println(max);
        printWriter.flush();
        printWriter.close();
    }
}
public class Shapes1Test {

    public static void main(String[] args) throws IOException {
        ShapesApplication shapesApplication = new ShapesApplication();

        System.out.println("===READING SQUARES FROM INPUT STREAM===");
        System.out.println(shapesApplication.readCanvases(System.in));
        System.out.println("===PRINTING LARGEST CANVAS TO OUTPUT STREAM===");
        shapesApplication.printLargestCanvasTo(System.out);

    }
}
