package app.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MovieDTO {
    private int id;
    private String title;

    @JsonProperty("original_title")
    private String originalTitle;

    @JsonProperty("release_date")
    private String releaseDate;

    private double popularity;

    @JsonProperty("vote_average")
    private double voteAverage;


    private String overview;

    @JsonProperty("original_language")
    private String originalLanguage;



    @JsonProperty("genre_ids")
    private List<Integer> genreIds;
}
