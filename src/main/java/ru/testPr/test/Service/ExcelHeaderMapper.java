package ru.testPr.test.Service;

import java.util.HashMap;
import java.util.Map;

public class ExcelHeaderMapper {

    public static Map<String, String> getColumnTranslations() {
        Map<String, String> columnTranslations = new HashMap<>();
        columnTranslations.put("audience_number", "Номер аудитории");
        columnTranslations.put("occupied_computers", "Кол-во занятых компьютеров");
        columnTranslations.put("total_people", "Кол-во участников");
        columnTranslations.put("total_curators", "Кол-во кураторов");
        columnTranslations.put("fio_curators", "ФИО кураторов");
        
        columnTranslations.put("name_olympiad", "Название олимпиады");
        columnTranslations.put("start_date_olymp", "Дата начала");
        columnTranslations.put("end_date_olymp", "Дата окончания");
        columnTranslations.put("offline_participants", "Кол-во очных участников");
        columnTranslations.put("offline_participants_attended", "Кол-во явившихя очных участников");
        columnTranslations.put("offline_curators", "Кол-во кураторов для очников");
        columnTranslations.put("online_participants", "Кол-во заочных участников");
        columnTranslations.put("online_participants_attended", "Кол-во явившихя заочных участников");
        columnTranslations.put("online_curators", "Кол-во кураторов для заочников");
        columnTranslations.put("occupied_audience", "Кол-во занятых аудиторий");
        
        columnTranslations.put("last_name", "Фамилия");
        columnTranslations.put("name", "Имя");
        columnTranslations.put("middle_name", "Отчество");
        columnTranslations.put("team_name", "Команда");
        columnTranslations.put("school_name", "Школа");
        columnTranslations.put("school_city", "Город");
        columnTranslations.put("phone_number", "Телефон");
        columnTranslations.put("email", "Почта");
        columnTranslations.put("school_class", "Класс");
        columnTranslations.put("computer_number", "Номер компьютера");
        columnTranslations.put("attended", "Присутствие");
        
        columnTranslations.put("points", "Кол-во баллов");
        columnTranslations.put("place", "Место");

        return columnTranslations;
    }
}
