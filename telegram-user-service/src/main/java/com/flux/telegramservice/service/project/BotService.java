package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.generator.impl.GenericCallbackQueryCommandGenerator;
import com.flux.telegramservice.util.exception.CannotSaveHistoryException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static com.flux.telegramservice.util.Links.*;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class BotService extends AbstractTelegramService {

    @Autowired
    private ObjectMapper objectMapper;

    private final Map<String, CommandGenerator> commands = new HashMap<>();

    @SneakyThrows
    public String getLessonsByGroup(Update update, String command) {
        GroupVO groupJson = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, command);

        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        if (!saveHistory(update, HistoryEvent.GROUP)) {
            throw new CannotSaveHistoryException("Can't save History at entity: " + update.getMessage().getChatId());
        }

        return restTemplateService.getForObject(String.class, GET_LESSONS_WITH_PARAM, objectMapper.writeValueAsString(groupJson), null);
    }

    @SneakyThrows
    public String findGroup(String group) {
        return objectMapper.writeValueAsString(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, group));
    }

    @SneakyThrows
    public SendMessage messageProcessing(Update update) {
        return !isNull(commands.get(update.getMessage().getText())) ?
                commands.get(update.getMessage().getText()).generateCommand(update) :
                generateMessage(update, update.getMessage().getText());
    }

    public SendMessage callBackQueryProcessing(Update update) {
        String command = update.getCallbackQuery().getData();

        GenericCallbackQueryCommandGenerator genericCallbackQueryCommandGenerator = new GenericCallbackQueryCommandGenerator(restTemplateService, botService, objectMapper) {
            @Override
            public String getInputCommand() {
                return command;
            }
        };

        return !isNull(genericCallbackQueryCommandGenerator.getCommandsList().get(command)) ?
                genericCallbackQueryCommandGenerator.generateCommand(update) :
        new SendMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), "Неверная команда.");
    }

    public String getLessonsWithParam(String group, String param) {
        return restTemplateService.getLessonsWithParam(group, param);
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }
}
