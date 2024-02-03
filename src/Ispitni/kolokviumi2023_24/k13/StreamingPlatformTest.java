package Ispitni.kolokviumi2023_24.k13;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;

class StreamingPlatform{
    List<Item> items = new ArrayList<>();
    void addItem (String data){
        Item item = ItemFactory.createItem(data);
        items.add(item);
    }
    void listAllItems (OutputStream os){
        List<Item> temp = new ArrayList<>();
        temp = items.stream().sorted(Comparator.comparingDouble(Item::rating).reversed()).collect(Collectors.toList());
        PrintWriter pw = new PrintWriter(os);
        temp.forEach(pw::println);
        pw.flush();
    }
    void listFromGenre (String genre, OutputStream os){
        List<Item> temp = new ArrayList<>();
        temp = items.stream().sorted(Comparator.comparingDouble(Item::rating).reversed()).collect(Collectors.toList());
        PrintWriter pw = new PrintWriter(os);
        temp.stream().filter(i -> i.genres.contains(genre)).forEach(pw::println);
        pw.flush();
    }
}

class ItemFactory{
    public static Item createItem(String line){
        //NAME;genre1,genre2,…,genreN;rating1 rating2 rating3 .. ratingN
        String[] parts = line.split(";");
        String name = parts[0];
        List<String> g = new ArrayList<>();
        String[] genres = parts[1].split(",");
        g.addAll(Arrays.asList(genres));
        if(parts.length==3){
            List<Integer> r = new ArrayList<>();
            String[] ratings = parts[2].split("\\s+");
            for (String rating : ratings) {
                r.add(Integer.parseInt(rating));
            }
            return new Movie(name,g,r);
        }else{
            Map<String,List<Integer>> episodesWithRatings = new TreeMap<>();
            for (int i = 2; i < parts.length; i++) {
                String[] episode = parts[i].split("\\s+");
                List<Integer> r = episodesWithRatings.get(episode[0]);
                if(r == null){
                    r = new ArrayList<>();
                }
                for (int j = 1; j < episode.length; j++) {
                    String s = episode[j];
                    r.add(Integer.parseInt(s));
                }
                episodesWithRatings.putIfAbsent(episode[0],r);
            }
            return new TvShow(name,g,episodesWithRatings);
        }
    }
}
abstract class Item{
    String name;
    List<String> genres;

    public Item(String name, List<String> genres) {
        this.name = name;
        this.genres = genres;
    }
    abstract double calculateRating();
    abstract double rating();
}

class Movie extends Item{
    List<Integer> ratings;

    public Movie(String name, List<String> genres, List<Integer> ratings) {
        super(name, genres);
        this.ratings = ratings;
    }

    @Override
    double calculateRating() {
        return ratings.stream().mapToDouble(i -> i).average().orElse(0.0) * Math.min(ratings.size()/20.0,1.0);
    }

    @Override
    double rating() {
        return calculateRating();
    }

    @Override
    public String toString() {
        return String.format("Movie %s %.4f",name,calculateRating());
    }

}

class TvShow extends Item{
    Map<String,List<Integer>> ratingsForEpisodes;
    List<Double> topThreeEpisodesByRatings;
    double rating;

    public TvShow(String name, List<String> genres, Map<String, List<Integer>> ratingsForEpisodes) {
        super(name, genres);
        this.ratingsForEpisodes = ratingsForEpisodes;
        topThreeEpisodesByRatings = new ArrayList<>();
        rating=0;
    }

    double calculateRatingForEpisode(String episode) {
        List<Integer> ratings = ratingsForEpisodes.get(episode);
        return ratings.stream().mapToDouble(i -> i).average().orElse(0.0) * Math.min(ratings.size()/ 20.0, 1.0);
    }

    public int getNumberEpisodes(){
        return ratingsForEpisodes.size();
    }
    @Override
    double calculateRating() {
        for (String s : ratingsForEpisodes.keySet()) {
            List<Integer> ratings = ratingsForEpisodes.get(s);
            double r = ratings.stream().mapToDouble(i -> i).average().orElse(0.0) * Math.min(ratings.size() / 20.0, 1.0);
            topThreeEpisodesByRatings.add(r);
        }
        topThreeEpisodesByRatings = topThreeEpisodesByRatings.stream().sorted(Comparator.reverseOrder()).collect(Collectors.toList());
        double sum=0;
        for (int i = 0; i < 3; i++) {
            sum+=topThreeEpisodesByRatings.get(i);
        }
        rating=sum/3;
        return sum/3.0;
    }

    @Override
    double rating() {
        return rating;
    }

    @Override
    public String toString() {
        return String.format("TV Show %s %.4f (%d episodes)",name,calculateRating(),getNumberEpisodes());
    }
}
public class StreamingPlatformTest {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StreamingPlatform sp = new StreamingPlatform();
        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split(" ");
            String method = parts[0];
            String data = Arrays.stream(parts).skip(1).collect(Collectors.joining(" "));
            if (method.equals("addItem")){
                sp.addItem(data);
            }
            else if (method.equals("listAllItems")){
                sp.listAllItems(System.out);
            } else if (method.equals("listFromGenre")){
                System.out.println(data);
                sp.listFromGenre(data, System.out);
            }
        }

    }
}

