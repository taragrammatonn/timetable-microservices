package com.flux.telegramservice.controller.generator;

import com.flux.telegramservice.controller.BotController;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public interface CommandGenerator {
    String generateCommand(Update update);

    String getInputCommand();

    @Autowired
    default void registerMySelf(BotController botController) {
        botController.register(getInputCommand(), this);
    }
}
