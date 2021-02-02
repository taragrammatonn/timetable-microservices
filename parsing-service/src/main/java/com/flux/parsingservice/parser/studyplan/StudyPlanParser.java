package com.flux.parsingservice.parser.studyplan;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.flux.parsingservice.parser.lessons.LessonsParser.USER_AGENT;

@Service
public class StudyPlanParser {

    public static final String ORIGIN_URL = "http://planstudii.usarb.md/";


    @SneakyThrows
    public void getContent() {
        Connection.Response res = getResponseContent();
        Document document = res.parse();


    }

    public Connection.Response getResponseContent() throws IOException {
        return Jsoup.connect(ORIGIN_URL).userAgent(USER_AGENT).method(Connection.Method.GET).execute();
    }

    public static void main(String[] args) {
        StudyPlanParser studyPlanParser = new StudyPlanParser();
        studyPlanParser.getContent();
    }
}

