package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
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

    private final ObjectMapper objectMapper;

    public BotService(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    protected final Map<String, CommandGenerator> commands = new HashMap<>();

    @SneakyThrows
    public String getLessonsByGroup(Update update, String command, String day) {
        UserVO userVO;
        if (!isNull(update.getMessage()))
            userVO = restTemplateService.getForObject(
                    UserVO.class,
                    GET_USER_BY_CHAT_ID,
                    update.getMessage().getChatId());
        else userVO = restTemplateService
                .getForObject(
                        UserVO.class,
                        GET_USER_BY_CHAT_ID,
                        Long.valueOf(
                                update.getCallbackQuery()
                                        .getFrom()
                                        .getId()));

        GroupVO groupJson = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, command);

        if (isNull(groupJson))
            return env.getProperty(isNull(update.getMessage()) ? update.getCallbackQuery().getFrom().getLanguageCode() + ".no_response" : update.getMessage().getFrom().getLanguageCode() + ".no_response");

        saveHistory(update, HistoryEvent.GROUP);

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
        return commands.get("callBackGenerator").generateCommand(update);
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }
}