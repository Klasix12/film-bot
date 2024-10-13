package ru.klasix12.film_bot.service;

public interface FilmBotService {
    String addFilm(String uri, long userId, String username);

    String findAll();
    String getRandomFilm();
    String findAllByGenre(String genre);
    String getRandomFilmByGenre(String genre);
}
