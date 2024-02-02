package Ispitni.vtorKolokviumIspit.i12;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {
    public static double cosineSimilarity (Collection<Integer> c1, Collection<Integer> c2) {
        int [] array1;
        int [] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1=0, down2=0;

        for (int i=0;i<c1.size();i++) {
            up+=(array1[i] * array2[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down1+=(array1[i]*array1[i]);
        }

        for (int i=0;i<c1.size();i++) {
            down2+=(array2[i]*array2[i]);
        }

        return up/(Math.sqrt(down1)*Math.sqrt(down2));
    }
}

class TextProcessor{
    List<String> texts; // site tekstovi
    List<String> wordsList; // site zborovi
    Map<String,Integer> wordFrequency; //word -> frequency;
    List<List<Integer>> textWithVector; //tekst vo vektorska repr
    List<List<String>> wordsForText; //zborovi od tekstovi
    public TextProcessor() {
        wordsList = new ArrayList<>();
        texts = new ArrayList<>();
        wordFrequency = new TreeMap<>();
        textWithVector = new ArrayList<>();
        wordsForText = new ArrayList<>();
    }

    void readText (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        texts = br.lines().collect(Collectors.toList());
        texts.forEach(this::extractWords);
        AtomicInteger i= new AtomicInteger(0);
        texts.forEach(t -> calculateVector(t, i.getAndIncrement()));
    }
    void extractWords(String text){
        String[] parts = text.split("\\s+");
        List<String> wordText = new ArrayList<>();
        for (String word : parts) {
            String resWord = "";
            char[] charArray = word.toCharArray();
            for (char c : charArray) {
                if(Character.isAlphabetic(c) || Character.isSpaceChar(c)){
                    resWord = resWord.concat(String.format("%c",c));
                }
            }
            wordText.add(resWord);
            wordsList.add(resWord.toLowerCase());
            wordFrequency.putIfAbsent(resWord.toLowerCase(),0);
            wordFrequency.computeIfPresent(resWord.toLowerCase(),(k,v) -> {
                v++;
                return v;
            });
        }
        wordsForText.add(wordText);
    }
    void calculateVector(String textToVector, int index){
        wordsList = wordsList.stream().sorted().distinct().collect(Collectors.toList());
        List<Integer> vectors = new ArrayList<>();
        List<String> wordsFromGivenText = wordsForText.get(index);
        for(int i=0;i<wordsList.size();i++){
            vectors.add(calculateOccurrences(wordsList.get(i),wordsFromGivenText));
        }
        textWithVector.add(vectors);
    }
    int calculateOccurrences(String word, List<String> text){
        int res=0;
        for (String s : text) {
            if(s.equalsIgnoreCase(word)) res++;
        }
        return res;
    }
    void printTextsVectors (OutputStream os){
        StringBuilder sb = new StringBuilder();
        PrintWriter pw = new PrintWriter(os);
        for (List<Integer> list : textWithVector) {
            sb.append("[");
            for (Integer i : list) {
                sb.append(String.format("%d, ",i));
            }
            sb.deleteCharAt(sb.lastIndexOf(" "));
            sb.deleteCharAt(sb.lastIndexOf(","));
            sb.append("]\n");
        }
        pw.print(sb);
        pw.flush();
    }
    void printCorpus(OutputStream os, int n, boolean ascending){
        PrintWriter pw = new PrintWriter(os);
        if(ascending){
           wordFrequency.entrySet().stream().sorted(Map.Entry.comparingByValue()).limit(n).forEach(w -> {
               pw.printf("%s : %d\n",w.getKey(),w.getValue());
           });
        }else {
            wordFrequency.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(n).forEach(w -> {
                pw.printf("%s : %d\n",w.getKey(),w.getValue());
            });
        }
        pw.flush();
    }
    public void mostSimilarTexts (OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        double max=0;
        int i1=0,i2=0;
        boolean flag = true;
        for (int i = 0; i < texts.size(); i++) {
            for(int j=0;j<texts.size();j++){
                if(i!=j){
                    double v = CosineSimilarityCalculator.cosineSimilarity(textWithVector.get(i), textWithVector.get(j));
                    if(flag){
                        flag = false;
                        max = v;
                        i1=i;
                        i2=j;
                    }
                    if(v > max){
                        max = v;
                        i1=i;
                        i2=j;
                    }
                }
            }
        }
        pw.printf("%s\n%s\n%.10f",texts.get(i1),texts.get(i2),max);
        pw.flush();
    }
}

public class TextProcessorTest {

    public static void main(String[] args) {
        TextProcessor textProcessor = new TextProcessor();

        textProcessor.readText(System.in);

        System.out.println("===PRINT VECTORS===");
        textProcessor.printTextsVectors(System.out);

        System.out.println("PRINT FIRST 20 WORDS SORTED ASCENDING BY FREQUENCY ");
        textProcessor.printCorpus(System.out,  20, true);

        System.out.println("PRINT FIRST 20 WORDS SORTED DESCENDING BY FREQUENCY");
        textProcessor.printCorpus(System.out, 20, false);

        System.out.println("===MOST SIMILAR TEXTS===");
        textProcessor.mostSimilarTexts(System.out);
    }
}
