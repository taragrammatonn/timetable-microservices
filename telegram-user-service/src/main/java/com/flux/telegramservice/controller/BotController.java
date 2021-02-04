package com.flux.telegramservice.controller;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.controller.generator.impl.AddDaysCommandGenerator;
import com.flux.telegramservice.service.project.BotService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class BotController extends Bot {

    private final Map<String, CommandGenerator> commands = new HashMap<>();

    private final BotService botService;

    public BotController(BotService botService) {
        this.botService = botService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        send(update);
    }

    @SneakyThrows
    public void send(Update update) {
        String command;

        if (update.getMessage() != null) {
            command = update.getMessage().getText();
            CommandGenerator commandGenerator = commands.get(command);

            if (commandGenerator == null) {
                String response = botService.searchCommand(command, update);

                if (isNull(response)) {
                    throw new UnsupportedOperationException("Command \"" + command + "\" not supported yet.");
                }

                sendMessage(update, response);
            } else execute(commandGenerator.generateCommand(update));
        } else {
            command = update.getCallbackQuery().getData();

            AddDaysCommandGenerator addDaysCommandGenerator = new AddDaysCommandGenerator() {

                @Override
                public SendMessage generateCommand(Update update) {
                    return super.generateCommand(update);
                }

                @Override
                public String getInputCommand() {
                    return command;
                }
            };

            if (addDaysCommandGenerator.getCommandsList().contains(command)) {
                addDaysCommandGenerator.generateCommand(update);
            }
        }
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }
}
