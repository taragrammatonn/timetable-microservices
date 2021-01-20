package com.flux.parsingservice.controller;

import com.flux.parsingservice.service.ParserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/lessons/api")
public class ParserController {

    private final ParserService parserService;

    public ParserController(ParserService parserService) {
        this.parserService = parserService;
    }

    @RequestMapping("/")
    public String getLessons() {
        return "Connected";
    }

    @RequestMapping("/lessonsByGroup")
    public String getLessons(
            @RequestParam String groupJson,
            @RequestParam String dailyParameters) {
        return parserService.getLessons(groupJson, dailyParameters);
    }

    @RequestMapping("/groups")
    public String getGroups() {
        return parserService.getGroups();
    }

    @RequestMapping("/teachers")
    public String getTeachers() {
        return parserService.getTeachers();
    }

    @RequestMapping("/audiences")
    public String getAudiences() {
        return parserService.getAudiences();
    }

    @RequestMapping("/getDailyParameters")
    public String getDailyParameters() {
        return parserService.getDailyParameters();
    }
}
