package ru.klasix12.film_bot.service.impl;

import org.springframework.stereotype.Service;
import ru.klasix12.film_bot.model.Film;
import ru.klasix12.film_bot.repository.FilmRepository;
import ru.klasix12.film_bot.service.FilmService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;
    protected static final List<String> GENRES = Collections.unmodifiableList(Arrays.asList(
            "комедия",
            "мультфильм",
            "ужас",
            "фантастика",
            "триллер",
            "боевик",
            "мелодрама",
            "детектив",
            "приключение",
            "фэнтези",
            "военный",
            "семейный",
            "аниме",
            "исторический",
            "драма",
            "документальный",
            "детский",
            "криминал",
            "биография",
            "вестерн",
            "фильм-нуар",
            "спортивный",
            "реальное тв",
            "короткометражка",
            "музыкальный",
            "мюзикл",
            "ток-шоу",
            "игра"
    ));

    public FilmServiceImpl(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Override
    public void save(Film film) {
        filmRepository.save(film);
    }

    @Override
    public Optional<Film> getFilmById(long id) {
        return filmRepository.findById(id);
    }

    @Override
    public List<Film> findAll() {
        return filmRepository.findAll();
    }

    @Override
    public void remove(Film film) {
        filmRepository.delete(film);
    }

    @Override
    public List<Film> getFilmsByGenre(String genre) {
        return filmRepository.findAllByGenre(genre);
    }

    @Override
    public List<String> getGenres() {
        return GENRES;
    }
}
