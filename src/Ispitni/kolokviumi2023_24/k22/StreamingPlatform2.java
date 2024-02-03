package Ispitni.kolokviumi2023_24.k22;

import java.util.*;
import java.util.stream.Collectors;

class CosineSimilarityCalculator {

    public static double cosineSimilarity(Map<String, Integer> c1, Map<String, Integer> c2) {
        return cosineSimilarity(c1.values(), c2.values());
    }

    public static double cosineSimilarity(Collection<Integer> c1, Collection<Integer> c2) {
        int[] array1;
        int[] array2;
        array1 = c1.stream().mapToInt(i -> i).toArray();
        array2 = c2.stream().mapToInt(i -> i).toArray();
        double up = 0.0;
        double down1 = 0, down2 = 0;

        for (int i = 0; i < c1.size(); i++) {
            up += (array1[i] * array2[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down1 += (array1[i] * array1[i]);
        }

        for (int i = 0; i < c1.size(); i++) {
            down2 += (array2[i] * array2[i]);
        }

        return up / (Math.sqrt(down1) * Math.sqrt(down2));
    }
}

class StreamingPlatform{
    Map<String,Movie> movies;
    Map<String,User> users;

    public StreamingPlatform() {
        this.movies = new HashMap<>();
        this.users = new HashMap<>();
    }

    void addMovie (String id, String name){
        movies.putIfAbsent(id,new Movie(id,name));
    }
    void addUser (String id, String username){
        users.putIfAbsent(id,new User(id,username));
    }
    void addRating (String userId, String movieId, int rating){
        users.get(userId).addMovie(movieId, rating);
        movies.get(movieId).addRating(rating);
    }
    void topNMovies (int n){
        Comparator<Movie> c = Comparator.comparing(Movie::getAverageRating).reversed();
        movies.entrySet().stream().sorted(Map.Entry.comparingByValue(c)).limit(n).forEach(j -> System.out.println(j.getValue()));
    }
    void favouriteMoviesForUsers(List<String> userIds){
        for (String userId : userIds) {
            User user = users.get(userId);
            List<String> favorites = user.sortedFavorites();
            System.out.println(user);
            List<Movie> m = new ArrayList<>();
            for (String favorite : favorites) {
                m.add(movies.get(favorite));
            }
            Comparator<Movie> c = Comparator.comparing(Movie::getAverageRating).thenComparing(Movie::getName).reversed();
            m.stream().sorted(c).forEach(System.out::println);
            System.out.println();
        }
    }
    void similarUsers(String userId){
        Set<String> movieIds = movies.keySet();
        for (User value : users.values()) {
            for (String movieId : movieIds) {
                value.userMovies.putIfAbsent(movieId,0);
            }
        }
        User u1 = users.get(userId);
        users.values().stream().filter(i -> !i.id.equals(userId))
                .sorted((u,u2) -> Double.compare(
                        CosineSimilarityCalculator.cosineSimilarity(u2.userMovies,u1.userMovies),
                        CosineSimilarityCalculator.cosineSimilarity(u.userMovies,u1.userMovies)
                ))
                .forEach(u -> System.out.println(u + " " + CosineSimilarityCalculator.cosineSimilarity(u.userMovies, u1.userMovies)));
    }
}

class Movie{
    String id;
    String name;
    List<Integer> ratings;

    public Movie(String id, String name) {
        this.id = id;
        this.name = name;
        ratings = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    void addRating(int rating){
        ratings.add(rating);
    }
    public double getAverageRating(){
        return ratings.stream().mapToDouble(i -> i).average().orElse(0.0);
    }

    @Override
    public String toString() {
       return String.format("Movie ID: %s Title: %s Rating: %.2f",id,name,getAverageRating());
    }
}
class User{
    String id;
    String username;
    Map<String,Integer> userMovies;

    public User(String id, String username) {
        this.id = id;
        this.username = username;
        userMovies = new HashMap<>();
    }
    void addMovie(String movieId, int rating){
        userMovies.putIfAbsent(movieId,rating);
    }
    List<String> sortedFavorites(){
        int max = userMovies.values().stream().mapToInt(i -> i).max().orElse(1);
        return userMovies.entrySet().stream().filter(e -> e.getValue() == max)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
    @Override
    public String toString() {
        return String.format("User ID: %s Name: %s",id,username);
    }
}

public class StreamingPlatform2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        StreamingPlatform sp = new StreamingPlatform();

        while (sc.hasNextLine()){
            String line = sc.nextLine();
            String [] parts = line.split("\\s+");

            if (parts[0].equals("addMovie")) {
                String id = parts[1];
                String name = Arrays.stream(parts).skip(2).collect(Collectors.joining(" "));
                sp.addMovie(id ,name);
            } else if (parts[0].equals("addUser")){
                String id = parts[1];
                String name = parts[2];
                sp.addUser(id ,name);
            } else if (parts[0].equals("addRating")){
                //String userId, String movieId, int rating
                String userId = parts[1];
                String movieId = parts[2];
                int rating = Integer.parseInt(parts[3]);
                sp.addRating(userId, movieId, rating);
            } else if (parts[0].equals("topNMovies")){
                int n = Integer.parseInt(parts[1]);
                System.out.println("TOP " + n + " MOVIES:");
                sp.topNMovies(n);
            } else if (parts[0].equals("favouriteMoviesForUsers")) {
                List<String> users = Arrays.stream(parts).skip(1).collect(Collectors.toList());
                System.out.println("FAVOURITE MOVIES FOR USERS WITH IDS: " + users.stream().collect(Collectors.joining(", ")));
                sp.favouriteMoviesForUsers(users);
            } else if (parts[0].equals("similarUsers")) {
                String userId = parts[1];
                System.out.println("SIMILAR USERS TO USER WITH ID: " + userId);
                sp.similarUsers(userId);
            }
        }
    }
}

