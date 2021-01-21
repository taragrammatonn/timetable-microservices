package com.flux.telegramservice.botconfiguration;

import com.flux.telegramservice.service.BotService;
import com.flux.telegramservice.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Slf4j
public abstract class Bot extends TelegramLongPollingBot {

    @Autowired
    protected BotService botService;

    @Autowired
    protected UserService userService;

    @Value("${bot.name}")
    private String botUsername;

    @Value("${bot.token}")
    private String botToken;

    @SneakyThrows
    @Override
    public void onUpdateReceived(Update update) {
        // default implementation
    }

    @SneakyThrows
    protected void sendMessage(Update update, String message) {
        log.info("\n###################--> Message send to: " + update.getMessage().getChat().getFirstName() + " " + update.getMessage().getChat().getLastName() + " <-- ###################\n" +
                "###################--> Message text   : " + update.getMessage().getText() + "               <-- ###################");
        execute(new SendMessage().enableMarkdown(true).setChatId(update.getMessage().getChatId()).setText(message));
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
