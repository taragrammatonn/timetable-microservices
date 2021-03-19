package com.flux.telegramservice.service.generator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flux.telegramservice.entity.UserOptionVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.generator.impl.*;
import com.flux.telegramservice.service.project.BotService;
import com.flux.telegramservice.service.project.UserService;
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

import java.util.Map;

import static com.flux.telegramservice.util.Links.GET_STUDY_PLAN;
import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@SpringBootTest
class CommandsGeneratorTest extends BotService {

    @Autowired private Environment env;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private GenericCallbackQueryCommandGenerator genericCallbackQueryCommandGenerator;

    @MockBean private UserService mockUserService;
    @MockBean private BotService mockBotService;
    @MockBean private RestTemplateService mockRestTemplateService;
    @MockBean private ObjectMapper mockObjectMapper;

    private Chat chat;
    private Message message;
    private Update update;
    private User user;
    private CallbackQuery callbackQuery;

    public CommandsGeneratorTest() {
        super(new ObjectMapper());
    }

    @BeforeEach
    void setUp() {
        this.chat = new Chat();
        this.message = new Message();
        this.update = new Update();
        this.user = new User();
        this.callbackQuery = new CallbackQuery();
    }

    @Test
    void startMessageGenerator() {
        setFieldsByReflection("/start");

        register("/start", new StartMessageGenerator(mockUserService));

        assertThat(update.getMessage().getText()).isNotNull().isEqualTo("/start");
        assertThat(commands.get(update.getMessage().getText())).isNotNull();

        when(mockUserService.completeUser(mockUserService.addNewUser(update))).thenReturn(env.getProperty("en.first_start_input"));
        String response = mockUserService.completeUser(mockUserService.addNewUser(update));

        assertThat(response).isNotNull();

        SendMessage message = commands.get(update.getMessage().getText()).generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("en.first_start_input"));
        assertThat(message.getReplyMarkup()).isNotNull();
    }

    @Test
    void groupMessageGenerator_When_UserVO_Group_Is_Null() {
        setFieldsByReflection("Group");

        register("Group", new GroupMessageGenerator(botService, mockRestTemplateService, env));

        assertThat(update.getMessage().getText()).isNotNull().isEqualTo("Group");
        assertThat(commands.get(update.getMessage().getText())).isNotNull();

        UserVO userVO = new UserVO();
        userVO.setUserLanguage("en");

        when(mockRestTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId()))
                .thenReturn(userVO);

        doNothing()
                .when(mockRestTemplateService)
                    .saveUserOption(new UserOptionVO().groupSelected(update.getMessage().getChatId()));

        SendMessage message = commands.get("Group").generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty(userVO.getUserLanguage() + ".get_group"));
    }

    @Test
    void groupMessageGenerator_Should_Throw_Exception() {
        setFieldsByReflection("Group");

        register("Group", new GroupMessageGenerator(botService, mockRestTemplateService, env));

        assertThrows(
                NullPointerException.class,
                () -> commands.get("Group").generateCommand(new Update())
        );
    }

    @Test
    void groupMessageGenerator_When_UserVO_Group_Is_Not_Null() {
        setFieldsByReflection("Group");

        register("Group", new GroupMessageGenerator(botService, mockRestTemplateService, env));

        assertThat(update.getMessage().getText()).isNotNull().isEqualTo("Group");
        assertThat(commands.get(update.getMessage().getText())).isNotNull();

        UserVO userVO = new UserVO();
        userVO.setUserGroup("test_response");

        when(mockRestTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, update.getMessage().getChatId()))
                .thenReturn(userVO);

        assertThat(userVO.getUserGroup()).isNotNull();

        when(botService.searchCommand(userVO.getUserGroup(), update)).thenReturn("test_response");

        SendMessage message = commands.get("Group").generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
        assertThat(message.getText()).isNotNull().isEqualTo("test_response");
    }

    @Test
    void getHelpMessageGenerator() {
        setFieldsByReflection("test");

       register("/help", new GetHelpMessageGenerator(env));

        SendMessage message = commands.get("/help").generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("ru.help"));
    }

    @Test
    void getStudyPlanCommandGenerator() {
        setFieldsByReflection("en");

        register("/studyPlan", new GetStudyPlanCommandGenerator(env));

        SendMessage message = commands.get("/studyPlan").generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
        assertThat(message.getText()).isNotNull().isEqualTo(env.getProperty("ru.choose_semester"));
    }

    @Test
    @SneakyThrows
    void genericCallbackQueryCommandGenerator_When_Command_Equals() {
        setFieldsByReflection("test");
        ReflectionTestUtils.setField(callbackQuery, "data", "tbSemI");

        UserVO userVO = new UserVO();
        userVO.setUserGroup("gr");

        when(mockRestTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);
        when(mockRestTemplateService.getForObject(String.class, GET_STUDY_PLAN, update.getCallbackQuery().getData(), mockObjectMapper.writeValueAsString(userVO))).thenReturn("test_answer_study_plan");

        SendMessage message = genericCallbackQueryCommandGenerator.generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getText()).isNotNull().isEqualTo("test_answer_study_plan");
        assertThat(message.getChatId()).isNotNull().isEqualTo("123");
    }

    @Test
    @SneakyThrows
    void genericCallbackQueryCommandGenerator_When_Command_Not_Equals() {
        setFieldsByReflection("test");
        ReflectionTestUtils.setField(callbackQuery, "data", "+1w");

        Map<String, String> commandsList = Map.ofEntries(Map.entry("+1w", "nextWeek"));
        ReflectionTestUtils.setField(genericCallbackQueryCommandGenerator, "commandsList", commandsList);

        UserVO userVO = new UserVO();
        userVO.setUserGroup("gr");

        when(mockRestTemplateService.getForObject(UserVO.class, GET_USER_BY_CHAT_ID, Long.valueOf(update.getCallbackQuery().getFrom().getId()))).thenReturn(userVO);
        when(mockBotService.getLessonsByGroup(update, userVO.getUserGroup(), commandsList.get("+1w"))).thenReturn("response");

        SendMessage message = genericCallbackQueryCommandGenerator.generateCommand(update);

        assertThat(message).isNotNull();
        assertThat(message.getText()).isNotNull().isEqualTo("response");
    }

    @Test
    void getPenisCommandGenerator() {
        setFieldsByReflection("test");

        register("/penis", new GetPenisCommandGenerator());

        SendMessage p = commands.get("/penis").generateCommand(update);

        assertThat(p).isNotNull();
        assertThat(p.getText()).isNotNull();
    }

    private void setFieldsByReflection(String inputCommand) {
        ReflectionTestUtils.setField(chat, "id", 123L);
        ReflectionTestUtils.setField(user, "languageCode", "ru");
        ReflectionTestUtils.setField(user, "id", 123);
        ReflectionTestUtils.setField(message, "from", user);
        ReflectionTestUtils.setField(callbackQuery, "data", "tbSemI");
        ReflectionTestUtils.setField(callbackQuery, "from", user);
        ReflectionTestUtils.setField(update, "callbackQuery", callbackQuery);
        ReflectionTestUtils.setField(message, "chat", chat);
        ReflectionTestUtils.setField(message, "text", inputCommand);
        ReflectionTestUtils.setField(update, "message", message);
    }
}
