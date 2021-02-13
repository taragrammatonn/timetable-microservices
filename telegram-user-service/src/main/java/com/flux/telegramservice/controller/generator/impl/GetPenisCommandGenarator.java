package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GetPenisCommandGenarator implements CommandGenerator {

    @Override
    public SendMessage generateCommand(Update update) {
        String response = "Чей апарат желаете увидеть";
        return new SendMessage(update.getMessage().getChatId(), response).setReplyMarkup(setButtons());
    }

    public InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Misha").setCallbackData("Misha"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Liubomir").setCallbackData("Liubomir"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getInputCommand() {
        return "/penis";
    }
}
