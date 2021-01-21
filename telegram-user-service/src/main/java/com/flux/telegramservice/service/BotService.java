package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import com.flux.telegramservice.entity.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Date;

import static java.util.Objects.isNull;

@Service
public class BotService {

    // LOGISTIC-SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    private static final String SAVE_USER = "/addUser";
    public static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    public static final String LESSON_BY_GROUP = "/lessonByGroup?groupJson={groupJson}";
    public static final String SAVE_HISTORY = "/saveHistory";

    // Util
    public static final String NULL = "null";

    private final RestTemplate restTemplate;

    public BotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserVO addNewUser(Update update) {
        UserVO user = restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, completeUser(update), UserVO.class);
        saveHistory(update, HistoryEvent.NEW_USER);

        return user;
    }

    public String findLessonsByGroup(Update update) {
        String groupJson = restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, update.getMessage().getText());
        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        saveHistory(update, HistoryEvent.GROUP);
        return restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, groupJson);
    }

    private UserVO completeUser(Update update) {
        return new UserVO(
                        update.getMessage().getChatId(),
                        update.getMessage().getChat().getFirstName(),
                        update.getMessage().getChat().getLastName(),
                        update.getMessage().getChat().getUserName(),
                        null,
                        update.getMessage().getFrom().getLanguageCode(),
                        true, false
        );
    }

    private void saveHistory(Update update, HistoryEvent event) {
        restTemplate.postForObject(
                LOGISTIC_SERVICE + SAVE_HISTORY,
                new HistoryVO(
                        event,
                        update.getMessage().getText(),
                        new Date(),
                        update.getMessage().getChatId()),
                HistoryVO.class
        );
    }
}
