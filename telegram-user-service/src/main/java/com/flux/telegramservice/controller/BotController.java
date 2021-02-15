package com.flux.telegramservice.controller;

import com.flux.telegramservice.botconfiguration.Bot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class BotController extends Bot {

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        switch (update.getMessage().getText()) {
            case "/start" -> {
                sendMessage(update, userService.completeUser(userService.addNewUser(update)));
                execute(setButtons(update.getMessage().getChatId()));
            }
            default -> {
                sendPenis(update.getCallbackQuery().getData(), update.getMessage().getChatId());
                sendMessage(update, botService.findLessonsByGroup(update));
            }
        }
    }
}
