package Ispitni.prvKolokvium.i24;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class Risk{
    List<Attack> game;

    public Risk() {
        game = new ArrayList<>();
    }

    public int processAttacksData(InputStream in) {
        int counter=0;
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        game = br.lines().map(line -> Attack.createAttack(line)).collect(Collectors.toList());
        for(Attack a : game){
            if(a.succAcc){
                counter++;
            }
        }
        return counter;
    }
}

class Attack{
    List<Integer> attacker;
    List<Integer> defender;

    boolean succAcc;

    public Attack(List<Integer> attacker, List<Integer> defender, boolean succAcc) {
        this.attacker = attacker;
        this.defender = defender;
        this.succAcc = succAcc;
    }

    public static Attack createAttack(String line){
        String[] parts = line.split("\\s+");
        String[] middle = parts[2].split(";");
        List<Integer> first = new ArrayList<>();
        List<Integer> second = new ArrayList<>();
        first.add(Integer.parseInt(parts[0]));
        first.add(Integer.parseInt(parts[1]));
        first.add(Integer.parseInt(middle[0]));

        second.add(Integer.parseInt(middle[1]));
        second.add(Integer.parseInt(parts[3]));
        second.add(Integer.parseInt(parts[4]));
        boolean tmp = successfulAttack(first,second);
        return new Attack(first,second,tmp);
    }
    public static boolean successfulAttack(List<Integer> first, List<Integer> second){
        List<Integer> sortedFirst = first.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        List<Integer> sortedSecond = second.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        for(int i=0;i<sortedFirst.size();i++){
            if(sortedFirst.get(i)<=sortedSecond.get(i)){
                return false;
            }
        }
        return true;
    }
}
public class RiskTester {
    public static void main(String[] args) {

        Risk risk = new Risk();

        System.out.println(risk.processAttacksData(System.in));

    }
}
