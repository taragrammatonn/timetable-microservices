package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.service.project.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class StartMessageGenerator implements CommandGenerator {

    @Autowired
    protected UserService userService;

    @Override
    public String generateCommand(Update update) {
        return userService.completeUser(
                userService.addNewUser(update)
        );
    }

    @Override
    public String getInputCommand() {
        return "/start";
    }
}
