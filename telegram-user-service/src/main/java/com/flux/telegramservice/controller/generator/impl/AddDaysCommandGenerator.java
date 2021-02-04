package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Component
public abstract class AddDaysCommandGenerator implements CommandGenerator {

    @Autowired
    private RestTemplateService restTemplateService;

    private List<String> commandsList = Arrays.asList("+1d", "+2d", "+1w");
    private String command;

    @Override
    public SendMessage generateCommand(Update update) {
        // todo resolve this
        return null;
    }
}
