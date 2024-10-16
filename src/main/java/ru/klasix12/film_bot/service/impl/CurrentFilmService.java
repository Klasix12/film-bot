package ru.klasix12.film_bot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.klasix12.film_bot.model.CurrentFilm;
import ru.klasix12.film_bot.repository.CurrentFilmRepository;

@Service
@RequiredArgsConstructor
public class CurrentFilmService {
    private final CurrentFilmRepository currentFilmRepository;

    public CurrentFilm findCurrentFilm() {
        return currentFilmRepository.getCurrentFilm();
    }

    public CurrentFilm save(CurrentFilm film) {
        return currentFilmRepository.save(film);
    }

    public void removeCurrentFilm() {
        currentFilmRepository.removeCurrentFilm();
    }
}
