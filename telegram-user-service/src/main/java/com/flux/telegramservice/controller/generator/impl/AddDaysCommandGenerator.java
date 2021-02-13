package com.flux.telegramservice.controller.generator.impl;

import com.flux.telegramservice.controller.generator.CommandGenerator;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.Arrays;
import java.util.List;


@Getter
@Setter
@Component
public abstract class AddDaysCommandGenerator implements CommandGenerator {

    private final RestTemplateService restTemplateService;
    private final BotService botService;

    @Autowired
    protected AddDaysCommandGenerator(RestTemplateService restTemplateService, BotService botService) {
        this.restTemplateService = restTemplateService;
        this.botService= botService;
    }

    public static final String LIUBOMIR_PINUS = "Liubomir penis\n{(')}";
    public static final String MISHA_PENIS = "Misha pinus\n8============================D";

    private List<String> commandsList = Arrays.asList("+1d", "+2d", "+1w", "Misha", "Liubomir");

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        UserVO userVO = restTemplateService.getUserByChatId(Long.valueOf(update.getCallbackQuery().getFrom().getId()));
        String command = userVO.getUserGroup();

        String response = switch (update.getCallbackQuery().getData()) {
            case "+1d" -> botService.searchCommand(update, command, "2");
            case "+2d" -> botService.searchCommand(update, command, "3");
            case "+1w" -> botService.searchCommand(update, command, "nextWeek");
            case "Liubomir" -> LIUBOMIR_PINUS;
            case "Misha" -> MISHA_PENIS;
            default -> "net takogo";
        };

        return new SendMessage(String.valueOf(update.getCallbackQuery().getFrom().getId()), response);
    }
}
