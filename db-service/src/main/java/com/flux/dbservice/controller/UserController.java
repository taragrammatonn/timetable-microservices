package com.flux.dbservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.user.User;
import com.flux.dbservice.service.ParsingService;
import com.flux.dbservice.service.UserService;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController

@RequestMapping("/api-gateway")
public class UserController {

    private final ParsingService parsingService;

    private final UserService userService;

    private final ObjectMapper objectMapper;

    public UserController(ParsingService parsingService, UserService userService, ObjectMapper objectMapper) {
        this.parsingService = parsingService;
        this.userService = userService;
        this.objectMapper = objectMapper;
    }

    @GetMapping("/check-connection")
    public String checkConnection() {
        return parsingService.checkConnection();
    }

    @SneakyThrows
    @PostMapping(value = "/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody String user) {
        return new ResponseEntity<String>(userService.saveUser(user), HttpStatus.OK);
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