package Auditoriski.a2t2;

public class Matrix {
    public static double sum(double[][] Matrix){
        double sumReturn=0;
        for (double[] row : Matrix) {
            for (double element : row) {
                sumReturn+=element;
            }
        }
        return sumReturn;
    }
    public static double avg(double[][] Matrix){
        double avgReturn=0;
        int count=0;
        for (double[] row : Matrix) {
            for (double element : row) {
                count++;
            }
        }
        return sum(Matrix)/count;
    }

    public static void main(String[] args) {
        double[][] Matrix={{1,2,3},
                            {4,5,6}};
        System.out.printf("Average is: %.2f\n",avg(Matrix));
        System.out.printf("Sum is: %.2f\n",sum(Matrix));
    }
}

