package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

public abstract class AbstractTelegramService {

    @Autowired
    protected RestTemplateService restTemplateService;

    @Autowired
    protected UserService userService;

    @Autowired
    protected BotService botService;

    protected boolean saveHistory(Update update, HistoryEvent event) {
        return restTemplateService.saveHistory(update, event);
    }
}
