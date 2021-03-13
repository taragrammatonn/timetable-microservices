package com.flux.telegramservice.service.project;

import com.flux.telegramservice.service.request.RestTemplateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.telegram.telegrambots.meta.api.objects.Update;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class BotServiceTest {

    @Mock
    private RestTemplateService restTemplateService;

    @Mock
    private BotService botService;

    @Mock
    private Update update;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        this.botService = mock(BotService.class);
        this.restTemplateService = mock(RestTemplateService.class);
        this.update = mock(Update.class);
    }

    @Test
    void findLessonsByGroup_Should_Return_Non_Null_Group_JSON() {
        given(restTemplateService.findGroup("im21r")).willReturn("{\"id\":700,\"name\":\"IM21R\"}");

        String groupJson = restTemplateService.findGroup("im21r");

        assertThat(groupJson).isNotNull().contains("im21r");
    }

    @Test
    void findLessonsByGroup_Should_Return_Null_Group_JSON() {
        given(restTemplateService.findGroup("im21r")).willReturn(null);

        String groupJson = botService.findLessonsByGroup(new Update());

        assertThat(groupJson).isNotNull().isEqualTo("Такой группы не существует!");
    }
}