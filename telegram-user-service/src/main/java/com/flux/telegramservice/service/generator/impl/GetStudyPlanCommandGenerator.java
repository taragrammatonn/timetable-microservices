package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
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

@Component
public class GetStudyPlanCommandGenerator implements CommandGenerator {

    protected final RestTemplateService restTemplateService;

    public GetStudyPlanCommandGenerator(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        return new SendMessage(update.getMessage().getChatId(), "Alege semestru").setReplyMarkup(setButtons());
    }

    public static InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Semestru I").setCallbackData("tbSemI"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Semestru II").setCallbackData("tbSemII"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getInputCommand() {
        return "/planStudii";
    }
}
