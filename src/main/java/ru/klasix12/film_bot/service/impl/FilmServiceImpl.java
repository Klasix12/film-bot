package ru.klasix12.film_bot.service.impl;

import org.springframework.stereotype.Service;
import ru.klasix12.film_bot.model.Film;
import ru.klasix12.film_bot.repository.FilmRepository;
import ru.klasix12.film_bot.service.FilmService;

import java.util.List;
import java.util.Optional;

@Service
public class FilmServiceImpl implements FilmService {
    private final FilmRepository filmRepository;

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
}
