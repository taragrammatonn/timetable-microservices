package com.flux.logisticservice.logisticservice.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LogisticService {

    // PARSING_SERVICE API's
    private static final String PARSING_SERVICE = "http://PARSING-SERVICE/lessons/api";
    private static final String GET_AUDIENCES = "/audiences";
    private static final String GET_GROUPS = "/groups";
    private static final String GET_TEACHERS = "/teachers";
    private static final String GET_LESSONS_WITH_PARAM = "/getLessons?groupJson={groupJson}&dailyParameters={dailyParameters}&day={day}&userVo={userVo}";
    private static final String GET_DAILY_PARAMETERS = "/getDailyParameters";
    private static final String GET_STUDY_PLAN = "/getStudyPlan?semester={semester}&userVo={userVo}";

    // DB-SERVICE API's
    private static final String DB_SERVICE = "http://DB-SERVICE/api-gateway";
    private static final String SAVE_USER = "/saveUser";
    private static final String GET_USER_BY_CHAT_ID = "/getUser?chatId={chatId}";
    private static final String FIND_GROUP = "/findGroup?groupName={groupName}";
    private static final String FIND_TEACHER = "/findTeacher?teacherName={teacherName}";
    private static final String FIND_AUDIENCE = "/findAudience?audienceName={audienceName}";
    private static final String SAVE_DAILY_PARAMETERS = "/saveDailyParameters";
    public static final String GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL = "/getDailyParametersByWeekNotNull";
    public static final String SAVE_HISTORY = "/saveHistory";
    private static final String SAVE_USER_OPTION = "/saveUserOption";
    private static final String GET_USER_OPTION_BY_CHAT_ID = "/getUserOption?chatId={chatId}";

    private final RestTemplate restTemplate;

    public LogisticService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public ResponseEntity<String> addUser(String userJson) {
        return new ResponseEntity<>(restTemplate.postForObject(DB_SERVICE + SAVE_USER, userJson, String.class), HttpStatus.OK);
    }


    /* find id and name for group/teacher/audience */
    public String findGroup(String groupName) {
        return restTemplate.getForObject(DB_SERVICE + FIND_GROUP, String.class, groupName);
    }

    public String findTeacher(String teacherName) {
        return restTemplate.getForObject(DB_SERVICE + FIND_TEACHER, String.class, teacherName);
    }

    public String findAudience(String audienceName) {
        return restTemplate.getForObject(DB_SERVICE + FIND_AUDIENCE, String.class, audienceName);
    }


    /* find all groups/teachers/audiences from orar.usarb.com */
    public  ResponseEntity<String> getAllAudiences() {
        return new ResponseEntity<>(restTemplate.getForObject(PARSING_SERVICE + GET_AUDIENCES, String.class), HttpStatus.OK);
    }

    public  ResponseEntity<String> getAllGroups() {
        return new ResponseEntity<>(restTemplate.getForObject(PARSING_SERVICE + GET_GROUPS, String.class), HttpStatus.OK);
    }

    public  ResponseEntity<String> getAllTeachers() {
        return new ResponseEntity<>(restTemplate.getForObject(PARSING_SERVICE + GET_TEACHERS, String.class), HttpStatus.OK);
    }


    public String getDailyParametersByWeekNotNull() {
        return restTemplate.getForObject(DB_SERVICE + GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL, String.class);
    }

    public ResponseEntity<String> saveHistory(String historyJson) {
        return new ResponseEntity<>(restTemplate.postForObject(DB_SERVICE + SAVE_HISTORY, historyJson, String.class), HttpStatus.OK);
    }

    public String getUserByChatId(String chatId) {
        return restTemplate.getForObject(DB_SERVICE + GET_USER_BY_CHAT_ID, String.class, chatId);
    }

    public void saveUserOption(String userOptionJson) {
        restTemplate.postForObject(DB_SERVICE + SAVE_USER_OPTION, userOptionJson, String.class);
    }

    public String getUserOptionByChatId(Long chatId) {
        return restTemplate.getForObject(DB_SERVICE + GET_USER_OPTION_BY_CHAT_ID, String.class, chatId);
    }

    public String getLessons(String groupJson, String day, String userVo) {
        return restTemplate.getForObject(
                PARSING_SERVICE + GET_LESSONS_WITH_PARAM, String.class, groupJson, saveDailyParameters(), day, userVo);
    }

    private String getDailyParameters() {
        return restTemplate.getForObject(PARSING_SERVICE + GET_DAILY_PARAMETERS, String.class);
    }

    private String saveDailyParameters() {
        return restTemplate.postForObject(DB_SERVICE + SAVE_DAILY_PARAMETERS, getDailyParameters(), String.class);
    }

    public String getStudyPlan(String semester, String userVo) {
        return restTemplate.getForObject(PARSING_SERVICE + GET_STUDY_PLAN, String.class, semester, userVo);
    }
}
