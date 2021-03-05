package com.flux.logisticservice.logisticservice.controller;

import com.flux.logisticservice.logisticservice.service.LogisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
        return logisticService.findGroup(groupName);
    }

    @GetMapping("/getAllGroups")
    public ResponseEntity<String> getAllGroups() {
        return logisticService.getAllGroups();
    }

    @GetMapping("/getDailyParametersByWeekNotNull")
    public String getDailyParametersByWeekNotNull() {
        return logisticService.getDailyParametersByWeekNotNull();
    }

    @PostMapping("/saveHistory")
    @ResponseBody
    public ResponseEntity<String> saveHistory(@RequestBody String historyJson) {
        return logisticService.saveHistory(historyJson);
    }

    @GetMapping("/getUser")
    public String getUserByChatId(@RequestParam String chatId) {
        return logisticService.getUserByChatId(chatId);
    }

    @RequestMapping("/saveUserOption") @ResponseBody
    public void saveUserOption(@RequestBody String userOptionJson) {
        logisticService.saveUserOption(userOptionJson);
    }

    @GetMapping("/getUserOption")
    public ResponseEntity<String> getUserOption(@RequestParam Long chatId) {
        return new ResponseEntity<>(logisticService.getUserOptionByChatId(chatId), HttpStatus.OK);
    }

    @GetMapping("/getLessons")
    public String lessonsDay(
            @RequestParam String groupJson,
            @RequestParam String day) {
        return logisticService.getLessons(groupJson, day);
    }

    @GetMapping("getStudyPlan")
    public String getStudyPlan(@RequestParam String group) {
        return logisticService.getStudyPlan(group);
    }
}
