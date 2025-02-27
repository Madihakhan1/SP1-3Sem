package app.services;

import app.dto.ActorDTO;
import app.dto.DirectorDTO;
import app.dto.TmdbResponseDTO;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class MovieService {
    private static final String API_KEY = System.getenv("API_KEY");
    private static final String TMDB_DISCOVER_URL = "https://api.themoviedb.org/3/discover/movie?"
            + "include_adult=false&include_video=false&language=en-US&page=1"
            + "&release_date.gte=2020-02-25&release_date.lte=2025-02-25"
            + "&sort_by=popularity.desc&with_original_language=da";


    public static TmdbResponseDTO fetchMoviesFromTMDb() {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API_KEY is not set! Please configure it as an environment variable.");
        }

        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String uri = TMDB_DISCOVER_URL;

        try {
            System.out.println("Fetching movies from TMDb: " + uri);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), TmdbResponseDTO.class);
            } else {
                System.out.println("TMDb API GET request failed. Status code: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static DirectorDTO fetchDirectorFromMovie(int movieId) {

        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API_KEY is not set! Please configure it as an environment variable.");
        }

        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String uri = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=da";


        try {
            System.out.println("Fetching movies from TMDb: " + uri);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {

                JsonNode node = objectMapper.readTree(response.body());
                JsonNode crew = node.get("crew");

                DirectorDTO directorDTO = null;

                for (JsonNode crewNode : crew) {
                    if (crewNode.get("job").asText().equalsIgnoreCase("Director")) {
                        directorDTO = objectMapper.treeToValue(crewNode, DirectorDTO.class); //Tag en crewNode (json node)  og lav den om til en directorDTO og det gør man ved at bruge treeToValue.
                        break;
                    }
                }
                return directorDTO;
            } else {
                System.out.println("TMDb API GET request failed. Status code: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;


    }

    public static List<ActorDTO> fetchActorsFromMovie(int movieId) {
        if (API_KEY == null || API_KEY.isEmpty()) {
            throw new IllegalStateException("API_KEY is not set! Please configure it as an environment variable.");
        }

        HttpResponse<String> response;
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        String uri = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?language=da";

        try {
            System.out.println("Fetching actors from TMDb: " + uri);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + API_KEY)
                    .GET()
                    .build();

            response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 200) {
                JsonNode node = objectMapper.readTree(response.body());
                JsonNode castArray = node.get("cast");

                List<ActorDTO> actors = new ArrayList<>();

                if (castArray != null) {
                    for (JsonNode actorNode : castArray) {
                        ActorDTO actor = new ActorDTO();
                        actor.setId(actorNode.get("id").asInt()); // Hent id fra JSON
                        actor.setName(actorNode.get("name").asText()); // Hent navn fra JSON
                        actors.add(actor);
                    }
                }

                return actors;
            } else {
                System.out.println("TMDb API GET request failed. Status code: " + response.statusCode());
                System.out.println("Response: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }


}
