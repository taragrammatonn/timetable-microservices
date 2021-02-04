package com.flux.telegramservice.botconfiguration;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public abstract class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    public static final String Liubomir_PINUS = "{(')}";
    public static final String MISHA_PENIS = "8============================D";

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // default implementation
    }

    @SneakyThrows
    protected void sendMessage(Update update, String message) {
        log.info("\n###################--> Message send to: " + update.getMessage().getChat().getFirstName() + " " + update.getMessage().getChat().getLastName() + " <-- ###################\n" +
                "###################--> Message text   : " + update.getMessage().getText() + "          <-- ###################");
        execute(new SendMessage().enableMarkdown(true).setChatId(update.getMessage().getChatId()).setText(message).setReplyMarkup(setButtons()));
    }

    public InlineKeyboardMarkup setButtons() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+1day").setCallbackData("+1d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+2day's").setCallbackData("+2d"));
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("+1week").setCallbackData("+1w"));
        inlineKeyboardMarkup.setKeyboard(Collections.singletonList(keyboardButtonsRow1));
        return inlineKeyboardMarkup;
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }
}
