package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.*;
import static java.util.Objects.isNull;

@Service
public class UserService extends AbstractTelegramService {

    private static final String ANSWER = "Привет, %s!\nВведи имя группы, что получить расписание на сегодня!";
    private static final String REPEATING_START_INPUT = "Астановитесь, %s!\nОтделу GroupConsulting нужно название вашей группы! \nВведите название:";

    public UserVO addNewUser(Update update) {
        UserVO user = restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, completeUser(update), UserVO.class);
        saveHistory(update, HistoryEvent.NEW_USER);

        return user;
    }
    private UserVO completeUser(Update update) {
        UserVO user = restTemplate.getForObject(
                LOGISTIC_SERVICE + GET_USER_BY_CHAT_ID,
                UserVO.class,
                update.getMessage().getChatId()
        );

        if (isNull(user)) {
            return new UserVO(
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

    public String completeUser(UserVO newUser) {
        if (Boolean.FALSE.equals(newUser.getIsDefined())) {
            newUser.setIsDefined(true);
            restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, newUser, UserVO.class);
            return String.format(ANSWER, newUser.getFName());
        }

        return String.format(REPEATING_START_INPUT, newUser.getFName());
    }
}
