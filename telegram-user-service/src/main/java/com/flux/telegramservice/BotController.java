package com.flux.telegramservice;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.service.BotService;
import com.flux.telegramservice.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
@Slf4j
public class BotController extends Bot {

    @Autowired
    private BotService botService;

    @Autowired
    private UserService userService;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        switch (update.getMessage().getText()) {
            case "/start" -> sendMessage(update, userService.completeUser(botService.addNewUser(update)));
            default ->  sendMessage(update, botService.findLessonsByGroup(update.getMessage().getText()));
        }
    }

    @SneakyThrows
    private void sendMessage(Update update, String message) {
        execute(new SendMessage().enableMarkdown(true).setChatId(update.getMessage().getChatId()).setText(message));
    }
}
