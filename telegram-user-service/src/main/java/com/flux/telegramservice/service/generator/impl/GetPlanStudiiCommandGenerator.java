package com.flux.telegramservice.service.generator.impl;

import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import static com.flux.telegramservice.util.Links.*;

@Component
public class GetPlanStudiiCommandGenerator implements CommandGenerator {

    protected final RestTemplateService restTemplateService;

    public GetPlanStudiiCommandGenerator(RestTemplateService restTemplateService) {
        this.restTemplateService = restTemplateService;
    }

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        String userGroup = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId())
                .getUserGroup();
        String response = restTemplateService.getForObject(String.class, GET_STUDY_PLAN, userGroup);
        return new SendMessage(update.getMessage().getChatId(), response);
    }

    @Override
    public String getInputCommand() {
        return "/planStudii";
    }
}
