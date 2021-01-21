package com.flux.dbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.history.History;
import com.flux.dbservice.entity.users.User;
import com.flux.dbservice.repository.history.HistoryRepository;
import com.flux.dbservice.repository.users.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryRepository historyRepository;

    @Autowired
    private UserRepository userRepository;

    @SneakyThrows
    public String saveHistory(String historyJson) {
        History history = objectMapper.readValue(historyJson, History.class);
        User user = userRepository.findByChatId(history.getUserChatId());
        history.setUser(user);

        return objectMapper.writeValueAsString(historyRepository.save(history));
    }
}
