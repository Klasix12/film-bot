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
    private static final String REMOVE = "/remove";
    private static final String INFO = "/info";
    private static final String REROLL = "/reroll";
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
        long chatId = update.getMessage().getChatId();

        if (command.equals(ROLL_FILM) || command.equals(ROLL_FILM + "@" + botUsername)) {
            sendMessage(chatId, filmBotService.getRandomFilm());
        } else if (command.split(" ").length == 2 && command.contains(ROLL_FILM)) {
            String genre = command.split(" ")[1];
            sendMessage(chatId, filmBotService.getRandomFilmByGenre(genre));
        } else if (command.contains("https://www.kinopoisk.ru/film/")) {
            sendMessage(chatId, filmBotService.addFilm(command, update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName()));
            removeMessage(chatId, update.getMessage().getMessageId());
        } else if (command.equals(FILMS) || command.equals(FILMS + "@" + botUsername)) {
            sendMessage(chatId, filmBotService.findAll());
        } else if (command.split(" ").length == 2 && command.contains(FILMS)) {
            String genre = command.split(" ")[1];
            sendMessage(chatId, filmBotService.findAllByGenre(genre));
        } else if (command.equals(HELP) || command.equals(HELP + "@" + botUsername)) {
            sendMessage(chatId, """
                    /roll_film - получить случайный фильм из всего списка фильмов
                    /roll_film <жанр> - получение случайного фильма указанного жанра
                    /films - получение списка всех фильмов
                    /films <жанр> - получение всех фильмов указанного жанра
                    /genres - жанры
                    """);
        } else if (command.contains(GENRES)) {
            sendMessage(chatId, filmBotService.getGenres());
        } else if (command.split(" ").length > 1 && command.contains(REMOVE)) {
            String filmName = extractFilmName(command, REMOVE);
            sendMessage(chatId, filmBotService.removeFilm(filmName));
        } else if (command.split(" ").length > 1 && command.contains(INFO)) {
            String filmName = extractFilmName(command, INFO);
            sendMessage(chatId, filmBotService.getFilmByName(filmName));
        } else if (command.contains(REROLL)) {
            sendMessage(chatId, filmBotService.rerollFilm(update.getMessage().getFrom().getUserName()));
        }
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    private String extractFilmName(String messageText, String command) {
        return messageText.substring(messageText.contains("@") ? command.length() + botUsername.length() + 2 : command.length() + 1);
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
        botCommandList.add(new BotCommand(INFO, "Получить информацию о фильме"));
        botCommandList.add(new BotCommand(REMOVE, "Удалить фильм"));
        botCommandList.add(new BotCommand(REROLL, "Реролл фильма"));
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
