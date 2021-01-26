package com.flux.telegramservice.service.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.flux.telegramservice.entity.HistoryEvent;
import com.flux.telegramservice.entity.HistoryVO;
import com.flux.telegramservice.entity.UserVO;
import lombok.SneakyThrows;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.flux.telegramservice.util.Links.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.BDDMockito.given;

class RestTemplateServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    private RestTemplateService restTemplateService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.restTemplateService = new RestTemplateService(restTemplate);
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void saveUser() {
        given(restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_USER, new UserVO(123L, "fName", "lName"), UserVO.class)).willReturn(
                new UserVO(123L, "fName", "lName"));

        UserVO userVO = restTemplateService.saveUser(new UserVO(123L, "fName", "lName"));

        assertThat(userVO).isNotNull();
        assertThat(userVO.getChatId()).isEqualTo(123L);
        assertThat(userVO.getFName()).isEqualTo("fName");
        assertThat(userVO.getLName()).isEqualTo("lName");
    }

    @Test
    void getUserByChatId() {
        given(restTemplate.getForObject(LOGISTIC_SERVICE + GET_USER_BY_CHAT_ID, UserVO.class, 123L)).willReturn(
                new UserVO(123L, "fName", "lName"));

        UserVO userVO = restTemplateService.getUserByChatId(123L);
        assertThat(userVO).isNotNull();
        assertThat(userVO.getChatId()).isEqualTo(123L);
    }

    @Test
    void findGroup_Should_Return_Group() {
        given(restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, "group")).willReturn("group");

        String group = restTemplateService.findGroup("group");
        assertThat(group)
                .isNotNull()
                .isEqualTo("group");
    }

    @Test
    void findGroup_Should_Return_Null() {
        given(restTemplate.getForObject(LOGISTIC_SERVICE + FIND_GROUP, String.class, "group")).willReturn(null);

        String group = restTemplateService.findGroup("group");
        assertThat(group).isNull();
    }

    @Test
    @SneakyThrows
    void getLessonsByGroup_Should_Return_Lessons() {
        given(restTemplate.getForObject(LOGISTIC_SERVICE + LESSON_BY_GROUP, String.class, "group")).willReturn(
                "{ \"week\": [ { \"cours_nr\": 3, \"cours_name\": \"Stresul în mediul educațional\", \"cours_office\": \"videoconferință\", \"teacher_name\": \"Cazacu D. \", \"cours_type\": \"Consultație\", \"Titlu\": \"lect. univ., dr.\", \"Color\": \"-32640\", \"day_number\": 1, \"Denumire\": \"MI31Z\", \"Subgrupa\": \"Subgrupa 1\", \"week\": 21 }, { \"cours_nr\": 4, \"cours_name\": \"Psihologia vârstelor\", \"cours_office\": \"videoconferință\", \"teacher_name\": \"S. Briceag\", \"cours_type\": \"Consultație\", \"Titlu\": \"conf. univ., dr.\", \"Color\": \"-8323073\", \"day_number\": 1, \"Denumire\": \"MI31Z\", \"Subgrupa\": \"Subgrupa 2\", \"week\": 21 }, { \"cours_nr\": 2, \"cours_name\": \"Psihologia vârstelor\", \"cours_office\": null, \"teacher_name\": \"S. Briceag\", \"cours_type\": \"Examinare\", \"Titlu\": \"conf. univ., dr.\", \"Color\": \"-8323073\", \"day_number\": 2, \"Denumire\": \"MI31Z\", \"Subgrupa\": \"Subgrupa 2\", \"week\": 21 }, { \"cours_nr\": 2, \"cours_name\": \"Stresul în mediul educațional\", \"cours_office\": \"513\", \"teacher_name\": \"Cazacu D. \", \"cours_type\": \"Examinare\", \"Titlu\": \"lect. univ., dr.\", \"Color\": \"-32640\", \"day_number\": 2, \"Denumire\": \"MI31Z\", \"Subgrupa\": \"Subgrupa 1\", \"week\": 21 } ] }");

        String lessonsJson = restTemplateService.getLessonsByGroup("group");

        assertThat(objectMapper.readTree(lessonsJson).get("week")).isNotNull();
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(lessonsJson).get("week");
        assertThat(arrayNode.isArray()).isTrue();

        JSONObject data = new JSONObject(lessonsJson);

        assertThat(data.has("week")).isTrue();
        Object week = data.get("week");

        assertThat(week).isInstanceOf(JSONArray.class);
        JSONArray weekData = (JSONArray) week;

        assertThat(weekData.length()).isEqualTo(4);
        assertThat(weekData.getJSONObject(0)).isInstanceOf(JSONObject.class);

        for (int i = 0; i < weekData.length(); i++) {
            JSONObject jsonObject = weekData.getJSONObject(i);
            assertThat(jsonObject.has("cours_nr")).isTrue();
            assertThat(jsonObject.has("cours_name")).isTrue();
            assertThat(jsonObject.has("cours_office")).isTrue();
            assertThat(jsonObject.has("teacher_name")).isTrue();
            assertThat(jsonObject.has("cours_type")).isTrue();
            assertThat(jsonObject.has("Titlu")).isTrue();
            assertThat(jsonObject.has("day_number")).isTrue();
            assertThat(jsonObject.has("Denumire")).isTrue();
            assertThat(jsonObject.has("Subgrupa")).isTrue();
            assertThat(jsonObject.has("week")).isTrue();
        }
    }

    @Test
    void saveHistory_Should_Return_Non_Null_History() {
        given(restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_HISTORY,
                new HistoryVO(
                        HistoryEvent.TEST,
                        "message",
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                        123L),
                HistoryVO.class
        )).willReturn(new HistoryVO(
                HistoryEvent.TEST,
                "message",
                new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                123L));

        assertThat(restTemplateService.saveHistory(new Update(), HistoryEvent.TEST)).isNotNull();
    }

    @Test
    void saveHistory_Should_Return_Null() {
        given(restTemplate.postForObject(LOGISTIC_SERVICE + SAVE_HISTORY,
                new HistoryVO(
                        HistoryEvent.TEST,
                        "message",
                        new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(new Date()),
                        123L),
                HistoryVO.class
        )).willReturn(null);

        assertThat(restTemplateService.saveHistory(new Update(), HistoryEvent.TEST)).isNotNull();
    }
}