package Ispitni.prvKolokvium.i23;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

public class QuizTest {
    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        Quiz quiz = new Quiz();

        int questions = Integer.parseInt(sc.nextLine());

        for (int i=0;i<questions;i++) {
            quiz.addQuestion(sc.nextLine());
        }

        List<String> answers = new ArrayList<>();

        int answersCount =  Integer.parseInt(sc.nextLine());

        for (int i=0;i<answersCount;i++) {
            answers.add(sc.nextLine());
        }

        int testCase = Integer.parseInt(sc.nextLine());

        if (testCase==1) {
            quiz.printQuiz(System.out);
        } else if (testCase==2) {
            try {
                quiz.answerQuiz(answers, System.out);
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println("Invalid test case");
        }
    }
}

class Quiz{
    List<Question> questions;

    public Quiz() {
        questions = new ArrayList<>();
    }

    void addQuestion(String questionData){
        //MC;Question1;3;E
        String[] parts = questionData.split(";");
        if(parts[0].equals("MC")){
            try {
                questions.add(new MC(typeQuestion.MC,questionData));
            } catch (InvalidOperationException e) {
                System.out.println(e.getMessage());
            }
        }else{
            questions.add(new TF(typeQuestion.TF,questionData));
        }
    }
    void printQuiz(OutputStream os){
        PrintWriter pw = new PrintWriter(os);
        questions
                .stream()
                .sorted(Comparator.reverseOrder())
                .forEach(x -> pw.print(x));

        pw.flush();
    }
    void answerQuiz (List<String> answers, OutputStream os) throws InvalidOperationException {
        if(questions.size()!=answers.size()){
            throw new InvalidOperationException("Answers and questions must be of same length!");
        }
        double points = 0;
        double pointPerQuestion=0;
        for(int i=0;i<answers.size();i++){
            if(questions.get(i).getAnswer().equals(answers.get(i))){
                points+=questions.get(i).points;
                pointPerQuestion = questions.get(i).points;
            }else if(questions.get(i).tq==typeQuestion.MC){
                points-=questions.get(i).points*0.20;
                pointPerQuestion = (questions.get(i).points*0.20)*-1;
            }else{
                pointPerQuestion=0;
            }
            System.out.printf("%d. %.2f\n",i+1,pointPerQuestion);
        }
        System.out.printf("Total points: %.2f\n",points);
    }
}

enum typeQuestion{
    TF,
    MC
}
abstract class Question implements Comparable<Question>{
    protected String text;
    protected int points;
    protected typeQuestion tq;
    @Override
    public int compareTo(Question o) {
        return points - o.points;
    }
    abstract String getAnswer();
}

class TF extends Question{
    boolean answer;

    public TF(typeQuestion tq, String line) {
        this.tq = tq;
        String[] parts = line.split(";");
        //0   1        2  3
        //TF;Question3;2;false
        this.text = parts[1];
        this.points = Integer.parseInt(parts[2]);
        this.answer = Boolean.parseBoolean(parts[3]);
    }

    @Override
    public String toString() {
        return String.format("True/False Question: %s Points: %d Answer: %s\n",
                text,points, answer);
    }

    @Override
    String getAnswer() {
        return String.valueOf(answer);
    }
}

class MC extends Question{
    String answer;

    public MC(typeQuestion tq, String line) throws InvalidOperationException {
        //0   1        2 3
        //MC;Question2;4;E
        String[] parts = line.split(";");
        if(parts[3].contains("A") || parts[3].contains("B") || parts[3].contains("C")
        || parts[3].contains("D") || parts[3].contains("E")) {
            this.text = parts[1];
            this.points = Integer.parseInt(parts[2]);
            this.answer = parts[3];
            this.tq = tq;
        }else {
            throw new InvalidOperationException(String.format("%s is not allowed option for this question",parts[3]));
        }
    }


    @Override
    public String toString() {
        return String.format("Multiple Choice Question: %s Points %d Answer: %s\n",
                text,points, answer);
    }

    @Override
    String getAnswer() {
        return answer;
    }
}

class InvalidOperationException extends Exception{
    public InvalidOperationException(String message) {
        super(message);
    }
}