package app;

import app.dto.TmdbResponseDTO;
import app.dto.MovieDTO;
import app.services.MovieService;

public class Main {
    public static void main(String[] args) {
        TmdbResponseDTO response = MovieService.fetchMoviesFromTMDb();

        if (response != null) {
            System.out.println("Movies from TMDb:");
            for (MovieDTO movie : response.getResults()) {
                System.out.println("Title: " + movie.getTitle());
                System.out.println("Release Date: " + movie.getReleaseDate());
                System.out.println("Rating: " + movie.getVoteAverage());
                System.out.println("Genres: " + movie.getGenreIds());
                System.out.println("------------------------------------");
            }
        } else {
            System.out.println("Could not fetch movies from TMDb.");
        }
    }
}
