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

    @RequestMapping("/getDailyParameters")
    public String getDailyParameters() {
        return parserService.getDailyParameters();
    }

    @GetMapping("/getLessons")
    public String getLessons(
            @RequestParam String groupJson,
            @RequestParam String dailyParameters,
            @RequestParam String day,
            @RequestParam String userVo) {
        return parserService.getLessons(groupJson, dailyParameters, day, userVo);
    }

    @GetMapping("/getStudyPlan")
    public String getStudyPlan(
            @RequestParam String semester,
            @RequestParam String userVo) {
        return parserService.getStudyPlan(semester, userVo);
    }
}
