package com.flux.telegramservice.jms;

import com.flux.telegramservice.entity.UserVO;
import lombok.extern.log4j.Log4j2;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Log4j2
@EnableJms
@Component
public class MessageConsumer {

//    @JmsListener(destination = "user-queue")
//    public void userListener(UserVO userVO){
//        log.info("Message received {} ", userVO);
//    }
//
//    @JmsListener(destination = "activity-queue")
//    public void activityListener(UserVO userVO){
//        log.info("Message received {} ", userVO);
//    }
}
