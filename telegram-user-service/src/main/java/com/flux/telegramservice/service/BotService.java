package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Service
public class BotService {

    // LOGISTIC-SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    private static final String SAVE_USER = "/addUser";
    private static final String GET_USER_BY_CHAT_ID = "/getUser?chatId={chatId}";
    public static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    public static final String LESSON_BY_GROUP = "/lessonByGroup?groupJson={groupJson}";

    // Util
    public static final String NULL = "null";

    private final RestTemplate restTemplate;

    public BotService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public User addNewUser(Update update) {
        return restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, completeUser(update), User.class);
    }

    public String findLessonsByGroup(String groupName) {
        String groupJson = restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, groupName);
        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        return restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, groupJson);
    }

    private User completeUser(Update update) {
        User user = restTemplate.getForObject(
                LOGISTIC_SERVICE + GET_USER_BY_CHAT_ID,
                User.class,
                update.getMessage().getChatId()
        );

        if (isNull(user)) {
            return new User(
                    update.getMessage().getChatId(),
                    update.getMessage().getChat().getFirstName(),
                    update.getMessage().getChat().getLastName(),
                    update.getMessage().getChat().getUserName(),
                    null,
                    update.getMessage().getFrom().getLanguageCode(),
                    true, false, false
            );
        } else {
            user.setIsDefined(true);
            return user;
        }
    }

}
