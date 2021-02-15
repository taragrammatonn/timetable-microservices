package com.flux.telegramservice.service.generator;

import com.flux.telegramservice.service.project.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandGenerator {
    SendMessage generateCommand(Update update);

    String getInputCommand();

    @Autowired
    default void registerMySelf(BotService botService) {
        botService.register(getInputCommand(), this);
    }
}
