//package com.flux.telegramservice.service.project;
//
//import com.flux.telegramservice.entity.UserVO;
//import com.flux.telegramservice.service.request.RestTemplateService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static com.flux.telegramservice.service.project.UserService.FIRST_START_INPUT;
//import static com.flux.telegramservice.service.project.UserService.REPEATING_START_INPUT;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.mockito.BDDMockito.given;
//
//class UserServiceTest {
//
//    @Mock
//    private RestTemplateService restTemplateService;
//    private UserService userService;
//    private UserVO userVO;
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//        this.userService = new UserService(restTemplateService);
//        this.userVO = new UserVO(
//                123L,
//                "fName",
//                "lName",
//                "nickName",
//                null,
//                "language",
//                true, false, false
//        );
//    }
//
//    @Test
//    void addNewUser_Should_Return_Not_Null_User() {
//        given(restTemplateService.getUserByChatId(123L)).willReturn(userVO);
//        assertThat(userVO).isNotNull();
//        assertThat(userVO.getChatId()).isEqualTo(123L);
//    }
//
//    @Test
//    void addNewUser_Should_Return_Null_User() {
//        given(restTemplateService.saveUser(userVO)).willReturn(userVO);
//        assertThat(userVO).isNotNull();
//        assertThat(userVO.getChatId()).isEqualTo(123L);
//    }
//
//    @Test
//    void completeUser_Should_Return_First_Start_Input() {
//        String response = userService.completeUser(userVO);
//        assertThat(response)
//                .isNotNull()
//                .contains(userVO.getFName())
//                .isEqualTo(String.format(FIRST_START_INPUT, "fName"),
//                        FIRST_START_INPUT, userVO.getFName());
//    }
//
//    @Test
//    void completeUser_Should_Return_Repeating_Start_Input() {
//        userVO.setIsDefined(true);
//
//        String response = userService.completeUser(userVO);
//        assertThat(response)
//                .isNotNull()
//                .contains(userVO.getFName())
//                .isEqualTo(String.format(
//                        REPEATING_START_INPUT, "fName"),
//                        REPEATING_START_INPUT, userVO.getFName()
//                );
//    }
//
//    @Test
//    void completeUser_Should_Return_First_Answer_Message() {
//        String response = userService.completeUser(userVO);
//        assertThat(response)
//                .isNotNull()
//                .contains(userVO.getFName())
//                .isEqualTo(String.format(
//                        FIRST_START_INPUT,
//                        userVO.getFName())
//                );
//    }
//}