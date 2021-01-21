package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.UserVO;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final String ANSWER = "Привет, %s!\nВведи имя группы, что получить расписание на сегодня!";

    public String completeUser(UserVO newUserVO) {
        return String.format(ANSWER, newUserVO.getUserNickName());
    }
}
