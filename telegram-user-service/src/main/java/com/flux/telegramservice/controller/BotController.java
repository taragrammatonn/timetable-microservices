package com.flux.telegramservice.controller;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.service.project.BotService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class BotController extends Bot {

    private final BotService botService;

    @Autowired
    public BotController(BotService botService) {
        this.botService = botService;
    }

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        execute(
                isNull(update.getMessage()) ?
                        botService.callBackQueryProcessing(update) :
                        botService.messageProcessing(update)
        );
    }
}
