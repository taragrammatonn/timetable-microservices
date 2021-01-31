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
import java.util.List;

@Slf4j
public abstract class Bot extends TelegramLongPollingBot {

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    public static final String MY_PINUS = "8============================D";
    public static final String MISHA_PENIS = "{(')}";

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // default implementation
    }

    @SneakyThrows
    protected void sendMessage(Update update, String message) {
        log.info("\n###################--> Message send to: " + update.getMessage().getChat().getFirstName() + " " + update.getMessage().getChat().getLastName() + " <-- ###################\n" +
                "###################--> Message text   : " + update.getMessage().getText() + "          <-- ###################");
        execute(new SendMessage().enableMarkdown(true).setChatId(update.getMessage().getChatId()).setText(message));
    }

    public SendMessage setButtons(long chatId) {
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
        return new SendMessage().setChatId(chatId).setText("Показать размер пениса").setReplyMarkup(inlineKeyboardMarkup);
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
