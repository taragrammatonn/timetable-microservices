package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
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
public class StartMessageGenerator implements CommandGenerator {

    protected final UserService userService;

    public StartMessageGenerator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public SendMessage generateCommand(Update update) {
        String response = userService.completeUser(userService.addNewUser(update));
        return new SendMessage(update.getMessage().getChatId(), response)
                .enableMarkdown(true).setReplyMarkup(setStickyButtons());
    }

    @Override
    public String getInputCommand() {
        return "/start";
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
