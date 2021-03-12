package com.flux.parsingservice.service;

import com.flux.parsingservice.parser.LessonsParser;
import com.flux.parsingservice.parser.StudyPlanParser;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

@Service
public class ParserService {

    private final LessonsParser parser;
    private final StudyPlanParser studyPlanParser;

    public ParserService(LessonsParser parser, StudyPlanParser studyPlanParser) {
        this.parser = parser;
        this.studyPlanParser = studyPlanParser;
    }

    public String getDailyParameters() {
        return parser.getDailyParameters();
    }

    @SneakyThrows
    public String getLessons(String groupJson, String dailyParameters, String day, String userVo) {
        return parser.getLessons(groupJson, dailyParameters, day, userVo);
    }

    @SneakyThrows
    public String getStudyPlan(String semester, String userVo) {
        return studyPlanParser.generateWebRequest(semester, userVo);
    }
}
