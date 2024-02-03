package Ispitni.kolokviumi2023_24.k1vlezna;

import java.io.*;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

class TeamRace{

    static void findBestTeam (InputStream is, OutputStream os) throws IOException {
        PrintWriter pw = new PrintWriter(os);
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        List<RaceContestant> raceContestants = br.lines().map(RaceContestant::new).collect(Collectors.toList());
        AtomicLong sec = new AtomicLong();
        raceContestants
                .stream()
                .sorted(Comparator.comparing(RaceContestant::getTime))
                .limit(4)
                .forEach(r -> {
                    pw.println(r);
                    sec.addAndGet(r.getTime());
                });
        pw.println(LocalTime.ofSecondOfDay(sec.longValue()));
        pw.flush();
    }

}
class RaceContestant{
    String id;
    LocalTime start;
    LocalTime end;

    public RaceContestant(String line) {
        //ID START_TIME END_TIME
        String[] parts = line.split("\\s+");
        id = parts[0];
        start = LocalTime.parse(parts[1]);
        end = LocalTime.parse(parts[2]);
    }
    public long getTime(){
        return Duration.between(start,end).getSeconds();
    }

    @Override
    public String toString() {
        return String.format("%s %s",id,LocalTime.ofSecondOfDay(getTime()).toString());
    }
}
public class RaceTest {


    public static void main(String[] args) {
        try {
            TeamRace.findBestTeam(System.in, System.out);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
