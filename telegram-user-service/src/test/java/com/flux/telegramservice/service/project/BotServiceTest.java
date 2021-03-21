package com.flux.telegramservice.service.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.impl.GetHelpMessageGenerator;
import com.flux.telegramservice.service.request.RestTemplateService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;

import java.util.Objects;

import static com.flux.telegramservice.util.Links.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class BotServiceTest {

    @MockBean private RestTemplateService restTemplateService;

    @Autowired private BotService botService;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private Environment env;

    private Message message;
    private Update update;
    private Chat chat;
    private CallbackQuery callbackQuery;
    private User user;
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
                "mi41z",
                "language",
                true, false, false
        );

        groupVO = new GroupVO("228", "MI31Z");
    }

    @Test
    void getLessonsByGroup_Should_Return_no_group_Message() {
        setFieldsByReflection();

        ReflectionTestUtils.setField(update, "message", null);
        assertThat(update.getMessage()).isNull();

        String res = botService.getLessonsByGroup(update, "MI31Z", "1");

        assertThat(res).isNotNull();
        assertThat(res).isEqualTo(env.getProperty("ru.no_response"));
    }

    @Test
    @SneakyThrows
    void getLessonsByGroup_Should_Return_Lessons() {
        setFieldsByReflection();
        when(restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);
        UserVO userVOMock = restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()));

        assertThat(userVOMock).isNotNull();
        assertThat(userVOMock.getChatId()).isNotNull().isEqualTo(123L);

        when(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z")).thenReturn(groupVO);
        GroupVO mockGroupVO = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z");

        assertThat(mockGroupVO).isNotNull();
        assertThat(mockGroupVO.getGroupId()).isEqualTo("228").isNotNull();

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
    void getLessonsByGroup_Should_Throw_Exception() {
        assertThrows(
                NullPointerException.class,
                () -> botService.getLessonsByGroup(new Update(), "test", "+1")
        );
    }

    @Test
    void messageProcessing_When_Command_Is_Null() {
        setFieldsByReflection();
        ReflectionTestUtils.setField(message, "text", "/help");
        botService.register("/help", new GetHelpMessageGenerator(env));

        SendMessage message = botService.messageProcessing(update);

        assertThat(botService.commands.get(update.getMessage().getText())).isNotNull();
        assertThat(message).isNotNull();
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("ru.help"));
    }

    @Test
    void messageProcessing_When_Command_Is_Not_Null() {
        setFieldsByReflection();
        ReflectionTestUtils.setField(message, "text", "another_command");
        botService.register("/help", new GetHelpMessageGenerator(env));

        SendMessage message = botService.messageProcessing(update);

        assertThat(botService.commands.get(update.getMessage().getText())).isNull();
        assertThat(message).isNotNull();
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("ru.choose_option"));
    }

    @Test
    void messageProcessing_Should_Throw_Exception() {
        assertThrows(
                NullPointerException.class,
                () -> botService.messageProcessing(new Update())
        );
    }

    @Test
    void callBackQueryProcessing() {
        setFieldsByReflection();
        ReflectionTestUtils.setField(callbackQuery, "data", "+1w");

        when(restTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);

        SendMessage message = botService.callBackQueryProcessing(update);

        assertThat(message).isNotNull();
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("ru.no_response"));
    }

    private void setFieldsByReflection() {
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