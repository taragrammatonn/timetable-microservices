package com.flux.dbservice.controller;

import com.flux.dbservice.service.HistoryService;
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

    private final HistoryService historyService;

    public UserController(ParsingService parsingService, UserService userService, HistoryService historyService) {
        this.parsingService = parsingService;
        this.userService = userService;
        this.historyService = historyService;
    }

    @GetMapping("/check-connection")
    public String checkConnection() {
        return parsingService.checkConnection();
    }

    @SneakyThrows
    @PostMapping(value = "/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody String user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
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

    @PostMapping("/saveHistory")
    public ResponseEntity<String> saveHistory(@RequestBody String historyJson) {
        return new ResponseEntity<>(historyService.saveHistory(historyJson), HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<String> getDailyParametersByWeekNotNull(@RequestParam String chatId) {
        return new ResponseEntity<>(userService.getUserByChatId(chatId), HttpStatus.OK);
    }
}