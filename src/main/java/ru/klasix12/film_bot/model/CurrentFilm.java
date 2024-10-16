package ru.klasix12.film_bot.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "current_film")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentFilm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    private Film film;

    private LocalDate date;
}
