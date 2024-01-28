package Ispitni.vtorKolokviumIspit.i29;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * Partial exam II 2016/2017
 */

class FootballTable{

    Map<String,Team> teams;

    public FootballTable() {
        teams = new HashMap<>();
    }

    public void addGame(String homeTeam, String awayTeam, int homeGoals, int awayGoals){
        Team home = teams.computeIfAbsent(homeTeam, k -> new Team(homeTeam));
        Team away = teams.computeIfAbsent(awayTeam,k -> new Team(awayTeam));
        home.goals +=homeGoals;
        away.goals += awayGoals;
        home.goalsConceded +=awayGoals;
        away.goalsConceded +=homeGoals;
        away.matchNumber++;
        home.matchNumber++;
        if(homeGoals>awayGoals){
            home.wins++;
            away.loss++;
        }else if(homeGoals<awayGoals){
            home.loss++;
            away.wins++;
        }else{
            home.draw++;
            away.draw++;
        }
    }
    public void printTable(){
        AtomicInteger i= new AtomicInteger(1);
        List<Team> teamList = teams.values().stream().sorted(Comparator.comparing(Team::getPoints).thenComparing(Team::goalDifference).reversed().thenComparing(Team::getName)).collect(Collectors.toList());
        teamList.forEach((k -> System.out.printf("%2d. %s", i.getAndIncrement(), k.toString())));
    }
}

class Team{
    String name;
    int goals;
    int matchNumber;
    int wins;
    int loss;
    int draw;
    int goalsConceded;

    public int getGoalsConceded() {
        return goalsConceded;
    }

    public Team(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getGoals() {
        return goals;
    }

    public int getMatchNumber() {
        return matchNumber;
    }

    public int getWins() {
        return wins;
    }

    public int getLoss() {
        return loss;
    }

    public int getDraw() {
        return draw;
    }

    public int goalDifference(){
        return goals - goalsConceded;
    }

    public int getPoints() {
        return wins*3+draw;
    }

    @Override
    public String toString() {
        return String.format("%-15s%5d%5d%5d%5d%5d\n",name,matchNumber,wins,draw,loss,getPoints());
    }
}
public class FootballTableTest {
    public static void main(String[] args) throws IOException {
        FootballTable table = new FootballTable();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        reader.lines()
                .map(line -> line.split(";"))
                .forEach(parts -> table.addGame(parts[0], parts[1],
                        Integer.parseInt(parts[2]),
                        Integer.parseInt(parts[3])));
        reader.close();
        System.out.println("=== TABLE ===");
        System.out.printf("%-19s%5s%5s%5s%5s%5s\n", "Team", "P", "W", "D", "L", "PTS");
        table.printTable();
    }
}

// Your code here


