package com.application.animalshelter.enums;


public enum Commands {
    SHELTER_DOG("Приют для собак"),
    SHELTER_CAT("Приют для кошек"),
    INFO_SHELTER("Узнать информацию о приюте"),
    ADOPT_PET("Как взять животное из приюта"),
    SEND_REPORT("Прислать отчет о питомце"),
    CALL_VOLUNTEER("Позвать волонтера"),
    BACK_TO_SHELTER_SELECTION("Вернуться к выбору приюта"),
    BOT_INFORMATION("Информация о боте"),
    ADDRESS_AND_WORKING_HOURS("Адреса приютов и время работы"),
    CAR_PASS("Оформление пропуска на машину"),
    RULES_AND_INTERACTION("Правила нахождения внутри"),
    CONTACT_DETAILS("Записать контактные данные"),
    BACK_TO_SHELTER_MENU("Вернуться в меню приюта");

    Commands(String command) {
        this.command = command;
    }
    private final String command;

    public String getText() {
        return command;
    }
}
