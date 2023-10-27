package com.application.animalshelter.listener;

import com.application.animalshelter.dao.UserDAO;
import com.application.animalshelter.dao.VolunteerDAO;
import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.entıty.Volunteer;
import com.application.animalshelter.enums.Commands;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;

import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Класс, отвечающий за обработку обновлений бота Telegram.
 * Слушает обновления и передает их на обработку соответствующим методам.
 */
@Service
public class TelegramBotUpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;
    private final MessageBuilder messageBuilder;
    private final UserDAO userDAO;
    private final VolunteerDAO volunteerDAO;


    public TelegramBotUpdatesListener(TelegramBot telegramBot, MessageBuilder messageBuilder, UserDAO userDAO, VolunteerDAO volunteerDAO) {
        this.telegramBot = telegramBot;
        this.messageBuilder = messageBuilder;
        this.userDAO = userDAO;
        this.volunteerDAO = volunteerDAO;
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
     * Метод для обработки обновления ("/start", коллбэка из кнопки или текста пользователя).
     *
     * @param update Объект, представляющий собой обновление.
     */
    public void process(Update update) {
        if (update.message() != null && update.message().text().equals("/start")) {
            processStartCommand(update);
        } else if (update.callbackQuery() != null) {
            processCallbackQuery(update);
        } else if (update.message() != null) {
            processUserResponse(update);
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

        com.pengrad.telegrambot.model.User telegramUser = update.message().from();
        // Проверяем, существует ли пользователь в базе данных
        if (userDAO.findByTelegramUserId(telegramUser.id()) == null) {
            // Если пользователя не существует, отправляем приветственное сообщение,
            // и сохраняем в базе данных
            sendMessage(messageBuilder.getStartMessage(chatId, userName));
            addUserInDatabase(update);
        } else {
            // Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора приюта
            sendMessage(new SendMessage(chatId, "Давай продолжим! Выбери, что тебя интересует:").replyMarkup(messageBuilder.getShelterTypeKeyboard()));
        }
    }

    private void addUserInDatabase(Update update){
        com.pengrad.telegrambot.model.User telegramUser = update.message().from();
        AppUser tempUser = new AppUser();
        tempUser.setTelegramUserId(telegramUser.id());
        tempUser.setFirstName(telegramUser.firstName());
        tempUser.setLastName(telegramUser.lastName());
        tempUser.setUserName(telegramUser.username());
        tempUser.setAdoptedAnimal(false);
        userDAO.save(tempUser);
    }

    /**
     * Метод для обработки коллбэк-запроса.
     *
     * @param update Объект, представляющий собой обновление с коллбэк-запросом.
     */
    private void processCallbackQuery(Update update) {
        logger.info("Processing callback query: {}", update.callbackQuery());
        long chatId = update.callbackQuery().message().chat().id();
        String callbackDataText = update.callbackQuery().data();
        long userId = update.callbackQuery().from().id();
//       Сохраняем пользователя в базе если по какой-то он еще не сохранен
        if (userDAO.findByTelegramUserId(userId)==null){
            addUserInDatabase(update);
        }
        //При нажатии на кнопку выбора приюта, сохраняем эту информацию в пользователе
        if (callbackDataText.equals(Commands.SHELTER_CAT.getText()) || callbackDataText.equals(Commands.SHELTER_DOG.getText())) {
            AppUser appUser = userDAO.findByTelegramUserId(userId);
            appUser.setShelterType(callbackDataText);
            userDAO.save(appUser);
        }
        if (callbackDataText.equals(Commands.CALL_VOLUNTEER.getText())){
            sendMessage(new SendMessage(chatId, "Волонтер скоро свяжется с вами в телеграмм"));
            Volunteer volunteer = volunteerDAO.findAll().get(0);
            sendMessage(new SendMessage(volunteer.getTelegramId(),"Запрос связи с пользователем, id: " + update.callbackQuery().from().id()));
        }
        sendMessage(messageBuilder.getReplyMessage(chatId, callbackDataText, userId));
    }

    /**
     * Метод для обработки ответа пользователя, содержащего номер телефона и почту.
     *
     */
    private void processUserResponse(Update update) {

        long userId = update.message().from().id();

//       Сохраняем пользователя в базе если по какой-то он еще не сохранен
        if (userDAO.findByTelegramUserId(userId)==null){
            addUserInDatabase(update);
        }

        long chatId = update.message().chat().id();
        String text = update.message().text();

        String phoneNumber = extractPhoneNumber(text);
        String email = extractEmail(text);

        AppUser appUser = userDAO.findByTelegramUserId(chatId);
        if (phoneNumber == null || email == null) {
            sendMessage(new SendMessage(chatId, "Телефон или почта введены не корректно, попробуйте еще"));
            sendMessage(messageBuilder.getReplyMessage(chatId,Commands.CONTACT_DETAILS.getText(),chatId));
            return;
        }
        if (appUser != null) {
            appUser.setPhoneNumber(phoneNumber);
            appUser.setEmail(email);
            userDAO.save(appUser);
            sendMessage(new SendMessage(chatId, "Спасибо, Ваши контактные данные сохранены! В скором времени с вами свяжется наш сотрудник").replyMarkup(messageBuilder.getStartMenuKeyboard()));
            Volunteer volunteer = volunteerDAO.findAll().get(0);
            sendMessage(new SendMessage(volunteer.getTelegramId(),"Запрос связи с пользователем, данные для связи: " + phoneNumber+", "+email));
        }
    }

    /**
     * Метод для извлечения номера телефона из текста.
     *
     * @param text Текст, из которого нужно извлечь номер телефона.
     * @return Строка с номером телефона или null, если номер не найден или некорректен.
     */
    private String extractPhoneNumber(String text) {
        String pattern = "\\+?\\d+"; // Любое количество цифр, возможно с "+" в начале

        Pattern phonePattern = Pattern.compile(pattern);
        Matcher matcher = phonePattern.matcher(text);

        StringBuilder phoneNumber = new StringBuilder();

        int digitCount = 0;
        while (matcher.find()) {
            String match = matcher.group();
            if (digitCount + match.length() <= 11) {
                phoneNumber.append(match);
                digitCount += match.length();
            } else {
                break;
            }
        }
        if(phoneNumber.length()<11){
            return null;
        }
        return phoneNumber.toString();
    }

    /**
     * Метод для извлечения адреса электронной почты из текста.
     *
     * @param text Текст, из которого нужно извлечь адрес электронной почты.
     * @return Строка с адресом электронной почты или null, если адрес не найден или некорректен.
     */
    private String extractEmail(String text) {
        String pattern = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,7}\\b";

        Pattern emailPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        Matcher matcher = emailPattern.matcher(text);

        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
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