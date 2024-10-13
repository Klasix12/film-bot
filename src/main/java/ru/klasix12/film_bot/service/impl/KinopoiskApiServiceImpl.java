package ru.klasix12.film_bot.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.klasix12.film_bot.model.Film;
import ru.klasix12.film_bot.service.KinopoiskApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class KinopoiskApiServiceImpl implements KinopoiskApiService {

    private static final String DEFAULT_URI = "https://kinopoiskapiunofficial.tech/api/v2.2/films";
    private static final String KINOPOISK_URI = "https://www.kinopoisk.ru/film/";
    private final String apiKey;
    private final RestTemplate restTemplate;

    public KinopoiskApiServiceImpl(@Value("${kinopoisk.apikey}") String apiKey, RestTemplate restTemplate) {
        this.apiKey = apiKey;
        this.restTemplate = restTemplate;
    }

    @Override
    public Optional<Film> getFilmById(long filmId) {
        return getResponse(filmId);
    }

    private Optional<Film> getResponse(long filmId) {
        String uri = DEFAULT_URI + "/" + filmId;  // Correctly forming the URI

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-API-KEY", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            String response = restTemplate.exchange(uri, HttpMethod.GET, entity, String.class).getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            Film film = Film.builder()
                    .kinopoiskId(jsonNode.get("kinopoiskId").asLong())
                    .nameRu(jsonNode.get("nameRu").asText())
                    .ratingKinopoisk(jsonNode.get("ratingKinopoisk").asDouble())
                    .ratingImdb(jsonNode.get("ratingImdb").asDouble())
                    .description(jsonNode.get("description").asText())
                    .url(KINOPOISK_URI + filmId + "/")
                    .genres(extractGenres(jsonNode))
                    .build();
            return Optional.of(film);
        } catch (HttpClientErrorException | JsonProcessingException | NullPointerException e) {
            return Optional.empty();
        }
    }

    private String extractGenres(JsonNode jsonNode) {
        List<String> genres = new ArrayList<>();
        for (JsonNode genre : jsonNode.get("genres")) {
            genres.add(genre.get("genre").toString().replaceAll("\"", ""));
        }
        return String.join(", ", genres);
    }
}
