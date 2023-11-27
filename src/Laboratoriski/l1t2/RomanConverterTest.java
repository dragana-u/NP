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
     * @param num number in decimal format
     * @return string representation of the number in Roman numeral
     */
    public static String toRoman(int num) {
        // your solution here
        StringBuilder sb = new StringBuilder();
        while(num>0){
            if(num-1000>=0){
                sb.append("M");
                num=num-1000;
            }
            else if(num-900>=0){
                sb.append("CM");
                num=num-900;
            }
            else if(num-500>=0){
                sb.append("D");
                num=num-500;
            }
            else if(num-400>=0){
                sb.append("CD");
                num=num-400;
            }
            else if(num-100>=0){
                sb.append("C");
                num=num-100;
            }
            else if(num-90>=0){
                sb.append("XC");
                num=num-90;
            }
            else if(num-50>=0){
                sb.append("L");
                num=num-50;
            }
            else if(num-40>=0){
                sb.append("XL");
                num=num-40;
            }
            else if(num-10>=0){
                sb.append("X");
                num=num-10;
            }
            else if(num-9>=0){
                sb.append("IX");
                num=num-9;
            }
            else if(num-5>=0){
                sb.append("V");
                num=num-5;
            }
            else if(num-4>=0){
                sb.append("IV");
                num=num-4;
            }
            else if(num-1>=0){
                sb.append("I");
                num=num-1;
            }
        }
        return sb.toString();
    }

}

