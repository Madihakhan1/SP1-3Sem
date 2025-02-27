package app;

import app.config.HibernateConfig;
import app.dao.MovieDAO;
import app.dto.DirectorDTO;
import app.dto.TmdbResponseDTO;
import app.dto.MovieDTO;
import app.dto.ActorDTO;
import app.entities.Director;
import app.entities.Movie;
import app.entities.Actor;
import app.services.MovieService;
import jakarta.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        EntityManagerFactory emf = HibernateConfig.getEntityManagerFactory();
        MovieDAO movieDAO = new MovieDAO(emf);

        TmdbResponseDTO response = MovieService.fetchMoviesFromTMDb();

        if (response != null && response.getResults() != null) {
            List<Movie> moviesToSave = new ArrayList<>();

            for (MovieDTO movieDTO : response.getResults()) {

                // Hent director og tilføj til filmen
                DirectorDTO directorDTO = MovieService.fetchDirectorFromMovie(movieDTO.getId());
                movieDTO.setDirector(directorDTO);

                // Hent skuespillere og tilføj til filmen
                List<ActorDTO> actorDTOs = MovieService.fetchActorsFromMovie(movieDTO.getId());
                movieDTO.setActors(actorDTOs);

                // Konverter til entity
                Movie movie = movieDTO.getAsEntity();
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
}
