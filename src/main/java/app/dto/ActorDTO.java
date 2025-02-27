package app.dto;


import app.entities.Actor;
import app.entities.Director;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ActorDTO {

    private String name;
    private int id;

    private Set<MovieDTO> movies;

    @JsonIgnore
    public Actor getAsEntity() {
        return Actor.builder()
                .id(id)
                .name(name)
                .build();
    }



}
