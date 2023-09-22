package com.application.animalshelter.listener;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import static com.application.animalshelter.enums.Commands.*;

/**
 * Класс MessageBuilder предоставляет методы для создания сообщений (SendMessage) в ответ на команды пользователя в телеграм-боте.
 * Он используется для формирования текстовых сообщений с различными вариантами ответов и клавиатурами.
 */
@Service
public class MessageBuilder {

    /**
     * Создает приветственное сообщение для пользователя с указанным идентификатором чата и именем пользователя.
     *
     * @param chatID   Идентификатор чата с пользователем.
     * @param userName Имя пользователя.
     * @return SendMessage с приветственным сообщением и клавиатурой выбора приюта.
     */
    public SendMessage getStartMessage(long chatID, String userName) {
        return new SendMessage(chatID, "Добро пожаловать, " + userName + "!" + '\n' + '\n' +
                "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информацию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке" + '\n' + '\n' +
                "Пожалуйста, выбери приют:").replyMarkup(getShelterTypeKeyboard());
    }

    /**
     * Создает сообщение с информацией о боте для пользователя с указанным идентификатором чата.
     *
     * @param chatID Идентификатор чата с пользователем.
     * @return SendMessage с информацией о боте.
     */
    public SendMessage getInfoMessage(long chatID) {
        return new SendMessage(chatID, "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информацию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке");
    }

    /**
     * Создает сообщение в зависимости от текста, полученного от пользователя.
     *
     * @param chatId          Идентификатор чата с пользователем.
     * @param userMessageText Текст сообщения, полученный от пользователя.
     * @return SendMessage с соответствующим ответом на текст пользователя и клавиатурой.
     */
    public SendMessage getReplyMessage(long chatId, String userMessageText) {
        return switch (userMessageText) {
            case "Информация о боте" -> getInfoMessage(chatId);
            case "Вернуться к выбору приюта" -> new SendMessage(chatId, "Пожалуйста, выберите приют:").replyMarkup(getShelterTypeKeyboard());
            case "Приют для кошек", "Приют для собак" -> new SendMessage(chatId, "Выбери, что тебя интересует:").replyMarkup(getStartMenuKeyboard());
            case "Узнать информацию о приюте" -> new SendMessage(chatId, "Выбери, что тебя интересует:").replyMarkup(getShelterMenuKeyboard());
            default -> new SendMessage(chatId, "Я не понимаю вашу команду. Выберите команду из списка:").replyMarkup(getStartMenuKeyboard());
        };
    }

    /**
     * Создает клавиатуру для выбора типа приюта (для кошек или собак).
     *
     * @return InlineKeyboardMarkup с кнопками выбора типа приюта.
     */
    public Keyboard getShelterTypeKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(SHELTER_CAT.getText() + "\uD83D\uDC08")
                        .callbackData(SHELTER_CAT.getText()),
                new InlineKeyboardButton(SHELTER_DOG.getText() + "\uD83D\uDC15")
                        .callbackData(SHELTER_DOG.getText()));
    }

    /**
     * Создает клавиатуру для главного меню с командами.
     *
     * @return InlineKeyboardMarkup с кнопками команд главного меню.
     */
    private Keyboard getStartMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(INFO_SHELTER.getText())
                .callbackData(INFO_SHELTER.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ADOPT_PET.getText())
                .callbackData(ADOPT_PET.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(SEND_REPORT.getText())
                .callbackData(SEND_REPORT.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getText())
                .callbackData(BACK_TO_SHELTER_SELECTION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BOT_INFORMATION.getText())
                .callbackData(BOT_INFORMATION.getText()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getText())
                .callbackData(CALL_VOLUNTEER.getText()));
        return inlineKeyboardMarkup;
    }

    /**
     * Создает клавиатуру для меню приюта с командами.
     *
     * @return InlineKeyboardMarkup с кнопками команд.
     */
    private Keyboard getShelterMenuKeyboard() {
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.addRow(new InlineKeyboardButton(ADDRESS_AND_WORKING_HOURS.getText())
                .callbackData(ADDRESS_AND_WORKING_HOURS.getText()));
        markup.addRow(new InlineKeyboardButton(CAR_PASS.getText())
                .callbackData(CAR_PASS.getText()));
        markup.addRow(new InlineKeyboardButton(RULES_AND_INTERACTION.getText())
                .callbackData(RULES_AND_INTERACTION.getText()));
        markup.addRow(new InlineKeyboardButton(CONTACT_DETAILS.getText())
                .callbackData(CONTACT_DETAILS.getText()));
        markup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_MENU.getText())
                .callbackData(BACK_TO_SHELTER_MENU.getText()));
        markup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getText())
                .callbackData(BACK_TO_SHELTER_SELECTION.getText()));
        markup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getText())
                .callbackData(CALL_VOLUNTEER.getText()));
        return markup;
    }
}
