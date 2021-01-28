package com.flux.telegramservice.controller;

import com.flux.telegramservice.botconfiguration.Bot;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class BotController extends Bot {

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {

        switch (messageProcessing(update)) {
            case "/start" -> {
                sendMessage(update, userService.completeUser(userService.addNewUser(update)));
                execute(setButtons(update.getMessage().getChatId()));
            }

            case "ALPHA" -> {
                execute(new SendMessage().setChatId(getChatId(update)).setText(MY_PINUS));
            }

            case "Misha" -> {
                execute(new SendMessage().setChatId(getChatId(update)).setText(MISHA_PENIS));
            }

            default -> {
                sendMessage(update, botService.findLessonsByGroup(update));
            }
        }
    }

    private String messageProcessing(Update update) {
        if (!isNull(update.getMessage()))
            return update.getMessage().getText();
        else return update.getCallbackQuery().getData();
    }

    private Long getChatId(Update update) {
        return update.getMessage() != null ? update.getMessage().getChatId() : update.getCallbackQuery().getMessage().getChatId();
    }
}
