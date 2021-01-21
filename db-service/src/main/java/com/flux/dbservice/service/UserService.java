package com.flux.dbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.history.History;
import com.flux.dbservice.entity.users.User;
import com.flux.dbservice.repository.history.HistoryRepository;
import com.flux.dbservice.repository.users.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static java.util.Objects.isNull;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @SneakyThrows
    public String saveUser(String userJson) {
        User jsonUser = objectMapper.readValue(userJson, User.class);
        User dbUser = userRepository.findByChatId(jsonUser.getChatId());

        if (!isNull(dbUser)) {
            jsonUser.setId(dbUser.getId());
        }
        return objectMapper.writeValueAsString(userRepository.save(jsonUser));
    }

    @SneakyThrows
    public String getUserByChatId(String chatId) {
        return objectMapper.writeValueAsString(userRepository.findByChatId(Long.valueOf(chatId)));
    }
}
