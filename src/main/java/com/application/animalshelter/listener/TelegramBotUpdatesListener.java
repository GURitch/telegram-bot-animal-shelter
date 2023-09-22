package com.application.animalshelter.listener;

import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.entıty.AppUser;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.User;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Класс, отвечающий за обработку обновлений бота Telegram.
 * Слушает обновления и передает их на обработку соответствующим методам.
 */
@Service
public class TelegramBotUpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final MessageBuilder messageBuilder;
    private final AppUserDAO appUserDAO;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, MessageBuilder messageBuilder, AppUserDAO appUserDAO) {
        this.telegramBot = telegramBot;
        this.messageBuilder = messageBuilder;
        this.appUserDAO = appUserDAO;
    }

    /**
     * Метод, выполняющийся после инициализации класса.
     * Устанавливает слушателя для обновлений и передает их на обработку.
     */
    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    /**
     * Метод для обработки обновления (нового сообщения или коллбэка).
     *
     * @param update Объект, представляющий собой обновление.
     */
    public void process(Update update) {
        if (update.message() != null && update.message().text().equals("/start")) {
            processStartCommand(update);
        } else if (update.callbackQuery() != null) {
            processCallbackQuery(update);
        } else if (update.message() != null) {
            processInvalidCommand(update);
        } else {
            logger.info("Update is not supported: {}", update);
        }
    }

    /**
     * Метод для обработки команды "/start" и сохранения пользователя в БД
     *
     * @param update Объект, представляющий собой обновление с командой "/start".
     */
    private void processStartCommand(Update update) {
        logger.info("Processing /start command: {}", update.message());
        long chatId = update.message().chat().id();
        String userName = update.message().chat().username();

        User telegramUser = update.message().from();
        // Проверяем, существует ли пользователь в базе данных
        if (appUserDAO.findByTelegramUserId(telegramUser.id()) == null) {
            // Если пользователя не существует, отправляем приветственное сообщение пользователю,
            // создаем временную запись пользователя и сохраняем в базе данных
            sendMessage(messageBuilder.getStartMessage(chatId, userName));
            AppUser tempAppUser = new AppUser();
            tempAppUser.setTelegramUserId(telegramUser.id());
            tempAppUser.setFirstName(telegramUser.firstName());
            tempAppUser.setLastName(telegramUser.lastName());
            tempAppUser.setUserName(telegramUser.username());
            tempAppUser.setActive(true);
            appUserDAO.save(tempAppUser);
        } else {
            // Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора приюта
            sendMessage(new SendMessage(chatId, "Давай продолжим! Выбери, что тебя интересует:").replyMarkup(messageBuilder.getShelterTypeKeyboard()));
        }
    }

    /**
     * Метод для обработки коллбэк-запроса.
     *
     * @param update Объект, представляющий собой обновление с коллбэк-запросом.
     */
    private void processCallbackQuery(Update update) {
        logger.info("Processing callback query: {}", update.callbackQuery());
        long chatId = update.callbackQuery().message().chat().id();
        String userMessageText = update.callbackQuery().data();
        sendMessage(messageBuilder.getReplyMessage(chatId, userMessageText));
    }

    /**
     * Метод для обработки неправильной команды.
     *
     * @param update Объект, представляющий собой обновление с неправильной командой.
     */
    private void processInvalidCommand(Update update) {
        logger.info("Processing invalid command: {}", update.message());
        long chatId = update.message().chat().id();
        sendMessage(new SendMessage(chatId, "Я не понимаю вашу команду. Пожалуйста, выберите приют: ").replyMarkup(messageBuilder.getShelterTypeKeyboard()));
    }

    /**
     * Метод для отправки сообщения через бота Telegram.
     *
     * @param message Сообщение для отправки.
     */
    private void sendMessage(SendMessage message) {
        try {
            telegramBot.execute(message);
        } catch (Exception e) {
            logger.error("Error sending message", e);
        }
    }
}