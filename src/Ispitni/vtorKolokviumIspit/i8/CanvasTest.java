package Ispitni.vtorKolokviumIspit.i8;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class InvalidIDException extends Exception {
    public InvalidIDException(String message) {
        super(message);
    }
}

class InvalidDimensionException extends Exception {
    public InvalidDimensionException(String message) {
        super(message);
    }
}

class Canvas {
    Map<String, Set<Shape>> shapesByUser;
    Set<Shape> allShapes;

    public Canvas() {
        allShapes = new TreeSet<>(Comparator.comparing(Shape::getArea));
        shapesByUser = new TreeMap<>();
    }

    void readShapes(InputStream is) throws InvalidDimensionException {
        Scanner sc = new Scanner(is);
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            try {
                Shape newShape = Shape.createShape(line);
                allShapes.add(newShape);
                String[] parts = line.split("\\s+");
                shapesByUser.putIfAbsent(parts[1], new TreeSet<>(Comparator.comparing(Shape::getPerimeter)));
                shapesByUser.get(parts[1]).add(newShape);
            } catch (InvalidIDException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    void scaleShapes(String userID, double coef) {
//        Set<Shape> sc = shapesByUser.get(userID);
//        if(sc!=null) {
//            for (Shape shape : sc) {
//                shape.scale(coef);
//            }
//         }
        shapesByUser.getOrDefault(userID, new HashSet<>()).forEach(iShape -> iShape.scale(coef));
    }

    void printAllShapes(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        allShapes.forEach(pw::println);
        pw.flush();
    }

    void printByUserId(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        Comparator<Map.Entry<String,Set<Shape>>> comparator = Comparator.comparing(e -> e.getValue().size());
        shapesByUser
                .entrySet()
                .stream()
                .sorted(comparator.reversed().thenComparing(e -> e.getValue().stream().mapToDouble(Shape::getArea).sum()))
                .forEach(e -> {
                    pw.println("Shapes of user: " + e.getKey());
                    e.getValue().forEach(pw::println);
                });
        pw.flush();
    }

    void statistics(OutputStream os) {
        PrintWriter pw = new PrintWriter(os);
        DoubleSummaryStatistics statistics = allShapes.stream().mapToDouble(Shape::getArea).summaryStatistics();
        pw.printf("count: %d\nsum: %.2f\nmin: %.2f\naverage: %.2f\nmax: %.2f\n",
                statistics.getCount(),
                statistics.getSum(),
                statistics.getMin(),
                statistics.getAverage(),
                statistics.getMax());
        pw.flush();
    }
}

abstract class Shape {

    public static Shape createShape(String line) throws InvalidIDException, InvalidDimensionException {
        //1 123456 4.8835
        String[] parts = line.split("\\s+");
        if (Double.parseDouble(parts[2]) == 0) {
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
        if(parts[0].equals("3") && Double.parseDouble(parts[3])==0){
            throw new InvalidDimensionException("Dimension 0 is not allowed!");
        }
        if (parts[1].length() != 6) {
            String e = String.format("ID %s is not valid", parts[1]);
            throw new InvalidIDException(e);
        }
        for (Character c : parts[1].toCharArray()) {
            if (!Character.isAlphabetic(c) && !Character.isDigit(c)) {
                String e = String.format("ID %s is not valid", parts[1]);
                throw new InvalidIDException(e);
            }
        }
        if (Integer.parseInt(parts[0]) == 1) {
            return new Circle(parts[1], Double.parseDouble(parts[2]));
        } else if (Integer.parseInt(parts[0]) == 2) {
            return new Square(parts[1], Double.parseDouble(parts[2]));
        } else {
            return new Rectangle(parts[1], Double.parseDouble(parts[2]), Double.parseDouble(parts[3]));
        }
    }

    abstract void scale(double coef);

    abstract double getPerimeter();

    abstract double getArea();
}

class Circle extends Shape {
    String iD;
    double radius;

    public Circle(String iD, double radius) {
        this.iD = iD;
        this.radius = radius;
    }


    @Override
    void scale(double coef) {
        radius = radius * coef;
    }

    @Override
    double getPerimeter() {
        return 2 * Math.PI * radius;
    }

    @Override
    double getArea() {
        return Math.PI * radius * radius;
    }

    @Override
    public String toString() {
        return String.format("Circle -> Radius: %.2f Area: %.2f Perimeter: %.2f", radius, getArea(), getPerimeter());
    }
}

class Square extends Shape {
    String iD;
    double a;

    public Square(String iD, double a) {
        this.iD = iD;
        this.a = a;
    }

    @Override
    void scale(double coef) {
        a *= coef;
    }

    @Override
    double getPerimeter() {
        return 4 * a;
    }

    @Override
    double getArea() {
        return a * a;
    }

    @Override
    public String toString() {
        return String.format("Square: -> Side: %.2f Area: %.2f Perimeter: %.2f", a, getArea(), getPerimeter());
    }
}

class Rectangle extends Shape {
    String iD;
    double a;
    double b;

    public Rectangle(String iD, double a, double b) {
        this.iD = iD;
        this.a = a;
        this.b = b;
    }

    @Override
    void scale(double coef) {
        a *= coef;
        b *= coef;
    }

    @Override
    double getPerimeter() {
        return 2 * a + 2 * b;
    }

    @Override
    double getArea() {
        return a * b;
    }

    @Override
    public String toString() {
        return String.format("Rectangle: -> Sides: %.2f, %.2f Area: %.2f Perimeter: %.2f", a, b, getArea(), getPerimeter());
    }
}

public class CanvasTest {

    public static void main(String[] args) {
        Canvas canvas = new Canvas();

        System.out.println("READ SHAPES AND EXCEPTIONS TESTING");
        try {
            canvas.readShapes(System.in);
        } catch (InvalidDimensionException e) {
            System.out.println(e.getMessage());
        }

        System.out.println("BEFORE SCALING");
        canvas.printAllShapes(System.out);
        canvas.scaleShapes("123456", 1.5);
        System.out.println("AFTER SCALING");
        canvas.printAllShapes(System.out);

        System.out.println("PRINT BY USER ID TESTING");
        canvas.printByUserId(System.out);

        System.out.println("PRINT STATISTICS");
        canvas.statistics(System.out);
    }
}
