package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.User;
import com.google.gson.Gson;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Service
public class BotService {

    // LOGISTIC-SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    private static final String SAVE_USER = "/addUser";
    public static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    public static final String LESSON_BY_GROUP = "/lessonByGroup?groupJson={groupJson}";

    // Util
    public static final String NULL = "null";

    private final RestTemplate restTemplate;

    private final Gson gson;

    private final String a = "";

    public BotService(RestTemplate restTemplate, Gson gson) {
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public User addNewUser(Update update) {
        return restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, completeUser(update), User.class);
    }

    public String findLessonsByGroup(String groupName) {
        String groupJson = restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, groupName);
        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        return restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, groupJson);
    }

    private String completeUser(Update update) {
        return gson.toJson(
                new User(
                        update.getMessage().getChatId(),
                        update.getMessage().getChat().getFirstName(),
                        update.getMessage().getChat().getLastName(),
                        update.getMessage().getChat().getUserName(),
                        null,
                        update.getMessage().getFrom().getLanguageCode(),
                        true, false
                )
        );
    }
}
