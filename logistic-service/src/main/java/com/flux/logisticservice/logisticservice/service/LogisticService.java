package com.flux.logisticservice.logisticservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogisticService {

    // PARSING_SERVICE API's
    private static final String PARSING_SERVICE = "http://PARSING-SERVICE/lessons/api";
    private static final String GET_GROUPS = "/groups";
    private static final String GET_LESSONS_BY_GROUP = "/lessonsByGroup?groupJson={groupJson}&dailyParameters={dailyParameters}";
    private static final String GET_DAILY_PARAMETERS = "/getDailyParameters";

    // DB-SERVICE API's
    private static final String DB_SERVICE = "http://DB-SERVICE/api-gateway";
    private static final String SAVE_USER = "/saveUser";
    private static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    private static final String SAVE_DAILY_PARAMETERS = "/saveDailyParameters";
    public static final String GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL = "/getDailyParametersByWeekNotNull";
    public static final String SAVE_HISTORY = "/saveHistory";

    private final RestTemplate restTemplate;

    public LogisticService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> addUser(String userJson) {
        return new ResponseEntity<>(restTemplate.postForObject(DB_SERVICE + SAVE_USER, userJson, String.class), HttpStatus.OK);
    }

    public String findLessonsByGroup(String groupName) {
        return restTemplate.getForObject(DB_SERVICE + FIND_GROUP, String.class, groupName);
    }

    public String getAllGroups() {
        return restTemplate.getForObject(PARSING_SERVICE + GET_GROUPS, String.class);
    }

    public String getLessonsByGroup(String groupJson) {
        return restTemplate.getForObject(
                PARSING_SERVICE + GET_LESSONS_BY_GROUP, String.class, groupJson,
                restTemplate.postForObject(
                        DB_SERVICE + SAVE_DAILY_PARAMETERS,
                        restTemplate.getForObject(
                                PARSING_SERVICE + GET_DAILY_PARAMETERS,
                                String.class
                        ), String.class
                )
        );
    }

    public String getDailyParametersByWeekNotNull() {
        return restTemplate.getForObject(DB_SERVICE + GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL, String.class);
    }

    public ResponseEntity<String> saveHistory(String historyJson) {
        return new ResponseEntity<>(restTemplate.postForObject(DB_SERVICE + SAVE_HISTORY, historyJson, String.class), HttpStatus.OK);
    }
}
