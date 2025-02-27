package app.dao;

import app.entities.Actor;
import app.entities.Director;
import app.entities.Movie;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


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

                    // Håndter director
                    Director foundDirector = em.find(Director.class, movie.getDirector().getId());
                    if (foundDirector == null) {
                        em.persist(movie.getDirector());
                    } else {
                        movie.setDirector(foundDirector);
                    }

                    // Håndter actors
                    Set<Actor> managedActors = new HashSet<>();
                    for (Actor actor : movie.getActors()) {
                        Actor foundActor = em.find(Actor.class, actor.getId());
                        if (foundActor == null) {
                            em.persist(actor);
                            managedActors.add(actor);
                        } else {
                            managedActors.add(foundActor);
                        }
                    }

                    // Opdater filmen med de korrekte actors
                    movie.setActors(managedActors);

                    // Persistér filmen med de opdaterede relationer
                    em.persist(movie);
                }
            }
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
