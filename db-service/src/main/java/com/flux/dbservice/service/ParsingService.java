package com.flux.dbservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.dbservice.entity.parsing.DailyParameters;
import com.flux.dbservice.entity.parsing.Group;
import com.flux.dbservice.repository.parsing.DailyParametersRepository;
import com.flux.dbservice.repository.parsing.GroupRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.NonUniqueResultException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.util.Objects.isNull;

@Service
@Slf4j
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

    private final ObjectMapper objectMapper;

    public ParsingService(
            DailyParametersRepository dailyParametersRepository,
            GroupRepository groupRepository,
            RestTemplate restTemplate,
            ObjectMapper objectMapper

    ) {
        this.dailyParametersRepository = dailyParametersRepository;
        this.groupRepository = groupRepository;
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String checkConnection() {
        return restTemplate.getForObject(PARSING_SERVICE + "/api", String.class);
    }

    @SneakyThrows
    public String findLessonsByGroup(String groupName) {
        try {
            return objectMapper.writeValueAsString(groupRepository.getByName(groupName.toUpperCase()));
        } catch (IncorrectResultSizeDataAccessException ex) {
            if (ex.getCause() instanceof NonUniqueResultException && Boolean.TRUE.equals(refreshGroupsStatus(groupName))) {
                return objectMapper.writeValueAsString(groupRepository.getByName(groupName.toUpperCase()));
            }
            log.error("Error in ParsingService.findLessonsByGroup. Cannot get group from Data Base.");
        }

        return NULL;
    }

    @SneakyThrows
    public Boolean refreshGroupsStatus(String groupName) {
        log.info("Refreshing groups.");
        groupRepository.deleteAll();

        List<Group> groups = Arrays.asList(objectMapper.readValue(
                restTemplate.getForObject(
                        LOGISTIC_SERVICE + GET_ALL_GROUPS, String.class),
                Group[].class));

        if (containsName(groups, groupName)) {
            groupRepository.saveAll(groups);
            return true;
        }

        log.error("Error in ParsingService.findLessonsByGroup. Cannot find group.");
         return false;
    }

    @SneakyThrows
    public String saveDailyParameters(String parametersJson) {
        var dailyParameters = dailyParametersRepository.getByParametersDate(LocalDate.now());
        if (isNull(dailyParameters)) {
            if (dailyParametersRepository.count() >= 1) {
                dailyParametersRepository.deleteAll();
            }
            return objectMapper.writeValueAsString(dailyParametersRepository.save(objectMapper.readValue(parametersJson, DailyParameters.class)));
        } else return objectMapper.writeValueAsString(dailyParameters);
    }

    @SneakyThrows
    public String getDailyParametersByWeekNotNull() {
        DailyParameters dailyParameters = dailyParametersRepository.findByWeekNotNull();
        dailyParameters.setWeek(String.valueOf(Integer.parseInt(dailyParameters.getWeek()) + 1));
        return objectMapper.writeValueAsString(dailyParametersRepository.findByWeekNotNull());
    }

    private boolean containsName(final List<Group> list, final String name) {
        return list.stream().anyMatch(o -> o.getName().equalsIgnoreCase(name));
    }
}

