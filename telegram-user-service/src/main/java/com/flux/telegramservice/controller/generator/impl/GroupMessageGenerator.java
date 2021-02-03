package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GroupMessageGenerator extends Bot implements CommandGenerator {

    protected final BotService botService;
    protected final RestTemplateService restTemplateService;

    public GroupMessageGenerator(BotService botService, RestTemplateService restTemplateService) {
        this.botService = botService;
        this.restTemplateService = restTemplateService;
    }

    @Override
    @SneakyThrows
    public void generateCommand(Update update) {
        restTemplateService.saveUserOption(new UserOptionVO().groupSelected(update.getMessage().getChatId()));

        sendMessage(update, "Введите навзание группы.");
    }

    @Override
    public String getInputCommand() {
        return "/group";
    }

    @Override
    protected void sendMessage(Update update, String message) {
        super.sendMessage(update, message);
    }
}
