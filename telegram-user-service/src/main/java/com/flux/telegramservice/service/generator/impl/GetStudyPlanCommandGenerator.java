package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Component
public class GetStudyPlanCommandGenerator implements CommandGenerator {

    private final Environment env;

    public GetStudyPlanCommandGenerator(Environment env) {
        this.env = env;
    }

    @Override
    public SendMessage generateCommand(Update update) {
        return new SendMessage(update.getMessage().getChatId(),
                Objects.requireNonNull(env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".choose_semester")))
                .setReplyMarkup(setButtons(update));
    }

    public InlineKeyboardMarkup setButtons(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(
                env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".button.semester_I")).setCallbackData("tbSemI"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText(
                env.getProperty(update.getMessage().getFrom().getLanguageCode() + ".button.semester_II")).setCallbackData("tbSemII"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getInputCommand() {
        return "/studyPlan";
    }
} 