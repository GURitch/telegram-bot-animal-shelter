package com.application.animalshelter.listener;


import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

            // Greetings
            if (update.message().text().equals("/start")){
                sendAnswer(update.message().chat().id(),"Welcome to my telegram Bot!");
            } else {
               sendAnswer(update.message().chat().id(), "Error in format input data!");
            }
        });
        return CONFIRMED_UPDATES_ALL;
    }

    private void sendAnswer(Long chatId, String text){
        SendMessage message = new SendMessage(chatId,text);
        telegramBot.execute(message);
    }
}
