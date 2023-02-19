package com.flux.telegramservice.service.generator.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.project.UserService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Queue;
import javax.jms.TextMessage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Component
public class StartMessageGenerator implements CommandGenerator {

//    @Autowired
//    private Queue userQueue;

    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    protected Environment env;

    @Value("${spring.activemq.topic}")
    String topic;

    protected final UserService userService;

    public StartMessageGenerator(UserService userService) {
        this.userService = userService;
    }

    @Override
    @SneakyThrows
    public SendMessage generateCommand(Update update) {
        UserVO userVO = userService.addNewUser(update);
        sendToTopic(userVO);
//        jmsTemplate.convertAndSend(userQueue, userVO);
//        String response = userService.completeUser();
        return new SendMessage(update.getMessage().getChatId(), Objects.requireNonNull(env.getProperty(userVO.getUserLanguage() + ".repeating_start_input")))
                .enableMarkdown(true).setReplyMarkup(setStickyButtons());
    }

    @Override
    public String getInputCommand() {
        return "/start";
    }

    public ReplyKeyboardMarkup setStickyButtons() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        keyboardFirstRow.add(new KeyboardButton("Group"));
        keyboardFirstRow.add(new KeyboardButton("Teacher"));
        keyboardFirstRow.add(new KeyboardButton("Audience"));
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        keyboard.add(keyboardFirstRow);
        replyKeyboardMarkup.setKeyboard(keyboard);

        return replyKeyboardMarkup;
    }

    public void sendToTopic(UserVO userVO) {
        try {
            jmsTemplate.convertAndSend(topic, userVO);
        }
        catch (Exception ex) {
            System.out.println("ERROR in sending message to queue");
        }
    }

    @JmsListener(destination = "user")
    public void receiveMessageFromTopic(UserVO jsonMessage) {
//        String messageData = null;
        System.out.println("Received message in 2nd topic " + jsonMessage);
//        if(jsonMessage instanceof TextMessage) {
//            TextMessage textMessage = (TextMessage)jsonMessage;
//            messageData = textMessage.getText();
            System.out.println("messageData in 2nd listener:" + jsonMessage);
//        }
    }
}