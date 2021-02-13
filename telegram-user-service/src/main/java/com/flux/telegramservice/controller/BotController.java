package com.flux.telegramservice.controller;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.controller.generator.impl.AddDaysCommandGenerator;
import com.flux.telegramservice.controller.generator.impl.GroupMessageGenerator;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class BotController extends Bot {

    private final Map<String, CommandGenerator> commands = new HashMap<>();

    private final BotService botService;
    private final RestTemplateService restTemplateService;

    @Autowired
    public BotController(BotService botService, RestTemplateService restTemplateService) {
        this.botService = botService;
        this.restTemplateService = restTemplateService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        send(update);
    }

    @SneakyThrows
    public void send(Update update) {

        if (update.getMessage() != null) {
            String command = update.getMessage().getText();
            CommandGenerator commandGenerator = commands.get(command);

            if (commandGenerator != null) {
                execute(commandGenerator.generateCommand(update));
            } else sendMessage(update, botService.searchCommand(update, command, "1"));

        } else if (update.getCallbackQuery().getData() != null) {
            String buttonCommand = update.getCallbackQuery().getData();
            AddDaysCommandGenerator addDaysCommandGenerator = new AddDaysCommandGenerator(restTemplateService, botService) {

                @Override
                public String getInputCommand() {
                    return buttonCommand;
                }
            };

            if (addDaysCommandGenerator.getCommandsList().contains(buttonCommand)) {
                execute(addDaysCommandGenerator.generateCommand(update));
            }
        }
    }

    public void register(String code, CommandGenerator generator) {
        commands.put(code, generator);
    }

    @SneakyThrows
    private void sendMessage(Update update, String message) {
        log.info("\n###################--> Message send to: " + update.getMessage().getChat().getFirstName() + " " + update.getMessage().getChat().getLastName() + " <-- ###################\n" +
                "###################--> Message text   : " + update.getMessage().getText() + "          <-- ###################");
        execute(
                new SendMessage()
                        .enableMarkdown(true)
                        .setChatId(update.getMessage().getChatId())
                        .setText(message)
                        .setReplyMarkup(GroupMessageGenerator.setButtons())
        );
    }
}
