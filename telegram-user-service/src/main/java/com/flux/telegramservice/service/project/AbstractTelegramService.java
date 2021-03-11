package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;

public abstract class AbstractTelegramService {

    @Autowired
    protected Environment env;

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
        UserVO userVO = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId());
        String response = null;

        try {
            if (Boolean.TRUE.equals(userOption.getGroupSelected())) {
                if (userVO.getUserGroup() == null) {
                    userVO.setUserGroup(command);
                    restTemplateService.saveUser(userVO);
                }
                return botService.getLessonsByGroup(update, command, null);
            }

            if (userOption.getAudienceSelected()) {

            }

            if (userOption.getTeacherSelected()) {

            }
        } catch (NullPointerException e) {
            return env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".choose_option");
        }

        return response;
    }

    @SneakyThrows
    protected SendMessage generateMessage(Update update, String command) {
        return new SendMessage()
                .enableMarkdown(true)
                .setChatId(update.getMessage().getChatId())
                .setText(searchCommand(command, update));
    }
}