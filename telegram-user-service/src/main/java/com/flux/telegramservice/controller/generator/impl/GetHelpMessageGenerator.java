package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Component
public class GetHelpMessageGenerator implements CommandGenerator {

    @Override
    public SendMessage generateCommand(Update update) {
        String response = "Здарова сталкер!\n " +
                "А если ты девушка то, \"Привет красавица!\"\n" +
                "Этот бот сделан группой энтузиастов (ну как \"группой\", бота создал @tetragramatonn а @MihaiCojusnean так подсосался под конец) для студетов и хорошых учителей USARB.\n" +
                "Пока что бот поддерживает следующие команды:\n" +
                "/penis\n" +
                "Другие команды в разработке.\n" +
                "{\nЧерный список @UsarbOrarBot:\n Zastanceanu Liubov\n}";
        return new SendMessage(update.getMessage().getChatId(), response);
    }

    @Override
    public String getInputCommand() {
        return "/help";
    }
}
