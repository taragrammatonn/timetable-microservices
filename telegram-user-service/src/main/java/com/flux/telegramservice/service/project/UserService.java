package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;
import static java.util.Objects.isNull;

@Service
public class UserService extends AbstractTelegramService {

    private String message;

    public UserService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    public UserVO addNewUser(Update update) {
        UserVO user = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId());

        if (isNull(user)) {
            user = restTemplateService.saveUser(createNewUserVO(update));
            saveHistory(update, HistoryEvent.NEW_USER);
        }

        return user;
    }

    public String completeUser(UserVO newUser) {
        if (Boolean.FALSE.equals(newUser.getIsDefined())) {
            newUser.setIsDefined(true);
            restTemplateService.saveUser(newUser);
            return String.format("message", newUser.getFName());
        }

        return String.format("message", newUser.getFName());
    }

    private UserVO createNewUserVO(Update update) {
        return new UserVO(
                update.getMessage().getChatId(),
                update.getMessage().getChat().getFirstName(),
                update.getMessage().getChat().getLastName(),
                update.getMessage().getChat().getUserName(),
                null,
                update.getMessage().getFrom().getLanguageCode(),
                true, false, false
        );
    }
}
