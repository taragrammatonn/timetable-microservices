package com.flux.parsingservice.parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.*;

import static java.util.Objects.isNull;

@Component
public class Parser {

    // LOGISTIC_SERVICE API's
    public static final String LOGISTIC_SERVICE = "http://LOGISTIC-SERVICE/logistic-api";
    public static final String GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL = "/getDailyParametersByWeekNotNull";

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/87.0.4280.88 Safari/537.36";

    public static final String ORIGIN_URL = "http://orar.usarb.md";

    private static final String GROUP_API = "http://orar.usarb.md/api/getGroups";
    private static final String TEACHERS_API = "http://orar.usarb.md/api/getteachers";
    private static final String AUDIENCE_API = "http://orar.usarb.md/api/getOffices";

    private static final String VALUE = "value";

    private static final String NO_DATA_FOR_TODAY = "[[]]";
    private static final String NO_DATA_FOR_TODAY_MESSAGE = "No data for today.";

    private static final List<String> CURRENT = Arrays.asList("day", "week", "semester");

    private final ObjectMapper objectMapper;
    private final RestTemplate restTemplate;

    public Parser(ObjectMapper objectMapper, RestTemplate restTemplate) {
        this.objectMapper = objectMapper;
        this.restTemplate = restTemplate;
    }

    public String getGroups() throws IOException {
        return getJsonContent(GROUP_API);
    }

    public String getTeachers() throws IOException {
        return getJsonContent(TEACHERS_API);
    }

    public String getAudiences() throws IOException {
        return getJsonContent(AUDIENCE_API);
    }

    public String getLessons(String groupJson, String dailyParameters, String day) throws IOException {

        Connection.Response res = getResponseContent();
        Document timeTableDom = res.parse();
        String csrf = timeTableDom.select("meta[name=\"csrf-token\"]").first().attr("content");

        JsonNode jsonNode = objectMapper.readTree(groupJson);
        Map<String, String> map = objectMapper.readValue(dailyParameters, new TypeReference<>(){});
        int dayNumber = Integer.parseInt(map.get("day")) + Integer.parseInt(day);

        if (isNull(map.get("week"))) {
            ArrayNode dailyParams = (ArrayNode) objectMapper.readTree(restTemplate.getForObject(LOGISTIC_SERVICE + GET_DAILY_PARAMETERS_BY_WEEK_NOT_NULL, String.class));
            map.put(
                    "week",
                    dailyParams.get("week").asText()
            );
        }

        if (Integer.parseInt(day) == 7) {
            String nextWeek = String.valueOf(Integer.parseInt(map.get("week")) + 1);
            map.replace("week", nextWeek);
            dayNumber = 1;
        }

        String response = Jsoup.connect(String.valueOf(LessonsBy.GROUP.getApi()))
                .method(Connection.Method.POST)
                .referrer("http://orar.usarb.md/")
                .userAgent(USER_AGENT)
                .referrer("http://orar.usarb.md/")
                .ignoreContentType(true)
                .cookies(res.cookies())
                .data("_csrf", csrf)
                .data("gr", jsonNode.get("id").toString().replace("\"", ""))
                .data("sem", map.get("semester").replace("\"", ""))
                .data("day", map.get("day").replace("\"", ""))
                .data("week", map.get("week").replace("\"", ""))
                .data("grName", jsonNode.get("name").toString().replace("\"", ""))
                .execute()
                .body();

        if (response.equals(NO_DATA_FOR_TODAY)) {
            return NO_DATA_FOR_TODAY_MESSAGE;
        }

        return parseLessons(response, dayNumber);
    }

    @SneakyThrows
    private String parseLessons(String weekLessons, int dayNumber) {
        ObjectMapper mapper = new ObjectMapper();
        ArrayNode arrayNode = (ArrayNode) mapper.readTree(weekLessons).get("week");
        StringBuilder todayLessons = new StringBuilder();
        String newLine = "\n--- ";
        String[] weekDay = getWeekDay(dayNumber);
        todayLessons.append(weekDay[0]).append(" ---> ").append(weekDay[1]).append("\n");
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

        if (todayLessons.length() < 40) {
            return todayLessons.append("На этот день нет пар! ... тау тау тау тау").toString();
        }

        return todayLessons.toString();
    }

    private String formatJson(String json) {
        return json.replace("Denumire", "name").replace("Id", "id");
    }

    private Connection.Response getResponseContent() throws IOException {
        return Jsoup.connect(ORIGIN_URL).userAgent(USER_AGENT).method(Connection.Method.GET).execute();
    }

    private String getJsonContent(String apiUrl) throws IOException {
        return formatJson(Jsoup.connect(apiUrl)
                .userAgent(USER_AGENT)
                .method(Connection.Method.POST)
                .ignoreContentType(true)
                .execute()
                .body());
    }

    @SneakyThrows
    public String getDailyParameters() {
        Connection.Response res = getResponseContent();
        Document timeTableDom = res.parse();
        Elements current = timeTableDom.select("option[selected=\"selected\"]");

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

    public String[] getWeekDay(int day) throws IOException {
        String[] weekDays = {"Luni", "Marți", "Miercuri", "Joi", "Vineri", "Sâmbătă", "Duminică"};
        String[] weekDay = new String[2];
        Document a = Jsoup.connect(String.valueOf(LessonsBy.GROUP.getApi())).get();
        String b = a.select("option[selected=\"selected\"]").get(1).html();
        String[] c = b.substring(b.indexOf("(") + 1, b.indexOf("-")).split("\\.");

        for (int i = 0; i < 7; i++) {
            if (day == i + 1) {
                weekDay[0] = Arrays.toString(c).replaceAll("\\[|]", "").replace(",", ".");
                weekDay[1] = weekDays[i];
            }
            c[0] = String.valueOf(Integer.parseInt(c[0]) + 1);
        }
        return weekDay;
    }
}