package ru.klasix12.film_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.klasix12.film_bot.model.CurrentFilm;

@Repository
public interface CurrentFilmRepository extends JpaRepository<CurrentFilm, Long> {
    @Query("SELECT cf FROM CurrentFilm cf")
    CurrentFilm getCurrentFilm();

    @Modifying
    @Transactional
    @Query("DELETE FROM CurrentFilm cf")
    void removeCurrentFilm();
}
