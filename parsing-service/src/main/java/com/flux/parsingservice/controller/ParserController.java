package com.flux.parsingservice.controller;

import com.flux.parsingservice.service.ParserService;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/getLessons")
    public String getLessons(
            @RequestParam String groupJson,
            @RequestParam String dailyParameters,
            @RequestParam String day) {
        return parserService.getLessons(groupJson, dailyParameters, day);
    }

    @GetMapping("/getStudyPlan")
    public String getStudyPlan(
            @RequestParam String group,
            @RequestParam String semester) {
        return parserService.getStudyPlan(group, semester);
    }
}
