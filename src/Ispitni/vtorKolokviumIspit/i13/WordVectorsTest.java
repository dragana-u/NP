package Ispitni.vtorKolokviumIspit.i13;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Word vectors test
 */

class WordVectors{
    String[] words;
    List<List<Integer>> vectors;
    List<String> text;
    Map<String,List<Integer>> textWithVectors;

    public WordVectors(String[] words, List<List<Integer>> vectors) {
        this.words = words;
        this.vectors = vectors;
        text = new ArrayList<>();
        textWithVectors = new LinkedHashMap<>();
    }
    public void readWords(List<String> wordsToRepresent){
        text.addAll(wordsToRepresent);
    }
    public List<Integer> calculateVector(String word){
        //delectus
        // 9:0:6:1:2
        List<Integer> vectorsRes = new ArrayList<>();
        List<String> listOfWordsWithVectors = Arrays.stream(words).collect(Collectors.toList());
        int indx = listOfWordsWithVectors.lastIndexOf(word);
        if(indx==-1){
            for(int i=0;i<5;i++){
                vectorsRes.add(5);
            }
        }else{
            return vectors.get(indx);
        }
        return vectorsRes;
    }
    public void calculateVectorsForText(){
        text.forEach(t -> {
            List<Integer> v = calculateVector(t);
            textWithVectors.putIfAbsent(t,v);
        });
    }
    public List<Integer> slidingWindow(int n){
        int max;
        List<Integer> res = new ArrayList<>();
        calculateVectorsForText();
        int v1,v2,v3,v4,v5;
        int i=0;
        while(i+n<=text.size()){
            v1=v2=v3=v4=v5=0;
            for(int j=0;j<n;j++){
                v1+=textWithVectors.get(text.get(j+i)).get(0);
                v2+=textWithVectors.get(text.get(j+i)).get(1);
                v3+=textWithVectors.get(text.get(j+i)).get(2);
                v4+=textWithVectors.get(text.get(j+i)).get(3);
                v5+=textWithVectors.get(text.get(j+i)).get(4);
            }
            max = Math.max(v1,v2);
            max = Math.max(max,v3);
            max = Math.max(max,v4);
            max = Math.max(max,v5);
            res.add(max);
            i++;
        }
        return res;
    }
}
public class WordVectorsTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] words = new String[n];
        List<List<Integer>> vectors = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            words[i] = parts[0];
            List<Integer> vector = Arrays.stream(parts[1].split(":"))
                    .map(Integer::parseInt)
                    .collect(Collectors.toList());
            vectors.add(vector);
        }
        n = scanner.nextInt();
        scanner.nextLine();
        List<String> wordsList = new ArrayList<>(n);
        for (int i = 0; i < n; ++i) {
            wordsList.add(scanner.nextLine());
        }
        WordVectors wordVectors = new WordVectors(words, vectors);
        wordVectors.readWords(wordsList);
        n = scanner.nextInt();
        List<Integer> result = wordVectors.slidingWindow(n);
        System.out.println(result.stream()
                .map(Object::toString)
                .collect(Collectors.joining(",")));
        scanner.close();
    }
}



