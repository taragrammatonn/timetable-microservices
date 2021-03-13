package com.flux.parsingservice.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static java.util.Objects.isNull;

@Component
@Slf4j
public class LessonsParser {

    private Document document;

    // LOGISTIC_SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    public static final String GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL = "/getDailyParametersByWeekNotNull";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";
    public static final String ORIGIN_URL = "http://orar.usarb.md";

    private static final String VALUE = "value";
    private static final String NO_DATA_FOR_TODAY = "[[]]";
    private static final String NO_DATA_FOR_TODAY_MESSAGE = "No data for today.";

    private static final List<String> CURRENT = Arrays.asList("day", "week", "semester");

    private final Environment env;
    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public LessonsParser(Environment env, ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.env = env;
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String getLessons(String groupJson, String dailyParameters, String userVo, String day) throws IOException {
        Connection.Response res = getResponseContent();
        this.document = res.parse();
        String csrf = this.document.select("meta[name=\"csrf-token\"]").first().attr("content");

        JsonNode jsonNode = objectMapper.readTree(groupJson);
        Map<String, String> map = objectMapper.readValue(dailyParameters, new TypeReference<>() {});

        if (isNull(map.get("week"))) {
            ObjectNode dailyParams = (ObjectNode) objectMapper.readTree(restTemplate.getForObject(LOGISTIC_SERVICE + GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL, String.class));
            map.put("week", dailyParams.get("week").asText());
        }

        // +1 because somebody once told me that the day on site is current_day + 1 in JS, lmao
        int dayNumber = Integer.parseInt(map.get("day")) + (!day.isEmpty() ? Integer.parseInt(day) : 0) + 1;
        if (day.equals("nextWeek")) {
            map.replace("week", String.valueOf(Integer.parseInt(map.get("week")) + 1));
            map.put("day", "1");
            dayNumber = 1;
        } else map.put("day", String.valueOf(dayNumber));

        String response = null;
        try {
            response = Jsoup.connect(String.valueOf(LessonsBy.GROUP.getApi()))
                    .method(Connection.Method.POST)
                    .userAgent(USER_AGENT)
                    .referrer(ORIGIN_URL)
                    .ignoreContentType(true)
                    .cookies(res.cookies())
                    .data("_csrf", csrf)
                    .data("gr", jsonNode.get("groupId").toString().replace("\"", ""))
                    .data("sem", map.get("semester").replace("\"", ""))
                    .data("day", map.get("day").replace("\"", ""))
                    .data("week", map.get("week").replace("\"", ""))
                    .data("grName", jsonNode.get("name").toString().replace("\"", ""))
                    .execute()
                    .body();

            if (response.equals(NO_DATA_FOR_TODAY)) return NO_DATA_FOR_TODAY_MESSAGE;
        } catch (HttpStatusException e) {
            log.error("Bad request parameters!", e);
        }

        return objectMapper.writeValueAsString(parseLessons(response, dayNumber, getWeekDay(dayNumber, map, userVo)));
    }

    @SneakyThrows
    private String parseLessons(String weekLessons, int dayNumber, String weekDay) {
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(weekLessons).get("week");
        StringBuilder todayLessons = new StringBuilder();
        String newLine = "\n--- ";
        todayLessons.append(weekDay);
        Map<Integer, String> courseNr = Map.of(
                1, "\n1. 8:00-9:30\n- ",
                2, "\n2. 9:45-11:15\n- ",
                3, "\n3. 11:30-13:00\n- ",
                4, "\n4. 13:15-14:45\n- ",
                5, "\n5. 15:00-16:30\n- ",
                6, "\n6. 16:45-18:15\n- ",
                7, "\n7. 18:30-20:00\n- "
        );
        if (arrayNode.isArray()) {
            for (JsonNode jsonNode : arrayNode) {
                if (dayNumber == (jsonNode.get("day_number").asInt())) {
                    todayLessons.append(courseNr.get(jsonNode.get("cours_nr").asInt())).append(jsonNode.get("cours_name").asText()).append(newLine);
                    todayLessons.append(jsonNode.get("cours_type").asText()).append(newLine);
                    todayLessons.append(jsonNode.get("Titlu").asText()).append(" ").append(jsonNode.get("teacher_name").asText()).append(newLine);
                    todayLessons.append(jsonNode.get("cours_office").asText()).append("\n");
                }
            }
        }

        return todayLessons.toString();
    }

    private Connection.Response getResponseContent() throws IOException {
        return Jsoup.connect(ORIGIN_URL).userAgent(USER_AGENT).method(Connection.Method.GET).execute();
    }

    @SneakyThrows
    public String getDailyParameters() {
        Elements current = getResponseContent().parse().select("option[selected=\"selected\"]");

        Map<String, String> weekData = new HashMap<>();
        if (current.size() < 3) {
            weekData.put(CURRENT.get(0), current.get(0).attr(VALUE));
            weekData.put(CURRENT.get(2), current.get(1).attr(VALUE));
        } else {
            weekData.put(CURRENT.get(0), current.get(0).attr(VALUE));
            weekData.put(CURRENT.get(1), current.get(1).attr(VALUE));
            weekData.put(CURRENT.get(2), current.get(2).attr(VALUE));
        }

        return objectMapper.writeValueAsString(weekData);
    }

    @SneakyThrows
    public String getWeekDay(int day, Map<String, String> map, String userVo) {
        String lang = objectMapper.readTree(userVo).get("userLanguage").asText();
        Map<Integer, String> daysOfWeek = Map.of(
                1, Objects.requireNonNull(env.getProperty(lang + ".weekDay.monday")),
                2, Objects.requireNonNull(env.getProperty(lang + ".weekDay.tuesday")),
                3, Objects.requireNonNull(env.getProperty(lang + ".weekDay.wednesday")),
                4, Objects.requireNonNull(env.getProperty(lang + ".weekDay.thursday")),
                5, Objects.requireNonNull(env.getProperty(lang + ".weekDay.friday")),
                6, Objects.requireNonNull(env.getProperty(lang + ".weekDay.saturday")),
                7, Objects.requireNonNull(env.getProperty(lang + ".weekDay.sunday"))
        );

        String[] data = this.document
                .getElementById("weekSelector")
                .select("option[value=" + map.get("week") + "]").text()
                .substring(3, 12).split("\\.");

        return (Integer.parseInt(data[0]) + day - 1) + "." + data[1] + "." + data[2] + " --- " + daysOfWeek.get(day) + "\n";
    }
}