package ru.klasix12.film_bot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import ru.klasix12.film_bot.bot.FilmBot;

@Configuration
public class FilmBotConfiguration {
    @Bean
    public TelegramBotsApi telegramBotsApi(FilmBot filmBot) throws TelegramApiException {
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        api.registerBot(filmBot);
        return api;
    }
}
