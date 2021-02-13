package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.util.exception.CannotSaveHistoryException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.NULL;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class BotService extends AbstractTelegramService {

    UserVO userVO;

    public String searchCommand(Update update, String command, String day) throws CannotSaveHistoryException {

        if (update.getMessage() == null) {
            userVO = restTemplateService.getUserByChatId(Long.valueOf(update.getCallbackQuery().getFrom().getId()));
        } else {
            userVO = restTemplateService.getUserByChatId(update.getMessage().getChatId());
        }

        String groupJson = findGroup(command);

        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        if (userVO.getUserGroup() == null) {
            userVO.setUserGroup(command);
            restTemplateService.saveUser(userVO);
        }

        if (!saveHistory(update, HistoryEvent.GROUP)) {
            throw new CannotSaveHistoryException("Can't save History at entity: " + update.getMessage().getChatId());
        }
        return restTemplateService.getLessons(groupJson, day);
    }

    public String findGroup(String group) {
        return restTemplateService.findGroup(group);
    }
}
