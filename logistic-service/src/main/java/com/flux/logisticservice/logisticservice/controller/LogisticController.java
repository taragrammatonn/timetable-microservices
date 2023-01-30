package com.flux.logisticservice.logisticservice.controller;

import com.flux.logisticservice.logisticservice.service.LogisticService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/logistic-api")
public class LogisticController {

    private final LogisticService logisticService;

    public LogisticController(LogisticService logisticService) {
        this.logisticService = logisticService;
    }

    @RequestMapping("/addUser") @ResponseBody
    public ResponseEntity<String> addUser(@RequestBody String userJson) {
        return logisticService.addUser(userJson);
    }

    /* find id and name for group/teacher/audience */
    @RequestMapping("/findGroup")
    public String findGroup(@RequestParam String groupName) {
        return logisticService.findGroup(groupName);
    }

    @RequestMapping("/findTeacher")
    public String findTeacher(@RequestParam String teacherName) {
        return logisticService.findTeacher(teacherName);
    }

    @RequestMapping("/findAudience")
    public String findAudience(@RequestParam String audienceName) {
        return logisticService.findAudience(audienceName);
    }

    /* find all groups/teachers/audiences from orar.usarb.com */
    @GetMapping("/getAllAudiences")
    public ResponseEntity<String> getAllAudiences() {
        return logisticService.getAllAudiences();
    }

    @GetMapping("/getAllGroups")
    public ResponseEntity<String> getAllGroups() {
        return logisticService.getAllGroups();
    }

    @GetMapping("/getAllTeachers")
    public ResponseEntity<String> getAllTeachers() {
        return logisticService.getAllTeachers();
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
            @RequestParam String day,
            @RequestParam String userVo) {
        return logisticService.getLessons(groupJson, day, userVo);
    }

    @GetMapping("getStudyPlan")
    public String getStudyPlan(
            @RequestParam String semester,
            @RequestParam String userVo) {
        return logisticService.getStudyPlan(semester, userVo);
    }

    @GetMapping("/refreshGroups")
    public ResponseEntity<Boolean> getUserOption() {
        return new ResponseEntity<>(logisticService.refreshGroups(), HttpStatus.OK);
    }
}
