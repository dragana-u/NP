package Ispitni.vtorKolokviumIspit.i38;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

class InvalidNumberOfAnswers extends Exception{
    public InvalidNumberOfAnswers(String message) {
        super(message);
    }
}

class QuizProcessor{
    static Map<String, Double> processAnswers(InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        Map<String,Double> result = new TreeMap<>();
        result = br.lines().map(line -> {
            try {
                return new StudentQuiz(line);
            } catch (InvalidNumberOfAnswers e) {
                System.out.println(e.getMessage());
            }
            return null;
        })
                .filter(Objects::nonNull)
                .collect(Collectors.toMap(StudentQuiz::getiD,StudentQuiz::getPoints));
        return new TreeMap<>(result);
    }
}

class StudentQuiz{
    String iD;
    List<String> answers;
    List<String> studentAnswers;

    public StudentQuiz(String line) throws InvalidNumberOfAnswers {
        answers = new ArrayList<>();
        studentAnswers = new ArrayList<>();
        //151020;A, B, C;A, C, C
        String[] byComma = line.split(",");
        //151020;A B C;A C C
        String[] part0 = byComma[0].split(";");
        int i=1;
        answers.add(part0[1]);
        boolean flag = false;
        String[] middle = new String[2];
        middle[0]="a";
        for(i=1;i<byComma.length;i++){
            if(byComma[i].contains(";") || flag){
                flag = true;
                if(middle[0].equals("a")){
                    middle = byComma[i].split(";");
                    answers.add(middle[0]);
                    studentAnswers.add(middle[1]);
                }else studentAnswers.add(byComma[i]);
            }else{
                answers.add(byComma[i]);
            }
        }
        if(answers.size()!=studentAnswers.size()){
            throw new InvalidNumberOfAnswers("A quiz must have same number of correct and selected answers");
        }
        iD = part0[0];
    }

    public String getiD() {
        return iD;
    }


    public double getPoints(){
        double pogodeni = 0;
        for(int i=0;i<answers.size();i++){
            if(studentAnswers.get(i).equals(answers.get(i))){
                pogodeni++;
            }
        }
        double promaseni = answers.size()-pogodeni;
        return pogodeni*1.0 - promaseni*0.25;
    }
}

public class QuizProcessorTest {
    public static void main(String[] args) {
        QuizProcessor.processAnswers(System.in).forEach((k, v) -> System.out.printf("%s -> %.2f%n", k, v));
    }
}
