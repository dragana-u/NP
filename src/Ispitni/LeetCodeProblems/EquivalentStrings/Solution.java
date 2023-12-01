package Ispitni.LeetCodeProblems.EquivalentStrings;

import java.util.Arrays;

public class Solution {
    public static boolean arrayStringsAreEqual(String[] word1, String[] word2) {
        String first = Arrays.toString(word1).replace(" ","").replace(",","");
        String second = Arrays.toString(word2).replace(" ","").replace(",","");
        return first.equals(second);
    }
    public static void main(String[] args) {
        String[] word1 = {"ab","c"};
        String[] word2 = {"a","bc"};
        System.out.println(arrayStringsAreEqual(word1,word2));
    }
}
