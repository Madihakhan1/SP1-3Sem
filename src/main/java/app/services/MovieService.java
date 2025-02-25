package app.services;

import app.dto.TmdbResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MovieService {
    private static final String API_KEY = System.getenv("API_KEY"); // API Key from environment variable
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
        String uri = TMDB_DISCOVER_URL; // ✅ No API Key in URL

        try {
            System.out.println("Fetching movies from TMDb: " + uri);

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI(uri))
                    .header("accept", "application/json")
                    .header("Authorization", "Bearer " + API_KEY) // ✅ Correct way to pass API Key
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
}
