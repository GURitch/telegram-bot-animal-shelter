package com.application.animalshelter.listener;

import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import org.springframework.stereotype.Service;

import static com.application.animalshelter.listener.Commands.*;

@Service
public class MessageBuilder {

    public SendMessage getStartMessage(long chatID, String userName) {
        return new SendMessage(chatID, "Добро пожаловать, " + userName + "!" + '\n' + '\n' +
                "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информяцию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке" + '\n' + '\n' +
                "Пожалуйста, выберите приют:").replyMarkup(createTypeOfShelterKeyboard());

    }

    public SendMessage getInfoMessage(long chatID) {
        return new SendMessage(chatID, "Этот телеграмм-бот может ответить на вопросы о том, что нужно знать и уметь, " +
                "чтобы забрать животное из приюта. Дать информяцию о интересующем приюте. " +
                "Так же, сюда можно присылать ежедневный отчет о том, как животное приспосабливается к новой обстановке");
    }

    public SendMessage getKeyboardShelterMessage(long chatID) {
        return new SendMessage(chatID, "Пожалуйста, выберите приют:").replyMarkup(createTypeOfShelterKeyboard());
    }

    public SendMessage getKeyboardCommandsMessage(long chatID, String userMessageText) {
        return new SendMessage(chatID, "Выбран: " + userMessageText + '\n' + '\n' + "Что вы хотите сделать далее:").replyMarkup(createQueryKeyboard());
    }

    private Keyboard createTypeOfShelterKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton(SHELTER_CAT.getCommand())},
                new KeyboardButton[]{new KeyboardButton(SHELTER_DOG.getCommand())}
        );
    }

    private Keyboard createQueryKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton(INFO_SHELTER.getCommand()),},
                new KeyboardButton[]{new KeyboardButton(ADOPT_PET.getCommand()),},
                new KeyboardButton[]{new KeyboardButton(SEND_REPORT.getCommand()),},
                new KeyboardButton[]{new KeyboardButton(BACK_TO_SHELTER_SELECTION.getCommand()),},
                new KeyboardButton[]{new KeyboardButton(BOT_INFORMATION.getCommand()),},
                new KeyboardButton[]{new KeyboardButton(CALL_VOLUNTEER.getCommand())}
        );
    }
}