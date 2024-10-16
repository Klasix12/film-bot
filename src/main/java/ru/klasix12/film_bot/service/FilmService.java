package ru.klasix12.film_bot.service;

import ru.klasix12.film_bot.model.Film;

import java.util.List;
import java.util.Optional;

public interface FilmService {
    void save(Film film);

    Optional<Film> getFilmById(long id);

    List<Film> findAll();

    void remove(Film film);
    List<Film> getFilmsByGenre(String genre);
    List<String> getGenres();
    Optional<Film> findByName(String filmName);
}
