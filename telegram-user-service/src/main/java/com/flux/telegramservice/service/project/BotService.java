package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.generator.impl.AddDaysCommandGenerator;
import com.flux.telegramservice.util.exception.CannotSaveHistoryException;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static com.flux.telegramservice.util.Links.NULL;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class BotService extends AbstractTelegramService {

    private final Map<String, CommandGenerator> commands = new HashMap<>();

    @SneakyThrows
    public String getLessonsByGroup(Update update, String command) {
        String groupJson = findGroup(command);

        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        if (!saveHistory(update, HistoryEvent.GROUP)) {
            throw new CannotSaveHistoryException("Can't save History at entity: " + update.getMessage().getChatId());
        }

        return restTemplateService.getLessonsWithParam(groupJson, null);
    }

    public String findGroup(String group) {
        return restTemplateService.findGroup(group);
    }

    @SneakyThrows
    public SendMessage messageProcessing(Update update) {
        return !isNull(commands.get(update.getMessage().getText())) ?
                commands.get(update.getMessage().getText()).generateCommand(update) :
                generateMessage(update, update.getMessage().getText());
    }

    public SendMessage callBackQueryProcessing(Update update) {
        String command = update.getCallbackQuery().getData();

        AddDaysCommandGenerator addDaysCommandGenerator = new AddDaysCommandGenerator(restTemplateService, botService) {
            @Override
            public String getInputCommand() {
                return command;
            }
        };

        return !isNull(addDaysCommandGenerator.getCommandsList().get(command)) ?
                addDaysCommandGenerator.generateCommand(update) :
        new SendMessage().setChatId(update.getMessage().getChatId()).setText("Неверная команда.");
    }

    public String getLessonsWithParam(String group, String param) {
        return restTemplateService.getLessonsWithParam(group, param);
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }
}
