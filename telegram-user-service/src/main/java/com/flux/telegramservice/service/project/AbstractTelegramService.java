package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

public abstract class AbstractTelegramService {

    @Autowired
    protected RestTemplateService restTemplateService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected BotService botService;

    protected boolean saveHistory(Update update, HistoryEvent event) {
        return restTemplateService.saveHistory(update, event);
    }

    @SneakyThrows
    public String searchCommand(String command, Update update) {
        UserOptionVO userOption = restTemplateService.getUserOptionVO(update.getMessage().getChatId());
        String response = null;

        if (Boolean.TRUE.equals(userOption.getGroupSelected())) {
            response = botService.findGroup(command);

            if (!isNull(response) && !response.equals("null")) {
                restTemplateService.saveUser(new UserVO(update.getMessage().getChatId(), command));
                return botService.findLessonsByGroup(update, command);
            }
        }

        if (userOption.getAudienceSelected()) {

        }

        if (userOption.getTeacherSelected()) {

        }
        return response;
    }
}
