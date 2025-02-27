package app.dto;

import app.entities.Director;
import app.entities.Movie;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DirectorDTO {

    private Integer id;

    private String name;

    private Set<MovieDTO> movies;

    public DirectorDTO(Director director) {
        this.id = director.getId();
        this.name = director.getName();
    }

    @JsonIgnore
    public Director getAsEntity() {
        return Director.builder()
                .id(id)
                .name(name)
                .build();
    }


}



