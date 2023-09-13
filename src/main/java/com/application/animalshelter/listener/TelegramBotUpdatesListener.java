package com.application.animalshelter.listener;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

    @Autowired
    private TelegramBot telegramBot;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        updates.forEach(update -> {
            log.info("Processing update: {}", update);
            if(update.message()==null && update.callbackQuery()==null) {
                log.info("message = null");
            }
            if(update.message()!=null){
                switchMessages(update.message());
            }
            if(update.callbackQuery()!=null){
                switchCallback(update.callbackQuery());
            }
    });
    return CONFIRMED_UPDATES_ALL;
}
    private void switchMessages(Message message){
        log.info("switchMessages is processing...");
        User telegramUser = message.from();
        Long chatId = message.chat().id();

        //TODO add MenuButton for start
        // Если пользователь уже обращался к боту ранее, то новое обращение начинается с выбора приюта,
        // о котором пользователь хочет узнать.
        if (message.text().equals("/start")) {
            //кнопка стартового меню
            telegramBot.execute(new SetChatMenuButton().chatId(chatId)
                    .menuButton(new MenuButtonWebApp("webapp", new WebAppInfo("https://core.telegram.org"))));


            String helloText = "Привет, " + telegramUser.firstName() + "!\n" +
                    "Я помогу тебе забрать собаку или кошку домой из приюта Астаны. Для начала выбери животное:";

            //отправляем кнопки с вариантами приютов
            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup(
                    new InlineKeyboardButton("Приют для кошек  " + "\uD83D\uDC08")
                            .callbackData("catShelter"),
                    new InlineKeyboardButton("Приют для собак   " + "\uD83D\uDC15")
                            .callbackData("dogShelter"));

            SendMessage mess = new SendMessage(chatId, helloText);
            mess.replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        }
    }

    private void switchCallback(CallbackQuery callbackQuery){
        if(callbackQuery.data().contains("catShelter")||callbackQuery.data().contains("dogShelter")){
            log.info("callback_data = catShelter");

            InlineKeyboardMarkup inlineKeyboard1 = new InlineKeyboardMarkup();
            inlineKeyboard1.addRow(new InlineKeyboardButton("Информация о приютах")
                    .callbackData("infoAboutShelter"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Как взять животное")
                    .callbackData("howTakeAnimal"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Прислать отчет о питомце")
                    .callbackData("sendReport"));
            inlineKeyboard1.addRow(new InlineKeyboardButton("Позвать волонтера")
                    .callbackData("callVolunteer"));

            SendMessage mess1 = new SendMessage(callbackQuery.message().chat().id(),"Выбор меню")
                    .replyMarkup(inlineKeyboard1);
            telegramBot.execute(mess1);
        }
    }
}
