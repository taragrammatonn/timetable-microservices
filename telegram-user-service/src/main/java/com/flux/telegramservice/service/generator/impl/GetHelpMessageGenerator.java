package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Objects;

@Component
public class GetHelpMessageGenerator implements CommandGenerator {

    @Autowired
    private Environment env;

    @Override
    public SendMessage generateCommand(Update update) {
        String response = env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".help");
        return new SendMessage(update.getMessage().getChatId(), Objects.requireNonNull(response));
    }

    @Override
    public String getInputCommand() {
        return "/help";
    }
}