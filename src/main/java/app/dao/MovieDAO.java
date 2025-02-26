package app.dao;

import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.List;



public class MovieDAO {

    private final EntityManagerFactory emf;

    public MovieDAO(EntityManagerFactory emf) {
        this.emf = emf;
    }


    public List<Movie> getMovies() {
        EntityManager em = emf.createEntityManager();
        try {
            return em.createQuery("SELECT m FROM Movie m", Movie.class).getResultList();
        } finally {
            em.close();
        }
    }

    public void saveMovies(List<Movie> movies) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            for (Movie movie : movies) {
                if (em.find(Movie.class, movie.getId()) == null) {
                    em.persist(movie);
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }
}
