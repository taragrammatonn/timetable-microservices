package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String ANSWER = "Привет, %s!\nВведи имя группы, что получить расписание на сегодня!";

    public String completeUser(User newUser) {
        return String.format(ANSWER, newUser.getUserNickName());
    }
}
