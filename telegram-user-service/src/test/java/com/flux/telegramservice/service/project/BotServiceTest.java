package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.CommandGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Objects;

import static com.flux.telegramservice.util.Links.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class BotServiceTest {

    @MockBean private RestTemplateService restTemplateService;

    @Autowired private BotService botService;
    @Autowired ObjectMapper objectMapper;

    public BotServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private Chat chat;
    private Message message;
    private User user;
    private CallbackQuery callbackQuery;
    private Update update;
    private UserVO userVO;
    private GroupVO groupVO;


    @BeforeEach
    void setUp() {
        this.chat = new Chat();
        this.message = new Message();
        this.update = new Update();
        this.user = new User();
        this.callbackQuery = new CallbackQuery();

        this.userVO = new UserVO(
                123L,
                "fName",
                "lName",
                "nickName",
                null,
                "language",
                true, false, false
        );

        groupVO = new GroupVO("228", "MI31Z");
    }

    @Test
    void getLessonsByGroup_Update_Get_Message_Is_Not_Null() {
        ReflectionTestUtils.setField(chat, "id", 123L);
        ReflectionTestUtils.setField(message, "chat", chat);
        ReflectionTestUtils.setField(update, "message", message);

        assertThat(chat).isNotNull();
        assertThat(message).isNotNull();
        assertThat(update).isNotNull();

        doReturn(userVO).when(restTemplateService).getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                update.getMessage().getChatId());

        UserVO userVOMock = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId());

        assertThat(userVOMock).isNotNull();
        assertThat(userVOMock.getChatId()).isNotNull().isEqualTo(123L);
    }

    @Test
    @SneakyThrows
    void getLessonsByGroup_Update_Get_Message_Is_Null() {
        ReflectionTestUtils.setField(user, "id", 123);
        ReflectionTestUtils.setField(callbackQuery, "from", user);
        ReflectionTestUtils.setField(update, "callbackQuery", callbackQuery);

        assertThat(user).isNotNull();
        assertThat(callbackQuery).isNotNull();
        assertThat(update).isNotNull();
        assertThat(update.getMessage()).isNull();

        when(restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);

        UserVO userVOMock = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                Long.valueOf(update.getCallbackQuery().getFrom().getId()));

        assertThat(userVOMock).isNotNull();
        assertThat(userVOMock.getChatId()).isNotNull().isEqualTo(123L);

        doReturn(userVO).when(restTemplateService).getForObject(UserVO.class, GET_USER_BY_CHAT_ID,
                update.getCallbackQuery().getFrom().getId());

        userVOMock = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getCallbackQuery().getFrom().getId());

        assertThat(userVOMock).isNotNull();
        assertThat(userVOMock.getChatId()).isNotNull().isEqualTo(123L);

        assertThat(user).isNotNull();
        assertThat(userVO.getChatId()).isNotNull().isEqualTo(123L);
    }

    @Test
    void getGroupVo_With_Not_Null_Message() {
        when(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z")).thenReturn(groupVO);
        GroupVO mockGroupVO = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z");

        assertThat(mockGroupVO).isNotNull();
        assertThat(mockGroupVO.getName()).isEqualTo("MI31Z");
    }

    @Test
    void getGroupVo_With_Null_Message() {
        when(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z")).thenReturn(groupVO);
        GroupVO mockGroupVO = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z");

        assertThat(mockGroupVO).isNotNull();
        assertThat(mockGroupVO.getName()).isEqualTo("MI31Z");
    }

    @Test
    void getLessonsByGroup_Should_Return_no_group_Message() {
        initFields();

        ReflectionTestUtils.setField(update, "message", null);
        assertThat(update.getMessage()).isNull();

        String res = botService.getLessonsByGroup(update, "MI31Z", "1");

        assertThat(res).isNotNull();
        assertThat(res).isEqualTo("Такой группы не существует.");
    }

    @Test
    @SneakyThrows
    void getLessonsByGroup_Should_Return_Lessons() {
        initFields();
        when(restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);
        UserVO userVOMock = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()));

        assertThat(userVOMock).isNotNull();
        assertThat(userVOMock.getChatId()).isNotNull().isEqualTo(123L);

        when(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z")).thenReturn(groupVO);
        GroupVO mockGroupVO = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z");

        assertThat(mockGroupVO).isNotNull();
        assertThat(mockGroupVO.getGroupId())
                .isEqualTo("228")
                .isNotNull();

        var s = new String(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("lessons_answer.txt")).readAllBytes());

        when(restTemplateService.getForObject(
                String.class, GET_LESSONS_WITH_PARAM,
                objectMapper.writeValueAsString(mockGroupVO),
                objectMapper.writeValueAsString(userVOMock), "1")
        ).thenReturn(s);

        String res = botService.getLessonsByGroup(update, "MI31Z", "1");
        assertThat(res).isNotNull().isEqualTo(s);

        ReflectionTestUtils.setField(update, "message", null);
        assertThat(update.getMessage()).isNull();

        res = botService.getLessonsByGroup(update, "MI31Z", "1");
        assertThat(res).isNotNull().isEqualTo(s);
    }

    @Test
    void messageProcessing() {
    }

    @Test
    void callBackQueryProcessing() {
    }

    @Test
    void register() {
    }

    private void initFields() {
        ReflectionTestUtils.setField(chat, "id", 123L);
        ReflectionTestUtils.setField(user, "languageCode", "ru");
        ReflectionTestUtils.setField(message, "from", user);
        ReflectionTestUtils.setField(message, "chat", chat);
        ReflectionTestUtils.setField(update, "message", message);

        ReflectionTestUtils.setField(user, "id", 123);
        ReflectionTestUtils.setField(user, "languageCode", "ru");
        ReflectionTestUtils.setField(callbackQuery, "from", user);
        ReflectionTestUtils.setField(update, "callbackQuery", callbackQuery);
    }
}