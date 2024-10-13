package ru.klasix12.film_bot.service;

import ru.klasix12.film_bot.model.Film;

import java.util.Optional;

public interface KinopoiskApiService {
    Optional<Film> getFilmById(long filmId);

    static long extractIdFromKinopoiskUri(String uri) {
        // https://www.kinopoisk.ru/film/394/
        return Long.parseLong(uri.split("/")[4]);
    }
}
