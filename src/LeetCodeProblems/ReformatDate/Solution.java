package LeetCodeProblems.ReformatDate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

class Solution {
    public static String reformatDate(String date) {
//        date = date.replaceAll("(?<=\\d)(st|nd|rd|th)", "");
//        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d MMM uuuu");
//        return LocalDate.parse(date,inputFormatter).toString();
        //20th Oct 2001
        String[] parts = date.split("\\s+");
        parts[0] = parts[0].replaceAll("th","").replaceAll("rd","").replaceAll("nd","").replaceAll("st","");
        //20 Oct 2001
        DateTimeFormatter df = DateTimeFormatter.ofPattern("d MMM yyyy");
        //2052-10-20
        String str = Arrays.toString(parts).replaceAll(",","").replaceAll("\\[","").replaceAll("]","");
        return LocalDate.parse(str,df).toString();
    }

    public static void main(String[] args) {
        System.out.println(reformatDate("20th Oct 2052"));
    }
}
