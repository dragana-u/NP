package Ispitni.LeetCodeProblems.DayOfTheWeek;

import java.time.LocalDate;

public class Solution {
    public static String dayOfTheWeek(int day, int month, int year) {
        String  dayValue = LocalDate.of(year,month,day).getDayOfWeek().name().toLowerCase();
        String d1 = dayValue.substring(0,1).toUpperCase();
        return d1 + dayValue.substring(1);
    }

    public static void main(String[] args) {
        System.out.println(dayOfTheWeek(18,7,1999));
    }
}
