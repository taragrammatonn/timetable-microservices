package com.flux.dbservice.service;

import com.flux.dbservice.entity.parsing.lessons.DailyParameters;
import com.flux.dbservice.entity.parsing.lessons.Group;
import com.flux.dbservice.repository.parsing.DailyParametersRepository;
import com.flux.dbservice.repository.parsing.GroupRepository;
import com.google.gson.Gson;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Objects.isNull;

@Service
public class ParsingService {

    private static final String PARSING_SERVICE = "http://PARSING-SERVICE/lessons/api";

    // LOGISTIC_SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    public static final String GET_ALL_GROUPS = "/getAllGroups";

    // Util
    public static final String NULL = "null";

    private final DailyParametersRepository dailyParametersRepository;

    private final GroupRepository groupRepository;

    private final RestTemplate restTemplate;

    private final Gson gson;

    public ParsingService(
            DailyParametersRepository dailyParametersRepository,
            GroupRepository groupRepository,
            RestTemplate restTemplate,
            Gson gson
    ) {
        this.dailyParametersRepository = dailyParametersRepository;
        this.groupRepository = groupRepository;
        this.restTemplate = restTemplate;
        this.gson = gson;
    }

    public String checkConnection() {
        return restTemplate.getForObject(PARSING_SERVICE + "/api", String.class);
    }

    public String findLessonsByGroup(String groupName) {
        String groupJson = gson.toJson(groupRepository.findByName(groupName.toUpperCase()));
        if (groupJson.equals(NULL)) {
            List<Group> groups = Arrays.asList(gson.fromJson(
                    restTemplate.getForObject(
                            LOGISTIC_SERVICE + GET_ALL_GROUPS, String.class),
                    Group[].class)
            );

            if (containsName(groups, groupName)) {
                groupRepository.saveAll(groups);
            } else return "null";
            return gson.toJson(groupRepository.findByName(groupName.toUpperCase()));
        }

        return groupJson;
    }

    public void refreshGroups(String groupsJson) {
        groupRepository.saveAll(Arrays.asList(gson.fromJson(groupsJson, Group[].class)));
    }

    @SneakyThrows
    public String saveDailyParameters(String parametersJson) {
        var dailyParameters = dailyParametersRepository.getByParametersDate(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        if (isNull(dailyParameters)) {
            if (dailyParametersRepository.count() > 2) {
                dailyParametersRepository.deleteAll();
            }
            return gson.toJson(dailyParametersRepository.save(gson.fromJson(parametersJson, DailyParameters.class)));
        } else return gson.toJson(dailyParameters);
    }

    public String getDailyParametersByWeekNotNull() {
        DailyParameters dailyParameters = dailyParametersRepository.findByWeekNotNull();
        dailyParameters.setWeek(String.valueOf(Integer.parseInt(dailyParameters.getWeek()) + 1));
        return gson.toJson(dailyParametersRepository.findByWeekNotNull());
    }

    private boolean containsName(final List<Group> list, final String name){
        return list.stream().anyMatch(o -> o.getName().equalsIgnoreCase(name));
    }
}

