package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.generator.impl.GenericCallbackQueryCommandGenerator;
import com.flux.telegramservice.util.exception.CannotSaveHistoryException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.flux.telegramservice.util.Links.*;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class BotService extends AbstractTelegramService {

    private final ObjectMapper objectMapper;

    public BotService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    private final Map<String, CommandGenerator> commands = new HashMap<>();

    @SneakyThrows
    public String getLessonsByGroup(Update update, String command, String day) {
        UserVO userVO = isNull(update.getMessage()) ?
                restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                    Long.valueOf(update.getCallbackQuery().getFrom().getId()))
                :
                restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                update.getMessage().getChatId());

        GroupVO groupJson = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, command);

        if (isNull(groupJson))
            return env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".no_group");

        if (!saveHistory(update, HistoryEvent.GROUP))
            throw new CannotSaveHistoryException("Can't save History at entity: " + update.getMessage().getChatId());

        return restTemplateService.getForObject(String.class, GET_LESSONS_WITH_PARAM,
                objectMapper.writeValueAsString(groupJson), objectMapper.writeValueAsString(userVO), day);
    }

    @SneakyThrows
    public SendMessage messageProcessing(Update update) {
        return !isNull(commands.get(update.getMessage().getText())) ?
                commands.get(update.getMessage().getText()).generateCommand(update) :
                generateMessage(update, update.getMessage().getText());
    }

    public SendMessage callBackQueryProcessing(Update update) {
        String command = update.getCallbackQuery().getData();

        GenericCallbackQueryCommandGenerator genericCallbackQueryCommandGenerator =
                new GenericCallbackQueryCommandGenerator(restTemplateService, botService, objectMapper) {
            @Override
            public String getInputCommand() {
                return command;
            }
        };

        return !isNull(genericCallbackQueryCommandGenerator.getCommandsList().get(command)) ?
                genericCallbackQueryCommandGenerator.generateCommand(update) :
                new SendMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()),
                        Objects.requireNonNull(env.getProperty(update.getMessage().getFrom().getLanguageCode()
                                + ".wrong_command")));
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }
}