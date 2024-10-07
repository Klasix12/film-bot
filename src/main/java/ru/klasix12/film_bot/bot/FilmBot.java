package ru.klasix12.film_bot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import ru.klasix12.film_bot.service.FilmBotService;

@Component
@Slf4j
public class FilmBot extends TelegramLongPollingBot {

    private final String botUsername;
    private static final String START = "/start";
    private static final String INFO = "/info";
    private static final String ADD_FILM = "/add_film";
    private static final String ROLL_FILM = "/roll_film";
    private final FilmBotService filmBotService;

    public FilmBot(@Value("${bot.token}") String botToken, @Value("${bot.username}")String botUsername, FilmBotService filmBotService) {
        super(botToken);
        this.botUsername = botUsername;
        this.filmBotService = filmBotService;
    }

    @Override
    public void onUpdateReceived(Update update) {

    }

    @Override
    public String getBotUsername() {
        return null;
    }
}
