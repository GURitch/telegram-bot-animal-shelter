package com.application.animalshelter.listener;


import com.application.animalshelter.dao.AnimalShelterDAO;
import com.application.animalshelter.dao.AppUserDAO;
import com.application.animalshelter.dao.CurrChoiceDAO;
import com.application.animalshelter.entıty.AnimalShelter;
import com.application.animalshelter.entıty.AppUser;
import com.application.animalshelter.entıty.CurrChoice;
import com.application.animalshelter.enums.City;
import com.application.animalshelter.service.AnimalShelterService;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.*;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.BaseRequest;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SetChatMenuButton;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * main class that processing the user's commands from telegram_bot
 */

@Service
public class TelegramBotUpdatesListener implements UpdatesListener {
    private final Logger log = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);
    private final AppUserDAO appUserDAO;
    private final AnimalShelterDAO animalShelterDAO;
    private final CurrChoiceDAO currChoiceDAO;
    private final AnimalShelterService animalShelterService;

    @Autowired
    private TelegramBot telegramBot;

    public TelegramBotUpdatesListener(AppUserDAO appUserDAO, AnimalShelterDAO animalShelterDAO, CurrChoiceDAO currChoiceDAO, AnimalShelterService animalShelterService) {
        this.appUserDAO = appUserDAO;
        this.animalShelterDAO = animalShelterDAO;
        this.currChoiceDAO = currChoiceDAO;
        this.animalShelterService = animalShelterService;
    }

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

    /**
     * Method that contains logic of processing messages <br>
     * based on: {@link TelegramBot} <br>
     * using {@code InlineKeyboardMarkup}
     * @see TelegramBot
     */
    private void switchMessages(Message message){
        log.info("switchMessages is processing...");
        User telegramUser = message.from();
        Long chatId = message.chat().id();

        if (message.text().equals("/start")) {
            if(appUserDAO.findByTelegramUserId(telegramUser.id())==null) {
                //кнопка стартового меню
                telegramBot.execute(new SetChatMenuButton().chatId(chatId)
//                    .menuButton(new MenuButtonWebApp("Menu", new WebAppInfo("https://core.telegram.org"))));
                        .menuButton(new MenuButton("/hello")));

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

                AppUser tempAppUser = new AppUser();
                tempAppUser.setTelegramUserId(telegramUser.id());
                tempAppUser.setFirstName(telegramUser.firstName());
                tempAppUser.setLastName(telegramUser.lastName());
                tempAppUser.setUserName(telegramUser.username());
                tempAppUser.setActive(true);

                AppUser addedAppUser = appUserDAO.save(tempAppUser);
            } else{
                InlineKeyboardMarkup inlineKeyboard1 = new InlineKeyboardMarkup();
                inlineKeyboard1.addRow(new InlineKeyboardButton("Информация о приютах")
                        .callbackData("infoAboutShelter"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Как взять животное")
                        .callbackData("howTakeAnimal"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Прислать отчет о питомце")
                        .callbackData("sendReport"));
                inlineKeyboard1.addRow(new InlineKeyboardButton("Позвать волонтера")
                        .callbackData("callVolunteer"));

                SendMessage mess1 = new SendMessage(chatId,"Давай продолжим! Выбери, что тебя интересует:")
                        .replyMarkup(inlineKeyboard1);
                telegramBot.execute(mess1);
            }
        }
    }

    private void switchCallback(CallbackQuery callbackQuery){
        if(callbackQuery.data().contains("catShelter")||callbackQuery.data().contains("dogShelter")){
            log.info("callback_data = catShelter");

            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
            inlineKeyboard.addRow(new InlineKeyboardButton("Информация о приютах")
                    .callbackData("infoAboutShelter"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Как взять животное")
                    .callbackData("howTakeAnimal"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Прислать отчет о питомце")
                    .callbackData("sendReport"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Позвать волонтера")
                    .callbackData("callVolunteer"));

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбор меню")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains("infoAboutShelter")){
            log.info("callback_data = infoAboutShelter");

            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
            inlineKeyboard.addRow(new InlineKeyboardButton("Адреса приютов и время работы")
                    .callbackData("sheltersAddresses"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Оформление пропуска на машину")
                    .callbackData("passCar"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Правила нахождения внутри и общения с животным")
                    .callbackData("shelterRules"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Записать контактные данные для связи")
                    .callbackData("contactInformation"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Позвать волонтера")
                    .callbackData("callVolunteer"));

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Я помогу тебе " +
                    "узнать информацию о приюте! Выбери, что тебя интересует: ")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains("sheltersAddresses")){
            log.info("callback_data = sheltersAddresses");

            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
            inlineKeyboard.addRow(new InlineKeyboardButton("Алматы")
                    .callbackData(City.ALMATY.toString()));
            inlineKeyboard.addRow(new InlineKeyboardButton("Астана")
                    .callbackData(City.ASTANA.toString()));
            inlineKeyboard.addRow(new InlineKeyboardButton("Аркалык")
                    .callbackData(City.ARKALYK.toString()));

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбери город:")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains(City.ALMATY.toString())){
            log.info("callback_data = City");
            InlineKeyboardMarkup inlineKeyboard = chooseAnimalShelters(City.ALMATY);

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбери приют:")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains(City.ASTANA.toString())){
            log.info("callback_data = City");
            InlineKeyboardMarkup inlineKeyboard = chooseAnimalShelters(City.ASTANA);

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбери приют:")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains(City.ARKALYK.toString())){
            log.info("callback_data = City");
            InlineKeyboardMarkup inlineKeyboard = chooseAnimalShelters(City.ARKALYK);

            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбери приют:")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        } else if(callbackQuery.data().contains(animalShelterDAO.findByName(callbackQuery.data()).getName())){
            log.info("callback_data = " + callbackQuery.data());

            CurrChoice tempCurrChoice = new CurrChoice();
            tempCurrChoice.setCurrentDateTime(LocalDateTime.now());
            tempCurrChoice.setAppUser(appUserDAO.findByTelegramUserId(callbackQuery.from().id()));
            tempCurrChoice.setAnimalShelter(animalShelterDAO.findByName(callbackQuery.data()));

            CurrChoice savedCurrChoice = currChoiceDAO.save(tempCurrChoice);



            InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
            inlineKeyboard.addRow(new InlineKeyboardButton("Расписание работы приюта")
                    .callbackData("WorkHours"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Адрес")
                    .callbackData("Address"));
            inlineKeyboard.addRow(new InlineKeyboardButton("Схема проезда")
                    .callbackData("LocationMap"));
            SendMessage mess = new SendMessage(callbackQuery.message().chat().id(),"Выбери приют:")
                    .replyMarkup(inlineKeyboard);
            telegramBot.execute(mess);
        }
    }

    private InlineKeyboardMarkup chooseAnimalShelters(City city){
        InlineKeyboardMarkup inlineKeyboard = new InlineKeyboardMarkup();
        List<AnimalShelter> animalShelterList = animalShelterService.getAnimalSheltersByCity(city);
        for (int i=0; i<animalShelterList.size();i++){
            inlineKeyboard.addRow(new InlineKeyboardButton(animalShelterList.get(i).getName())
                    .callbackData(animalShelterList.get(i).getName()));
        }
        return  inlineKeyboard;
    }

}
