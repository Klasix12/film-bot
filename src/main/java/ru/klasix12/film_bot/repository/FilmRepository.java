package ru.klasix12.film_bot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.klasix12.film_bot.model.Film;

import java.util.List;
import java.util.Optional;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {

    @Query("SELECT f FROM Film f WHERE f.viewed = FALSE")
    List<Film> findAll();

    @Query("SELECT f FROM Film f WHERE f.genres LIKE %:genre%")
    List<Film> findAllByGenre(@Param("genre") String genre);

    Optional<Film> findByNameRuIgnoreCase(String filmName);
}
