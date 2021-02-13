package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.util.Objects.isNull;

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
        UserVO userVO = restTemplateService.getUserByChatId(update.getMessage().getChatId());

        if (isNull(userVO.getUserGroup())) {
            restTemplateService.saveUserOption(new UserOptionVO().groupSelected(update.getMessage().getChatId()));
            return new SendMessage(update.getMessage().getChatId(), "Введите навзание группы.");
        }

        String command = userVO.getUserGroup();
        String response = botService.searchCommand(update, command, "1");
        return new SendMessage(update.getMessage().getChatId(), response).setReplyMarkup(setButtons());
    }

    public static InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+1day").setCallbackData("+1d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+2day's").setCallbackData("+2d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+1week").setCallbackData("+1w"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getInputCommand() {
        return "Grupa";
    }
}
