package Laboratoriski.l2t2;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


class InsufficientElementsException extends Exception {
    public InsufficientElementsException() {
        super("Insufficient number of elements");
    }
}

class InvalidRowNumberException extends Exception {
    public InvalidRowNumberException(){
        super("Invalid row number");
    }
}

class InvalidColumnNumberException extends Exception {
    public InvalidColumnNumberException(){
        super("Invalid column number");
    }
}

final class DoubleMatrix {
    private final double[][] data;
    private final int m;
    private final int n;

    DoubleMatrix(double[] arr, int m, int n) throws InsufficientElementsException {
        if (arr.length < (m * n)) {
            throw new InsufficientElementsException();
        }
        this.data = new double[m][n];
        int k = arr.length - (m*n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                this.data[i][j] = arr[k++];
            }
        }
        this.m=m;
        this.n=n;
    }
    public String getDimensions(){
        return String.format("[%d x %d]",m,n);
    }
    public int rows(){
        return m;
    }
    public int columns(){
        return n;
    }
    public double maxElementAtRow(int row) throws InvalidRowNumberException {
        if(row<1 || row>m){
            throw new InvalidRowNumberException();
        }
        double max=-5000.0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(data[row-1][j]>max){
                    max=data[row-1][j];
                }
            }
        }
        return max;
//        if (row < 1 || row > m) {
//            throw new InvalidRowNumberException();
//        }
//        return Arrays.stream(data[row - 1]).max().orElse(0);
    }
    public double maxElementAtColumn(int column) throws InvalidColumnNumberException {
        if(column<1 || column>n){
            throw new InvalidColumnNumberException();
        }
        double max=-5000.0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                if(data[i][column-1]>max){
                    max=data[i][column-1];
                }
            }
        }
        return max;
//        if(column<1 || column>n){
//            throw new InvalidColumnNumberException();
//        }
//        return IntStream.range(0,data.length)
//                .mapToDouble(i -> data[i][column-1])
//                .max().orElse(0);
    }
    public double sum(){
        double sum=0.0;
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                sum+=data[i][j];
            }
        }
        return sum;
    }

    public double[] toSortedArray(){
        double[] sorted = Arrays.stream(data)
                .flatMapToDouble(Arrays::stream)
                .sorted()
                .toArray();
        return IntStream.range(0, sorted.length)
                .mapToDouble(i -> sorted[sorted.length - 1 - i])
                .toArray();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<m;i++){
            for(int j=0;j<n;j++){
                sb.append(String.format("%.2f",data[i][j]));
                if(j!=n-1){
                    sb.append("\t");
                }
            }
            if(i!=m-1) {
                sb.append("\n");
            }
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoubleMatrix that = (DoubleMatrix) o;
        return Arrays.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(data);
    }
}

class MatrixReader{
    public static DoubleMatrix read(InputStream input) throws InsufficientElementsException {
        Scanner sc = new Scanner(input);
        int m = sc.nextInt();
        int n = sc.nextInt();
        double[] data = new double[m*n];
        for(int i=0;i<data.length;i++){
            data[i]=sc.nextDouble();
        }
        return new DoubleMatrix(data,m,n);
    }
}

public class DoubleMatrixTester {

    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);

        int tests = scanner.nextInt();
        DoubleMatrix fm = null;

        double[] info = null;

        DecimalFormat format = new DecimalFormat("0.00");

        for (int t = 0; t < tests; t++) {

            String operation = scanner.next();

            switch (operation) {
                case "READ": {
                    int N = scanner.nextInt();
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    double[] f = new double[N];

                    for (int i = 0; i < f.length; i++)
                        f[i] = scanner.nextDouble();

                    try {
                        fm = new DoubleMatrix(f, R, C);
                        info = Arrays.copyOf(f, f.length);

                    } catch (InsufficientElementsException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }

                    break;
                }

                case "INPUT_TEST": {
                    int R = scanner.nextInt();
                    int C = scanner.nextInt();

                    StringBuilder sb = new StringBuilder();

                    sb.append(R + " " + C + "\n");

                    scanner.nextLine();

                    for (int i = 0; i < R; i++)
                        sb.append(scanner.nextLine() + "\n");

                    fm = MatrixReader.read(new ByteArrayInputStream(sb
                            .toString().getBytes()));

                    info = new double[R * C];
                    Scanner tempScanner = new Scanner(new ByteArrayInputStream(sb
                            .toString().getBytes()));
                    tempScanner.nextDouble();
                    tempScanner.nextDouble();
                    for (int z = 0; z < R * C; z++) {
                        info[z] = tempScanner.nextDouble();
                    }

                    tempScanner.close();

                    break;
                }

                case "PRINT": {
                    System.out.println(fm.toString());
                    break;
                }

                case "DIMENSION": {
                    System.out.println("Dimensions: " + fm.getDimensions());
                    break;
                }

                case "COUNT_ROWS": {
                    System.out.println("Rows: " + fm.rows());
                    break;
                }

                case "COUNT_COLUMNS": {
                    System.out.println("Columns: " + fm.columns());
                    break;
                }

                case "MAX_IN_ROW": {
                    int row = scanner.nextInt();
                    try {
                        System.out.println("Max in row: "
                                + format.format(fm.maxElementAtRow(row)));
                    } catch (InvalidRowNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "MAX_IN_COLUMN": {
                    int col = scanner.nextInt();
                    try {
                        System.out.println("Max in column: "
                                + format.format(fm.maxElementAtColumn(col)));
                    } catch (InvalidColumnNumberException e) {
                        System.out.println("Exception caught: " + e.getMessage());
                    }
                    break;
                }

                case "SUM": {
                    System.out.println("Sum: " + format.format(fm.sum()));
                    break;
                }

                case "CHECK_EQUALS": {
                    int val = scanner.nextInt();

                    int maxOps = val % 7;

                    for (int z = 0; z < maxOps; z++) {
                        double work[] = Arrays.copyOf(info, info.length);

                        int e1 = (31 * z + 7 * val + 3 * maxOps) % info.length;
                        int e2 = (17 * z + 3 * val + 7 * maxOps) % info.length;

                        if (e1 > e2) {
                            double temp = work[e1];
                            work[e1] = work[e2];
                            work[e2] = temp;
                        }

                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(work, fm.rows(),
                                fm.columns());
                        System.out
                                .println("Equals check 1: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    if (maxOps % 2 == 0) {
                        DoubleMatrix f1 = fm;
                        DoubleMatrix f2 = new DoubleMatrix(new double[]{3.0, 5.0,
                                7.5}, 1, 1);

                        System.out
                                .println("Equals check 2: "
                                        + f1.equals(f2)
                                        + " "
                                        + f2.equals(f1)
                                        + " "
                                        + (f1.hashCode() == f2.hashCode() && f1
                                        .equals(f2)));
                    }

                    break;
                }

                case "SORTED_ARRAY": {
                    double[] arr = fm.toSortedArray();

                    String arrayString = "[";

                    if (arr.length > 0)
                        arrayString += format.format(arr[0]) + "";

                    for (int i = 1; i < arr.length; i++)
                        arrayString += ", " + format.format(arr[i]);

                    arrayString += "]";

                    System.out.println("Sorted array: " + arrayString);
                    break;
                }

            }

        }

        scanner.close();
    }
}

