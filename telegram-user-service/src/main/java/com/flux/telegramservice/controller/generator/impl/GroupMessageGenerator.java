package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Component
public class GroupMessageGenerator implements CommandGenerator {

    protected final BotService botService;
    protected final RestTemplateService restTemplateService;

    public GroupMessageGenerator(BotService botService, RestTemplateService restTemplateService) {
        this.botService = botService;
        this.restTemplateService = restTemplateService;
    }

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        restTemplateService.saveUserOption(new UserOptionVO().groupSelected(update.getMessage().getChatId()));

        return new SendMessage(update.getMessage().getChatId(), "Введите навзание группы.");
    }

    @Override
    public String getInputCommand() {
        return "Grupa";
    }

    public InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton();
        inlineKeyboardButton1.setText("Liubomir");
        inlineKeyboardButton1.setCallbackData("Button \"Liubomir\" has been pressed");
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        List<InlineKeyboardButton> keyboardButtonsRow2 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Hozyain").setCallbackData("ALPHA"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Misha").setCallbackData("Misha"));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
