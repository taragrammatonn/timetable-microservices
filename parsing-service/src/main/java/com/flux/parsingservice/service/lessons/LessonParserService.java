package com.flux.parsingservice.service.lessons;


import com.flux.parsingservice.parser.lessons.LessonsParser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class LessonParserService {

    private final LessonsParser lessonsParser;

    public LessonParserService(LessonsParser lessonsParser) {
        this.lessonsParser = lessonsParser;
    }

    @SneakyThrows
    public String getLessons(String groupJson, String dailyParameters) {
        return lessonsParser.getLessons(groupJson, dailyParameters);
    }

    @SneakyThrows
    public String getGroups() {
        return lessonsParser.getGroups();
    }

    @SneakyThrows
    public String getTeachers() {
        return lessonsParser.getTeachers();
    }

    @SneakyThrows
    public String getAudiences() {
        return lessonsParser.getAudiences();
    }

    public String getDailyParameters() {
        return lessonsParser.getDailyParameters();
    }
}
