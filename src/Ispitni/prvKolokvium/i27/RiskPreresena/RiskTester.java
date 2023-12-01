package Ispitni.prvKolokvium.i27.RiskPreresena;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class RiskTester {
    public static void main(String[] args) {
        Risk risk = new Risk();
        risk.processAttacksData(System.in);
    }
}

class Risk{

    List<RiskFactory> games;

    public Risk() {
        this.games = new ArrayList<>();
    }

    void processAttacksData (InputStream is){
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        games = br.lines().map(RiskFactory::create).filter(Objects::nonNull).collect(Collectors.toList());
        games.forEach(RiskFactory::attacks);
        games.forEach(i -> System.out.printf("%d %d%n",i.countAttacker,3 - i.countAttacker));
    }
}

class RiskFactory{
    List<Integer> attacker;
    List<Integer> defender;
    int countAttacker = 0;

    public RiskFactory(List<Integer> attacker, List<Integer> defender) {
        this.attacker = attacker;
        this.defender = defender;
    }

    static RiskFactory create(String line){
        //5 3 4;2 4 1
        String[] parts = line.split("\\s+");
        //5 3 4;2 4 1
        String[] parts2 = parts[2].split(";");
        //4 2
        List<Integer> atk = new ArrayList<>();
        atk.add(Integer.parseInt(parts[0]));//5
        atk.add(Integer.parseInt(parts[1]));//3
        atk.add(Integer.parseInt(parts2[0]));//4

        List<Integer> def = new ArrayList<>();
        def.add(Integer.parseInt(parts2[1]));//2
        def.add(Integer.parseInt(parts[3]));//4
        def.add(Integer.parseInt(parts[4]));//1

        return new RiskFactory(atk,def);
    }

    public void attacks(){
        attacker = attacker.stream().sorted().collect(Collectors.toList());
        defender = defender.stream().sorted().collect(Collectors.toList());
        for (int i = 0; i < attacker.size(); i++) {
            if(attacker.get(i) > defender.get(i)){
                countAttacker++;
            }
        }
    }
}
