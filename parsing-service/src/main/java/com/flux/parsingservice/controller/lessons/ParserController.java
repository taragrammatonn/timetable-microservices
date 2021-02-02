package com.flux.parsingservice.controller.lessons;

import com.flux.parsingservice.service.lessons.LessonParserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessons/api")
public class ParserController {

    private final LessonParserService lessonParserService;

    public ParserController(LessonParserService lessonParserService) {
        this.lessonParserService = lessonParserService;
    }

    @RequestMapping("/")
    public String getLessons() {
        return "Connected";
    }

    @RequestMapping("/lessonsByGroup")
    public String getLessons(
            @RequestParam String groupJson,
            @RequestParam String dailyParameters) {
        return lessonParserService.getLessons(groupJson, dailyParameters);
    }

    @RequestMapping("/groups")
    public String getGroups() {
        return lessonParserService.getGroups();
    }

    @RequestMapping("/teachers")
    public String getTeachers() {
        return lessonParserService.getTeachers();
    }

    @RequestMapping("/audiences")
    public String getAudiences() {
        return lessonParserService.getAudiences();
    }

    @RequestMapping("/getDailyParameters")
    public String getDailyParameters() {
        return lessonParserService.getDailyParameters();
    }
}
