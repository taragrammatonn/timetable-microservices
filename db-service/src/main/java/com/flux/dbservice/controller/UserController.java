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
    public ResponseEntity<String> checkConnection() {
        return new ResponseEntity<>(parsingService.checkConnection(), HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping(value = "/saveUser")
    public ResponseEntity<String> saveUser(@RequestBody String user) {
        return new ResponseEntity<>(userService.saveUser(user), HttpStatus.OK);
    }

    @GetMapping("/findGroup")
    public ResponseEntity<String> findGroup(@RequestParam String groupName) {
        return new ResponseEntity<>(parsingService.findLessonsByGroup(groupName), HttpStatus.OK);
    }

    @PostMapping("/saveDailyParameters")
    public ResponseEntity<String> saveDailyParameters(@RequestBody String parametersJson) {
        return new ResponseEntity<>(parsingService.saveDailyParameters(parametersJson), HttpStatus.OK);
    }

    @GetMapping("/getDailyParametersByWeekNotNull")
    public ResponseEntity<String> getDailyParametersByWeekNotNull() {
        return new ResponseEntity<>(parsingService.getDailyParametersByWeekNotNull(), HttpStatus.OK);
    }

    @PostMapping("/saveHistory")
    public ResponseEntity<String> saveHistory(@RequestBody String historyJson) {
        return new ResponseEntity<>(historyService.saveHistory(historyJson), HttpStatus.OK);
    }

    @GetMapping("/getUser")
    public ResponseEntity<String> getDailyParametersByWeekNotNull(@RequestParam String chatId) {
        return new ResponseEntity<>(userService.getUserByChatId(chatId), HttpStatus.OK);
    }

    @PostMapping("/saveUserOption")
    public void saveUserOption(@RequestBody String userOptionJson) {
        userService.saveUserOptions(userOptionJson);
    }

    @GetMapping("/getUserOption")
    public ResponseEntity<String> getUserOption(@RequestParam Long chatId) {
        return new ResponseEntity<>(userService.getUserOptionByChatId(chatId), HttpStatus.OK);
    }
}