package com.flux.telegramservice.service.generator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static com.flux.telegramservice.util.Links.FIND_GROUP;
import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;


@Getter
@Setter
@Component
public abstract class AddDaysCommandGenerator implements CommandGenerator {

    private final RestTemplateService restTemplateService;
    private final BotService botService;
    private final ObjectMapper objectMapper;

    @Autowired
    protected AddDaysCommandGenerator(RestTemplateService restTemplateService, BotService botService, ObjectMapper objectMapper) {
        this.restTemplateService = restTemplateService;
        this.botService = botService;
        this.objectMapper = objectMapper;
    }

    private Map<String, String> commandsList = Map.ofEntries(
            Map.entry("+1d", "1"),
            Map.entry("+2d", "2"),
            Map.entry("+1w", "nextWeek")
    );

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        UserVO userVO = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()));

        String response = botService.getLessonsWithParam(
                objectMapper.writeValueAsString(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, userVO.getUserGroup())),
                commandsList.get(update.getCallbackQuery().getData())
        );

        return new SendMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), response);
    }
}
