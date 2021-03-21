package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
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

import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;
import static java.util.Objects.isNull;

@Component
public class GroupMessageGenerator implements CommandGenerator {

    private final Environment env;
    protected final BotService botService;
    protected final RestTemplateService restTemplateService;

    public GroupMessageGenerator(BotService botService, RestTemplateService restTemplateService, Environment env) {
        this.botService = botService;
        this.restTemplateService = restTemplateService;
        this.env = env;
    }

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        UserVO userVO = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId());
        restTemplateService.saveUserOption(new UserOptionVO().groupSelected(update.getMessage().getChatId()));

        if (isNull(userVO.getUserGroup())) {
            return new SendMessage(update.getMessage().getChatId(),
                    Objects.requireNonNull(env.getProperty(userVO.getUserLanguage() + ".get_group")));
        }

        String response = botService.searchCommand(userVO.getUserGroup(), update);
        return new SendMessage(update.getMessage().getChatId(), response).setReplyMarkup(setButtons(update));
    }

    public InlineKeyboardMarkup setButtons(Update update) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton()
                .setText(env.getProperty(
                        update.getMessage().getFrom().getLanguageCode() + ".button.+1day")).setCallbackData("+1d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton()
                .setText(env.getProperty(
                        update.getMessage().getFrom().getLanguageCode() + ".button.+2day's")).setCallbackData("+2d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton()
                .setText(env.getProperty(
                        update.getMessage().getFrom().getLanguageCode() + ".button.+week")).setCallbackData("+1w"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getInputCommand() {
        return "Group";
    }
}