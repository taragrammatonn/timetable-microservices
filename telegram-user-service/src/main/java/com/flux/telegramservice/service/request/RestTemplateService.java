package com.flux.telegramservice.service.request;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import com.flux.telegramservice.entity.UserOptionVO;
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

    public boolean saveHistory(Update update, HistoryEvent event) {
        if (update.getMessage() == null) {
            return restTemplate.postForObject(
                    LOGISTIC_SERVICE + SAVE_HISTORY,
                    new HistoryVO(
                            event,
                            update.getCallbackQuery().getData(),
                            new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                            Long.valueOf(update.getCallbackQuery().getFrom().getId())),
                    HistoryVO.class
            ) != null;
        } else return restTemplate.postForObject(
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

    public String getLessonsWithParam(String group, String param) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_LESSONS_WITH_PARAM, String.class, group, param);
    }
}
