package com.flux.parsingservice.service;


import com.flux.parsingservice.parser.LessonsParser;
import com.flux.parsingservice.parser.PlanStudiiParser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

    private final LessonsParser parser;
    private final PlanStudiiParser planStudiiParser;

    public ParserService(LessonsParser parser, PlanStudiiParser planStudiiParser) {
        this.parser = parser;
        this.planStudiiParser = planStudiiParser;
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

    public String getStudyPlan(String group) {
        return planStudiiParser.generateWebRequest(group);
    }
}
