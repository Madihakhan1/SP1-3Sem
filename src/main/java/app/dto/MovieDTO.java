package app.dto;

import app.entities.Actor;
import app.entities.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private int id;
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date")
    private LocalDate releaseDate;

    private double popularity;

    @JsonProperty("vote_average")
    private double voteAverage;


    private String overview;

    @JsonProperty("original_language")
    private String originalLanguage;


    @JsonProperty("genre_ids")
    private List<Integer> genreIds;

    @JsonIgnore
    private DirectorDTO director;

    @JsonIgnore
    private List<ActorDTO> actors;

    public MovieDTO(Movie movie) {
        this.id = movie.getId();
        this.title = movie.getTitle();
        this.originalTitle = movie.getOriginalTitle();
        //this.releaseDate = movie.getReleaseDate();
        this.popularity = movie.getPopularity();
        this.voteAverage = movie.getVoteAverage();
        this.overview = movie.getOverview();
        //this.originalLanguage = movie.getOriginalLanguage();
        //this.genreIds = movie.getGenreIds();
    }

    public Movie getAsEntity() {

        Set<Actor> actorEntities = actors.stream().map(ActorDTO::getAsEntity).collect(Collectors.toSet());

        return Movie.builder()
                .id(id)
                .title(title)
                .originalTitle(originalTitle)
                .releaseDate(releaseDate)
                .voteAverage(voteAverage)
                .popularity(popularity)
                .overview(overview)
                .director(director.getAsEntity())
                .actors(actorEntities)
                .build();
    }

}
