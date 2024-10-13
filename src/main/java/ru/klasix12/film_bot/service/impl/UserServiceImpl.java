package ru.klasix12.film_bot.service.impl;

import org.springframework.stereotype.Service;
import ru.klasix12.film_bot.model.User;
import ru.klasix12.film_bot.repository.UserRepository;
import ru.klasix12.film_bot.service.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> getUserById(long id) {
        return userRepository.findById(id);
    }

    @Override
    public void save(User user) {
        userRepository.save(user);
    }
}
