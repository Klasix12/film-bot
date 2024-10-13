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
        } else if (command.contains("https://www.kinopoisk.ru/film/")) {
            sendMessage(update.getMessage().getChatId(), filmBotService.addFilm(command, update.getMessage().getFrom().getId(), update.getMessage().getFrom().getUserName()));
            removeMessage(update.getMessage().getChatId(), update.getMessage().getMessageId());
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
        try {
            this.execute(new SetMyCommands(botCommandList, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error(e.getMessage());
        }
    }
}
