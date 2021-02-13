package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    @Autowired
    private ObjectMapper objectMapper;

    protected boolean saveHistory(Update update, HistoryEvent event) {
        return restTemplateService.saveHistory(update, event);
    }

    @SneakyThrows
    public String searchCommand(String command, Update update) {
        UserOptionVO userOption = restTemplateService.getUserOptionVO(update.getMessage().getChatId());
        UserVO userVO = restTemplateService.getUserByChatId(update.getMessage().getChatId());
        String response = null;

        if (Boolean.TRUE.equals(userOption.getGroupSelected())) {
            if (userVO.getUserGroup() == null) {
                userVO.setUserGroup(command);
                restTemplateService.saveUser(userVO);
            }
            response = botService.findGroup(command);

            if (!isNull(response) && !response.equals("null")) {
                return botService.searchCommand(update, command, "1");
            }
        }

        if (userOption.getAudienceSelected()) {

        }

        if (userOption.getTeacherSelected()) {

        }
        return response;
    }
}
