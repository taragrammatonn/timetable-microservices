package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.botconfiguration.Bot;
import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.service.project.UserService;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@Component
public class StartMessageGenerator extends Bot implements CommandGenerator {

    protected final UserService userService;
    protected final SendMessage sendMessage;

    public StartMessageGenerator(UserService userService, SendMessage sendMessage) {
        this.userService = userService;
        this.sendMessage = sendMessage;
    }

    @Override
    public void generateCommand(Update update) {
        sendMessage(update, userService.completeUser(userService.addNewUser(update)));
    }

    @Override
    public String getInputCommand() {
        return "/start";
    }

    @Override
    protected void sendMessage(Update update, String message) {
        sendMessage.setChatId(update.getMessage().getChatId());
        sendMessage.enableMarkdown(true);
        sendMessage.setText(message);
        sendMessage.setReplyMarkup(setStickyButtons());
    }

    public ReplyKeyboardMarkup setStickyButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Grupa"));
        keyboardFirstRow.add(new KeyboardButton("Profesor"));
        keyboardFirstRow.add(new KeyboardButton("Hata"));
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }
}
