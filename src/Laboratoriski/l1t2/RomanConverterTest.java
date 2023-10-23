//Да се напише метод кој ќе прима еден цел број и
// ќе ја печати неговата репрезентација како Римски број.
// Aко ако се повика со парамететар 1998, излезот треба да биде MCMXCVIII.

package Laboratoriski.l1t2;

import java.util.Scanner;
import java.util.stream.IntStream;

public class RomanConverterTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        IntStream.range(0, n)
                .forEach(x -> System.out.println(RomanConverter.toRoman(scanner.nextInt())));
        scanner.close();
    }
}


class RomanConverter {
    /**
     * Roman to decimal converter
     *
     * @param n number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int n) {
        // your solution here
        StringBuilder sb = new StringBuilder();
        while(n>0){
            if(n-1000>=0){
                sb.append("M");
                n=n-1000;
            }
            else if(n-900>=0){
                sb.append("CM");
                n=n-900;
            }
            else if(n-500>=0){
                sb.append("D");
                n=n-500;
            }
            else if(n-400>=0){
                sb.append("CD");
                n=n-400;
            }
            else if(n-100>=0){
                sb.append("C");
                n=n-100;
            }
            else if(n-90>=0){
                sb.append("XC");
                n=n-90;
            }
            else if(n-50>=0){
                sb.append("L");
                n=n-50;
            }
            else if(n-40>=0){
                sb.append("XL");
                n=n-40;
            }
            else if(n-10>=0){
                sb.append("X");
                n=n-10;
            }
            else if(n-9>=0){
                sb.append("IX");
                n=n-9;
            }
            else if(n-5>=0){
                sb.append("V");
                n=n-5;
            }
            else if(n-4>=0){
                sb.append("IV");
                n=n-4;
            }
            else if(n-1>=0){
                sb.append("I");
                n=n-1;
            }
        }
        return sb.toString();
    }

}

