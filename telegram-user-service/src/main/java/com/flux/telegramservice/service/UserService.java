package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class UserService {

    private static final String ANSWER = "Привет, %s!\nВведи имя группы, что получить расписание на сегодня!";
    private static final String REPEATING_START_INPUT = "Астановитесь, %s!\nОтделу GroupConsulting нужно название вашей группы! \nВведите название:";

    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    private static final String SAVE_USER = "/addUser";

    @Autowired
    private RestTemplate restTemplate;

    public String completeUser(UserVO newUser) {
        if (Boolean.FALSE.equals(newUser.getIsDefined())) {
            newUser.setIsDefined(true);
            restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, newUser, UserVO.class);
            return String.format(ANSWER, newUser.getFName());
        }
        return REPEATING_START_INPUT;
    }
}
