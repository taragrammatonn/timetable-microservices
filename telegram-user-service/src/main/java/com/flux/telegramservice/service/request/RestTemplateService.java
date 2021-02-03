package com.flux.telegramservice.service.request;

import com.flux.telegramservice.entity.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.flux.telegramservice.util.Links.*;

@Service
public class RestTemplateService {

    private final RestTemplate restTemplate;

    public RestTemplateService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserVO saveUser(UserVO userVO) {
        return restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, userVO, UserVO.class);
    }

    public UserVO getUserByChatId(Long chatId) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_USER_BY_CHAT_ID, UserVO.class, chatId);
    }

    public String findGroup(String group) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, group);
    }

    public String getLessonsByGroup(String group) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, group);
    }

    public List<GroupVO> getAllGroups() {
        ResponseEntity<GroupVO[]> response = restTemplate.getForEntity(LOGISTIC_SERVICE + GET_ALL_GROUPS, GroupVO[].class);

        GroupVO[] groupArray = response.getBody();

        if (groupArray == null) {
            throw new NullPointerException("No data in GroupVOList response!");
        }

        return Arrays.stream(groupArray).collect(Collectors.toList());
    }

    public boolean saveHistory(Update update, HistoryEvent event) {
        return restTemplate.postForObject(
                LOGISTIC_SERVICE + SAVE_HISTORY,
                new HistoryVO(
                        event,
                        update.getMessage().getText(),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                        update.getMessage().getChatId()),
                HistoryVO.class
        ) != null;
    }

    public void saveUserOption(UserOptionVO userOptionVO) {
        restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER_OPTION, userOptionVO, UserOptionVO.class);
    }

    public UserOptionVO getUserOptionVO(Long chatId) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_USER_OPTION_BY_CHAT_ID, UserOptionVO.class, chatId);
    }
}
