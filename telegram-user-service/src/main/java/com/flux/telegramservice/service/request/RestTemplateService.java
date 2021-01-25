package com.flux.telegramservice.service.request;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import com.flux.telegramservice.entity.UserVO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    public void saveErrorMessage(HistoryEvent group) {
    }
}
