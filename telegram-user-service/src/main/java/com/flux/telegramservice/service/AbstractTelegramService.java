package com.flux.telegramservice.service;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.flux.telegramservice.util.Links.LOGISTIC_SERVICE;
import static com.flux.telegramservice.util.Links.SAVE_HISTORY;

public abstract class AbstractTelegramService {

    @Autowired
    protected RestTemplate restTemplate;

    @Autowired
    protected UserService userService;

    @Autowired
    protected BotService botService;

    void saveHistory(Update update, HistoryEvent event) {
        restTemplate.postForObject(
                LOGISTIC_SERVICE + SAVE_HISTORY,
                new HistoryVO(
                        event,
                        update.getMessage().getText(),
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                        update.getMessage().getChatId()),
                HistoryVO.class
        );
    }
}
