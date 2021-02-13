package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Service
public class UserService extends AbstractTelegramService {

    public static final String FIRST_START_INPUT = "Привет, %s!\nВыбери тип расписания из меню! Можешь не торопится, мы нанели Ждуна обраьатовать запросы";
    public static final String REPEATING_START_INPUT = "Астановитесь, %s!\nСлишко много работы может растроить Ждуна! \nВыбери тип расписания";

    public UserService(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    public UserVO addNewUser(Update update) {
        UserVO user = restTemplateService.getUserByChatId(update.getMessage().getChatId());

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
            return String.format(FIRST_START_INPUT, newUser.getFName());
        }

        return String.format(REPEATING_START_INPUT, newUser.getFName());
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
