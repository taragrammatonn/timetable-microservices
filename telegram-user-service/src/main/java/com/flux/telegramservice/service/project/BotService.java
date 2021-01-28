package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.HistoryEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.NULL;
import static java.util.Objects.isNull;

@Service
@Slf4j
public class BotService extends AbstractTelegramService {

    public String findLessonsByGroup(Update update) {
        String groupJson = restTemplateService.findGroup(update.getMessage().getText());

        if (isNull(groupJson) || groupJson.equals(NULL)) return "Такой группы не существует!";

        saveHistory(update, HistoryEvent.GROUP);

        return restTemplateService.getLessonsByGroup(groupJson);
    }
}
