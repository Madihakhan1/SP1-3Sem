package app;

import app.config.HibernateConfig;
import app.dao.MovieDAO;
import app.dto.TmdbResponseDTO;
import app.dto.MovieDTO;
import app.entities.Movie;
import app.services.MovieService;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieDAO movieDAO = new MovieDAO(emf);

        TmdbResponseDTO response = MovieService.fetchMoviesFromTMDb();

        if (response != null && response.getResults() != null) {
            List<Movie> moviesToSave = new ArrayList<>();

            for (MovieDTO movieDTO : response.getResults()) {
                Movie movie = Movie.builder()
                        .id(movieDTO.getId())
                        .title(movieDTO.getTitle())
                        .originalTitle(movieDTO.getOriginalTitle())
                        .releaseDate(parseDate(movieDTO.getReleaseDate()))
                        .voteAverage(movieDTO.getVoteAverage())
                        .popularity(movieDTO.getPopularity())
                        .overview(movieDTO.getOverview())
                        .build();

                moviesToSave.add(movie);
            }

            if (!moviesToSave.isEmpty()) {
                movieDAO.saveMovies(moviesToSave);
            }

            List<Movie> savedMovies = movieDAO.getMovies();
            for (Movie m : savedMovies) {
                System.out.println(m.getTitle() + " (" + m.getReleaseDate() + ")");
            }
        }

        emf.close();
    }

    private static LocalDate parseDate(String dateStr) {
        try {
            return LocalDate.parse(dateStr);
        } catch (Exception e) {
            return null;
        }
    }




}
