package com.flux.parsingservice.service;


import com.flux.parsingservice.parser.Parser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

    private final Parser parser;

    public ParserService(Parser parser) {
        this.parser = parser;
    }

    @SneakyThrows
    public String getGroups() {
        return parser.getGroups();
    }

    @SneakyThrows
    public String getTeachers() {
        return parser.getTeachers();
    }

    @SneakyThrows
    public String getAudiences() {
        return parser.getAudiences();
    }

    public String getDailyParameters() {
        return parser.getDailyParameters();
    }

    @SneakyThrows
    public String getLessons(String groupJson, String dailyParameters, String day) {
        return parser.getLessons(groupJson, dailyParameters, day);
    }
}
