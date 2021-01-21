package com.flux.dbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.history.History;
import com.flux.dbservice.repository.history.HistoryRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HistoryRepository historyRepository;

    @SneakyThrows
    public String saveHistory(String historyJson) {
        return objectMapper.writeValueAsString(historyRepository.save(objectMapper.readValue(historyJson, History.class)));
    }
}
