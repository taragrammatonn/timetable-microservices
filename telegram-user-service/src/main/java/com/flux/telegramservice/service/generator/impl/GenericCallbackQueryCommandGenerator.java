package com.flux.telegramservice.service.generator.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Map;

import static com.flux.telegramservice.util.Links.GET_STUDY_PLAN;
import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;


@Getter
@Component
public class GenericCallbackQueryCommandGenerator implements CommandGenerator {

    private final RestTemplateService restTemplateService;
    private final BotService botService;
    private final ObjectMapper objectMapper;

    public GenericCallbackQueryCommandGenerator(RestTemplateService restTemplateService, BotService botService, ObjectMapper objectMapper) {
        this.restTemplateService = restTemplateService;
        this.botService = botService;
        this.objectMapper = objectMapper;
    }

    private final Map<String, String> commandsList = Map.ofEntries(
            Map.entry("+1d", "1"),
            Map.entry("+2d", "2"),
            Map.entry("+1w", "nextWeek"),
            Map.entry("tbSemI", "Semestru I"),
            Map.entry("tbSemII", "Semestru II")
    );

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        UserVO userVO = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()));
        String command = update.getCallbackQuery().getData();

        String response = command.equals("tbSemI") || command.equals("tbSemII") ?
                restTemplateService.getForObject(String.class, GET_STUDY_PLAN, command, objectMapper.writeValueAsString(userVO))
                : botService.getLessonsByGroup(update, userVO.getUserGroup(), commandsList.get(command));

        return new SendMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), response);
    }

    @Override
    public String getInputCommand() {
        return "callBackGenerator";
    }
}
