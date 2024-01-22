package Ispitni.vtorKolokviumIspit.i24;

import java.util.*;
import java.util.stream.Collectors;

class MoviesList{
    List<Movie> movies;

    public MoviesList() {
        movies = new ArrayList<>();
    }
    public void addMovie(String title, int[] ratings){
        movies.add(new Movie(title, ratings));
    }
    public List<Movie> top10ByAvgRating(){
        return movies.stream().sorted(Comparator.comparing(Movie::getAverageRating).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
    public List<Movie> top10ByRatingCoef(){
        movies.forEach(m -> m.setCoef(m.getAverageRating()*m.getRatings().length/getMaxRatings()));
        return movies.stream().sorted(Comparator.comparing(Movie::getCoef).reversed().thenComparing(Movie::getTitle)).limit(10).collect(Collectors.toList());
    }
    public  double getMaxRatings(){
        double sum = 0;
        for (Movie movie : movies) {
            sum+=movie.ratings.length;
        }
        return sum;
    }

}

class Movie{
    String title;
    int[] ratings;
    double coef;

    public void setCoef(double coef) {
        this.coef = coef;
    }

    public double getCoef() {
        return coef;
    }

    public Movie(String title, int[] ratings) {
        this.title = title;
        this.ratings = ratings;
    }

    public String getTitle() {
        return title;
    }

    public int[] getRatings() {
        return ratings;
    }
    public double getAverageRating(){
        double sum  = 0;
        for (int rating : ratings) {
            sum+=rating;
        }
        return sum/ratings.length;
    }

    @Override
    public String toString() {
        return String.format("%s (%.2f) of %d ratings",
                title,getAverageRating(),ratings.length);
    }
}
public class MoviesTest {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        MoviesList moviesList = new MoviesList();
        int n = scanner.nextInt();
        scanner.nextLine();
        for (int i = 0; i < n; ++i) {
            String title = scanner.nextLine();
            int x = scanner.nextInt();
            int[] ratings = new int[x];
            for (int j = 0; j < x; ++j) {
                ratings[j] = scanner.nextInt();
            }
            scanner.nextLine();
            moviesList.addMovie(title, ratings);
        }
        scanner.close();
        List<Movie> movies = moviesList.top10ByAvgRating();
        System.out.println("=== TOP 10 BY AVERAGE RATING ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
        movies = moviesList.top10ByRatingCoef();
        System.out.println("=== TOP 10 BY RATING COEFFICIENT ===");
        for (Movie movie : movies) {
            System.out.println(movie);
        }
    }
}

// vashiot kod ovde