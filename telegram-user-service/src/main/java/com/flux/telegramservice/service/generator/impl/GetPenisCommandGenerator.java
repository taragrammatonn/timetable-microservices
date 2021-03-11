package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.security.SecureRandom;

@Component
public class GetPenisCommandGenerator implements CommandGenerator {

    @Override
    public SendMessage generateCommand(Update update) {
        String response = "8" + "=".repeat(Math.max(0, (new SecureRandom().nextInt(30)))) + "D";
        return new SendMessage(update.getMessage().getChatId(), response);
    }

    @Override
    public String getInputCommand() {
        return "/penis";
    }
}
