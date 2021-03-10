package com.flux.telegramservice.service.request;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.flux.telegramservice.util.Links.*;

@Service
@Slf4j
public class RestTemplateService extends RestTemplateHelper {

    public UserVO saveUser(UserVO userVO) {
        return restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, userVO, UserVO.class);
    }

    /**
     *
     * @deprecated Use RestTemplateHelper#getForObject method instead for restTemplate.getForObject() calls
     * See: https://gist.github.com/slmanju/a85a6854cb7d37d069351edae06a4eb7#file-resttemplatehelper-java-L17
     */
    @Deprecated(forRemoval = true, since = "28/02/2021")
    public UserVO getUserByChatId(Long chatId) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_USER_BY_CHAT_ID, UserVO.class, chatId);
    }

    /**
     *
     * @deprecated Use RestTemplateHelper#getForObject method instead for restTemplate.getForObject() calls
     * See: https://gist.github.com/slmanju/a85a6854cb7d37d069351edae06a4eb7#file-resttemplatehelper-java-L17
     */
    @Deprecated(forRemoval = true, since = "28/02/2021")
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

    /**
     *
     * @deprecated Use RestTemplateHelper#getForObject method instead for restTemplate.getForObject() calls
     * TODO: using RestTemplateService#getForObject instead
     */
    @Deprecated(forRemoval = true, since = "28/02/2021")
    public UserOptionVO getUserOptionVO(Long chatId) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_USER_OPTION_BY_CHAT_ID, UserOptionVO.class, chatId);
    }

    /**
     *
     * @deprecated Use RestTemplateHelper#getForObject method instead for restTemplate.getForObject() calls
     * TODO: using RestTemplateService#getForObject instead
     */
    @Deprecated(forRemoval = true, since = "28/02/2021")
    public String getLessonsWithParam(String group, String param) {
        return restTemplate.getForObject(LOGISTIC_SERVICE + GET_LESSONS_WITH_PARAM, String.class, group, param);
    }
}
