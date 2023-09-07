package com.application.animalshelter.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TelegramBotUpdatesListener {

    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    private final TelegramBot telegramBot;


    public TelegramBotUpdatesListener(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(updates -> {
            updates.forEach(this::process);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }
//    @PostConstruct
//    public void init() {
//        telegramBot.setUpdatesListener(this);
//    }


    public void process(Update update) {

            String updateMessageText = update.message().text();

            if (update.message() == null) {
                return;
            }

            long chatId = update.message().chat().id();
            try {
                if (update.message() != null && "/start".equals(updateMessageText)) {
                    SendMessage message = new SendMessage(chatId, "Добро пожаловать, " + update.message().chat().firstName() + "!" + '\n' + """
                                                        
                            Я помогу вам забрать животное из приюта.
                                                        
                            Пожалуйста, выберите приют:""").replyMarkup(createTypeOfShelterKeyboard());
                    telegramBot.execute(message);
                }
                else if (update.message() != null && ("Приют для кошек".equals(updateMessageText) || "Приют для собоак".equals(updateMessageText))) {
                    SendMessage message = new SendMessage(chatId, "Выбран " + updateMessageText + '\n' + '\n' + "Что вы хотите сделать далее:").replyMarkup(createQueryKeyboard());
                    telegramBot.execute(message);
                } else {
                    sendMessage(chatId, "Извините, контент в разрабтке" );
                }
            } catch (Exception e) {
                logger.error("Error sending message", e);
            }

    }

    private void sendMessage(long chatId, String messageText) {
        SendMessage message = new SendMessage(chatId, messageText);
        try {
            telegramBot.execute(message);
        } catch (Exception e) {
            logger.error("Error sending message", e);
        }
    }

    private Keyboard createTypeOfShelterKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton("Приют для кошек")},
                new KeyboardButton[]{new KeyboardButton("Приют для собоак")}
        );
    }

    public static Keyboard createQueryKeyboard() {
        return new ReplyKeyboardMarkup(
                new KeyboardButton[]{new KeyboardButton("Узнать информацию о приюте"),},
                new KeyboardButton[]{new KeyboardButton("Как взять животное из приюта"),},
                new KeyboardButton[]{new KeyboardButton("Прислать отчет о питомце"),},
                new KeyboardButton[]{new KeyboardButton("Позвать волонтера")}
        );
    }


}
