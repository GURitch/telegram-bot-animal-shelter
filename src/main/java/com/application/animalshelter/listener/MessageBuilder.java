package com.application.animalshelter.listener;

import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import static com.application.animalshelter.enums.Commands.*;

@Service
public class MessageBuilder {

    public SendMessage getStartMessage(long chatID, String userName) {
        return new SendMessage(chatID, "Добро пожаловать, " + userName + "!" + '\n' + '\n' +
                "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информяцию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке" + '\n' + '\n' +
                "Пожалуйста, выберите приют:").replyMarkup(getShelterTypeKeyboard());

    }

    public SendMessage getInfoMessage(long chatID) {
        return new SendMessage(chatID, "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информяцию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке");
    }

    public SendMessage getKeyboardShelterMessage(long chatID) {
        return new SendMessage(chatID, "Пожалуйста, выберите приют:").replyMarkup(getShelterTypeKeyboard());
    }

    public SendMessage getKeyboardCommandsMessage(long chatID, String userMessageText) {
        return new SendMessage(chatID, "Выбран: " + userMessageText + '\n' + '\n' + "Что вы хотите сделать далее:").replyMarkup(getStartMenuKeyboard());
    }

    public SendMessage getReplyMessage(long chatId, String userName, String userMessageText) {
        return switch (userMessageText) {
            case "/start" -> getStartMessage(chatId, userName);
            case "Информация о боте" -> getInfoMessage(chatId);
            case "Вернуться к выбору приюта" -> getKeyboardShelterMessage(chatId);
            case "Приют для кошек", "Приют для собак" -> getKeyboardCommandsMessage(chatId, userMessageText);
            default -> throw new IllegalStateException("Unexpected value: " + userMessageText);
        };
    }

    private Keyboard getShelterTypeKeyboard() {
        return new InlineKeyboardMarkup(
                new InlineKeyboardButton(SHELTER_CAT.getCommand() + "\uD83D\uDC08")
                        .callbackData(SHELTER_CAT.getCommand()),
                new InlineKeyboardButton(SHELTER_DOG.getCommand() + "\uD83D\uDC15")
                        .callbackData(SHELTER_DOG.getCommand()));
    }

    private Keyboard getStartMenuKeyboard() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(INFO_SHELTER.getCommand())
                .callbackData(INFO_SHELTER.getCommand()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(ADOPT_PET.getCommand())
                .callbackData(ADOPT_PET.getCommand()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(SEND_REPORT.getCommand())
                .callbackData(SEND_REPORT.getCommand()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BACK_TO_SHELTER_SELECTION.getCommand())
                .callbackData(BACK_TO_SHELTER_SELECTION.getCommand()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(BOT_INFORMATION.getCommand())
                .callbackData(BOT_INFORMATION.getCommand()));
        inlineKeyboardMarkup.addRow(new InlineKeyboardButton(CALL_VOLUNTEER.getCommand())
                .callbackData(CALL_VOLUNTEER.getCommand()));
        return inlineKeyboardMarkup;
    }
}
