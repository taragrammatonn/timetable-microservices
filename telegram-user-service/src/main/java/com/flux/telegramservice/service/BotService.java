package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.HistoryEvent;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.*;
import static java.util.Objects.isNull;

@Service
public class BotService extends AbstractTelegramService {

    public String findLessonsByGroup(Update update) {
        String groupJson = restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, update.getMessage().getText());
        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        saveHistory(update, HistoryEvent.GROUP);
        return restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, groupJson);
    }
}
