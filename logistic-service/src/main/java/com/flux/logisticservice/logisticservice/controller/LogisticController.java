package com.flux.logisticservice.logisticservice.controller;

import com.flux.logisticservice.logisticservice.service.LogisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistic-api")
public class LogisticController {

    @Autowired
    private LogisticService logisticService;

    @RequestMapping("/addUser") @ResponseBody
    public ResponseEntity<String> addUser(@RequestBody String userJson) {
        return logisticService.addUser(userJson);
    }

    @RequestMapping("/findGroup")
    public String findGroup(@RequestParam String groupName) {
        return logisticService.findLessonsByGroup(groupName);
    }

    @GetMapping("/getAllGroups")
    public String getAllGroups() {
        return logisticService.getAllGroups();
    }

    @RequestMapping("/lessonByGroup")
    public String lessonByGroup(@RequestParam String groupJson) {
        return logisticService.getLessonsByGroup(groupJson);
    }

    @GetMapping("/getDailyParametersByWeekNotNull")
    public String getDailyParametersByWeekNotNull() {
        return logisticService.getDailyParametersByWeekNotNull();
    }

    @GetMapping("/getUser")
    public String getUserByChatId(@RequestParam String chatId) {
        return logisticService.getUserByChatId(chatId);
    }
}
