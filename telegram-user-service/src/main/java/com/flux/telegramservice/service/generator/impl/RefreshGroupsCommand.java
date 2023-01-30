package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class RefreshGroupsCommand implements CommandGenerator {

    @Autowired
    public RefreshGroupsCommand(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    private final RestTemplateService restTemplateService;

    @Override
    public SendMessage generateCommand(Update update) {
        return new SendMessage(update.getMessage().getChatId(), String.valueOf(restTemplateService.refreshGroupsRequest()));
    }

    @Override
    public String getInputCommand() {
        return "/refresh_groups";
    }
}
