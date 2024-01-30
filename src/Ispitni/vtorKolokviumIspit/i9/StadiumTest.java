package Ispitni.vtorKolokviumIspit.i9;

import java.util.*;

class SeatNotAllowedException extends Exception{
    public SeatNotAllowedException() {
    }
}

class SeatTakenException extends Exception{
    public SeatTakenException() {
    }
}

class Sector{
    String kod;
    int brojMestaZaSedenje;
    int vkBrojMesta;
    Map<Integer,Integer> zafatenostNaMesta;

    public Sector(String kod, int brojMestaZaSedenje) {
        zafatenostNaMesta = new TreeMap<>();
        this.kod = kod;
        this.brojMestaZaSedenje = brojMestaZaSedenje;
        this.vkBrojMesta = brojMestaZaSedenje;
    }
    public void takeSeat(int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Integer mesto = zafatenostNaMesta.get(seat);
        if (mesto==null){
            Integer i = type;
            if((i==1 && zafatenostNaMesta.containsValue(2)) || (i==2 && zafatenostNaMesta.containsValue(1))){
                throw new SeatNotAllowedException();
            }
            zafatenostNaMesta.put(seat,type);
            brojMestaZaSedenje--;
        }else{
            throw new SeatTakenException();
        }
    }

    public int getBrojMestaZaSedenje() {
        return brojMestaZaSedenje;
    }

    public String getKod() {
        return kod;
    }

    @Override
    public String toString() {
        return String.format("%s\t%d/%d\t%.1f%%",kod,brojMestaZaSedenje,vkBrojMesta,(1-((double)brojMestaZaSedenje/vkBrojMesta))*100);
    }
}

class Stadium{
    String ime;
    Map<String,Sector> sektori;
    Stadium(String name){
        this.ime = name;
        sektori = new TreeMap<>();
    }
    void createSectors(String[] sectorNames, int[] sizes){
        for(int i=0;i<sectorNames.length;i++){
            Sector sector = new Sector(sectorNames[i],sizes[i]);
            sektori.putIfAbsent(sector.kod,sector);
        }
    }
    void buyTicket(String sectorName, int seat, int type) throws SeatTakenException, SeatNotAllowedException {
        Sector s = sektori.get(sectorName);
        s.takeSeat(seat,type);
    }
    void showSectors(){
        sektori
                .values()
                .stream()
                .sorted(Comparator.comparing(Sector::getBrojMestaZaSedenje).reversed().thenComparing(Sector::getKod))
                .forEach(System.out::println);
    }
}

public class StadiumTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        scanner.nextLine();
        String[] sectorNames = new String[n];
        int[] sectorSizes = new int[n];
        String name = scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            sectorNames[i] = parts[0];
            sectorSizes[i] = Integer.parseInt(parts[1]);
        }
        Stadium stadium = new Stadium(name);
        stadium.createSectors(sectorNames, sectorSizes);
        n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String line = scanner.nextLine();
            String[] parts = line.split(";");
            try {
                stadium.buyTicket(parts[0], Integer.parseInt(parts[1]),
                        Integer.parseInt(parts[2]));
            } catch (SeatNotAllowedException e) {
                System.out.println("SeatNotAllowedException");
            } catch (SeatTakenException e) {
                System.out.println("SeatTakenException");
            }
        }
        stadium.showSectors();
    }
}

