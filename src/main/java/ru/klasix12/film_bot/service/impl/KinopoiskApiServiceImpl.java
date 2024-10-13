package ru.klasix12.film_bot.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import ru.klasix12.film_bot.model.Film;
import ru.klasix12.film_bot.service.KinopoiskApiService;

import java.util.Optional;


@Service
public class KinopoiskApiServiceImpl implements KinopoiskApiService {

    private static final String DEFAULT_URI = "https://kinopoiskapiunofficial.tech/api/v2.2/films";
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
            Film response = restTemplate.exchange(uri, HttpMethod.GET, entity, Film.class).getBody();
            response.setUrl(uri);
            return Optional.of(response);
        } catch (HttpClientErrorException | NullPointerException e) {
            return Optional.empty();
        }
    }
}
