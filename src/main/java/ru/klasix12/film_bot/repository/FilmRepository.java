package ru.klasix12.film_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.klasix12.film_bot.model.Film;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
}
