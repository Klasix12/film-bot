package ru.klasix12.film_bot.bot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.klasix12.film_bot.service.FilmBotService;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class FilmBot extends TelegramLongPollingBot {

    private final String botUsername;
    private static final String ROLL_FILM = "/roll_film";
    private static final String FILMS = "/films";
    private static final String HELP = "/help";
    private static final String GENRES = "/genres";
    private final FilmBotService filmBotService;

    public FilmBot(@Value("${bot.token}") String botToken, @Value("${bot.username}") String botUsername, FilmBotService filmBotService) {
        super(botToken);
        this.botUsername = botUsername;
        this.filmBotService = filmBotService;
        registerBotCommand();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (!update.hasMessage() || !update.getMessage().hasText()) {
            return;
        }
        log.trace(update.toString());
        String command = update.getMessage().getText();
        if (command.equals(ROLL_FILM) || command.equals(ROLL_FILM + "@" + botUsername)) {
            sendMessage(update.getMessage().getChatId(), filmBotService.getRandomFilm());
        } else if (command.split(" ").length == 2 && command.contains(ROLL_FILM)) {
            String genre = command.split(" ")[1];
            sendMessage(update.getMessage().getChatId(), filmBotService.getRandomFilmByGenre(genre));
        } else if (command.contains("https://www.kinopoisk.ru/film/")) {
            sendMessage(update.getMessage().getChatId(), filmBotService.addFilm(command, update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName()));
            removeMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
        } else if (command.equals(FILMS) || command.equals(FILMS + "@" + botUsername)) {
            sendMessage(update.getMessage().getChatId(), filmBotService.findAll());
        } else if (command.split(" ").length == 2 && command.contains(FILMS)) {
            String genre = command.split(" ")[1];
            sendMessage(update.getMessage().getChatId(), filmBotService.findAllByGenre(genre));
        } else if (command.equals(HELP) || command.equals(HELP + "@" + botUsername)) {
            sendMessage(update.getMessage().getChatId(), """
                    /roll_film - получить случайный фильм из всего списка фильмов
                    /roll_film <жанр> - получение случайного фильма указанного жанра
                    /films - получение списка всех фильмов
                    /films <жанр> - получение всех фильмов указанного жанра
                    /genres - жанры
                    """);
        } else if (command.equals(GENRES) || command.equals(GENRES + "@" + botUsername)) {
            sendMessage(update.getMessage().getChatId(), filmBotService.getGenres());
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private void sendMessage(long chatId, String message) {
        SendMessage sendMessage = new SendMessage(String.valueOf(chatId), message);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void removeMessage(long chatId, int messageId) {
        DeleteMessage deleteMessage = new DeleteMessage(String.valueOf(chatId), messageId);
        try {
            execute(deleteMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    private void registerBotCommand() {
        List<BotCommand> botCommandList = new ArrayList<>();
        botCommandList.add(new BotCommand(ROLL_FILM, "Получить случайный фильм"));
        botCommandList.add(new BotCommand(FILMS, "Получить список всех фильмов"));
        botCommandList.add(new BotCommand(HELP, "Получить информацию о боте"));
        botCommandList.add(new BotCommand(GENRES, "Получить список жанров"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
