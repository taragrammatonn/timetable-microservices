package com.flux.telegramservice.service.project;

import com.flux.telegramservice.entity.GroupVO;
import com.flux.telegramservice.entity.UserVO;
import com.flux.telegramservice.service.request.RestTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.util.ReflectionTestUtils;
import org.telegram.telegrambots.meta.api.objects.*;

import static com.flux.telegramservice.util.Links.FIND_GROUP;
import static com.flux.telegramservice.util.Links.GET_USER_BY_CHAT_ID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
class BotServiceTest {

    @MockBean private RestTemplateService restTemplateService;

    @Autowired private BotService botService;

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
    void getLessonsByGroup_Update_Get_Message_Null() {
        ReflectionTestUtils.setField(user, "id", 123);
        ReflectionTestUtils.setField(callbackQuery, "from", user);
        ReflectionTestUtils.setField(update, "callbackQuery", callbackQuery);

        assertThat(user).isNotNull();
        assertThat(callbackQuery).isNotNull();
        assertThat(update).isNotNull();

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
    void getGroupVo() {
        when(restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z")).thenReturn(groupVO);
        GroupVO mockGroupVO = restTemplateService.getForObject(GroupVO.class, FIND_GROUP, "MI31Z");

        assertThat(mockGroupVO).isNotNull();
        assertThat(mockGroupVO.getName()).isEqualTo("MI31Z");
    }

    @Test
    void getLessonsByGroup_Should_Return_no_group_Message() {
        initFields();

        String res = botService.getLessonsByGroup(update, "MI31Z", "1");

        assertThat(res).isNotNull();
        assertThat(res).isEqualTo("Такой группы не существует.");
    }

    @Test
    void getLessonsByGroup_Should_Return_Lessons() {
        // TODO complete getLessonsByGroup method
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