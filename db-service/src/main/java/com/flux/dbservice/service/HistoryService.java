package com.flux.dbservice.service;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.history.History;
import com.flux.dbservice.entity.users.User;
import com.flux.dbservice.repository.history.HistoryRepository;
import com.flux.dbservice.repository.users.UserRepository;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final ObjectMapper objectMapper;

    private final HistoryRepository historyRepository;

    private final UserRepository userRepository;

    public HistoryService(ObjectMapper objectMapper, HistoryRepository historyRepository, UserRepository userRepository) {
        this.objectMapper = objectMapper;
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    @SneakyThrows
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public String saveHistory(String historyJson) {
        History history = null;

        try {
            history = objectMapper.readValue(historyJson, History.class);
            User user = userRepository.findByChatId(history.getUserChatId());
            history.setUser(user);

            return objectMapper.writeValueAsString(historyRepository.save(history));
        } catch (JsonProcessingException e) {
            return objectMapper.writeValueAsString(history);
        }
    }
}
