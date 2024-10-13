package ru.klasix12.film_bot.service;

import ru.klasix12.film_bot.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> getUserById(long id);

    void save(User user);
}
