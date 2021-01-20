package com.flux.dbservice.controller;

import com.flux.dbservice.service.ParsingService;
import com.flux.dbservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api-gateway")
public class UserController {

    @Autowired
    private ParsingService parsingService;

    @Autowired
    private UserService userService;

    @GetMapping("/check-connection")
    public String checkConnection() {
        return parsingService.checkConnection();
    }

    @PostMapping("/saveUser")
    public String saveUser(@RequestBody String userJson) {
        return userService.saveUser(userJson);
    }

    @GetMapping("/findGroup")
    public String findGroup(@RequestParam String groupName) {
        return parsingService.findLessonsByGroup(groupName);
    }

    @PostMapping("/refreshGroups")
    public void refreshGroups(@RequestBody String groupsJson) {
        parsingService.refreshGroups(groupsJson);
    }

    @PostMapping("/saveDailyParameters")
    public String saveDailyParameters(@RequestBody String parametersJson) {
        return parsingService.saveDailyParameters(parametersJson);
    }

    @GetMapping("/getDailyParametersByWeekNotNull")
    public String getDailyParametersByWeekNotNull() {
        return parsingService.getDailyParametersByWeekNotNull();
    }
}