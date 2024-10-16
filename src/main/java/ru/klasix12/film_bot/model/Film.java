package ru.klasix12.film_bot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "films")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Film {
    @Id
    private long kinopoiskId;
    private String nameRu;
    private double ratingKinopoisk;
    private double ratingImdb;
    @Column(columnDefinition = "TEXT")
    private String description;
    private String url;
    private String genres;
    @ManyToOne
    private User user;
    private boolean viewed;
}
