package ru.klasix12.film_bot.service.impl;

import org.springframework.stereotype.Service;
import ru.klasix12.film_bot.model.Film;
import ru.klasix12.film_bot.model.User;
import ru.klasix12.film_bot.service.FilmBotService;
import ru.klasix12.film_bot.service.FilmService;
import ru.klasix12.film_bot.service.KinopoiskApiService;
import ru.klasix12.film_bot.service.UserService;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class FilmBotServiceImpl implements FilmBotService {

    private final KinopoiskApiService kinopoiskApiService;
    private final UserService userService;
    private final FilmService filmService;
    private final Random random = new Random();

    public FilmBotServiceImpl(KinopoiskApiService kinopoiskApiService, UserService userService, FilmService filmService) {
        this.kinopoiskApiService = kinopoiskApiService;
        this.userService = userService;
        this.filmService = filmService;
    }

    @Override
    public String addFilm(String uri, long userId, String username) {
        Optional<Film> existingFilm = filmService.getFilmById(KinopoiskApiService.extractIdFromKinopoiskUri(uri));
        if (existingFilm.isPresent()) {
            return existingFilm.get().getNameRu() + " уже был добавлен пользователем @" + existingFilm.get().getUser().getUsername();
        }
        Optional<Film> film = kinopoiskApiService.getFilmById(KinopoiskApiService.extractIdFromKinopoiskUri(uri));
        if (film.isEmpty()) {
            return "Такого фильма не существует";
        } else if (film.get().getRatingKinopoisk() < 7) {
            return film.get().getNameRu() + " слишком низкий рейтинг кинопоиска: " + film.get().getRatingKinopoisk();
        } else if (film.get().getRatingImdb() < 7) {
            return film.get().getNameRu() + " слишком низкий рейтинг IMDb: " + film.get().getRatingImdb();
        }
        User user = userService.getUserById(userId).orElse(User.builder()
                .username(username)
                .id(userId)
                .addedFilms(0)
                .build());
        user.setAddedFilms(user.getAddedFilms() + 1);
        film.get().setUser(user);
        userService.save(user);
        filmService.save(film.get());
        return String.format("""
                        @%s добавил фильм
                        %s
                        Оценка кинопоиск: %s
                        Оценка IMDb: %s
                        Жанры: %s
                        Описание: %s""",
                user.getUsername(),
                film.get().getNameRu(),
                film.get().getRatingKinopoisk(),
                film.get().getRatingImdb(),
                film.get().getGenres(),
                film.get().getDescription());
    }

    @Override
    public String findAll() {
        List<Film> films = filmService.findAll();
        if (films.isEmpty()) {
            return "Список фильмов пуст";
        }
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < films.size(); i++) {
            sb.append(i + 1).append(") ").append(films.get(i).getNameRu()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getRandomFilm() {
        List<Film> films = filmService.findAll();
        if (films.size() <= 0) {
            return "Список фильмов пуст";
        }
        Film film = films.get(random.nextInt(films.size()));
        filmService.remove(film);
        return formatFilm(film);
    }

    @Override
    public String findAllByGenre(String genre) {
        List<Film> films = filmService.getFilmsByGenre(genre);
        if (films.isEmpty()) {
            return "По вашему запросу ничего не нашлось :(";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < films.size(); i++) {
            sb.append(i + 1).append(") ").append(films.get(i).getNameRu()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String getRandomFilmByGenre(String genre) {
        List<Film> films = filmService.getFilmsByGenre(genre);
        if (films.isEmpty()) {
            return "По вашему запросу ничего не нашлось :(";
        }
        Film film = films.get(random.nextInt(films.size()));
        filmService.remove(film);
        return formatFilm(film);
    }

    @Override
    public String getGenres() {
        return String.join(", ", filmService.getGenres());
    }

    private String formatFilm(Film film) {
        return String.format("""
                        Выпал фильм: %s
                        Оценка кинопоиск: %s
                        Оценка IMDb: %s
                        Жанры: %s
                        Описание: %s
                        Ссылка: %s
                        Добавил: @%s
                        """,
                film.getNameRu(),
                film.getRatingKinopoisk(),
                film.getRatingImdb(),
                film.getGenres(),
                film.getDescription(),
                film.getUrl().replace("kino", "ss"),
                film.getUser().getUsername());
    }
}
