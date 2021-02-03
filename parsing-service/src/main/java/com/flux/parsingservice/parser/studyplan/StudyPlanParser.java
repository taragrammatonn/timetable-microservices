package com.flux.parsingservice.parser.studyplan;

import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

import static com.flux.parsingservice.parser.lessons.LessonsParser.USER_AGENT;

@Service
public class StudyPlanParser {

    public static final String ORIGIN_URL = "http://planstudii.usarb.md/";


    @SneakyThrows
    public String[] getRequestParameters() {
        Connection.Response res = getResponseContent();
        Document document = res.parse();

        Elements elements = document.select("option[selected]");

        System.out.println(1);

        return new String[] {
                document.select("input[id=\"__VIEWSTATE\"]").first().attr("value"),
                document.select("input[id=\"__VIEWSTATEGENERATOR\"]").first().attr("value"),
                document.select("input[id=\"__EVENTVALIDATION\"]").first().attr("value")
        };
    }

    public Connection.Response getResponseContent() throws IOException {
        return Jsoup.connect("http://planstudii.usarb.md/?sp=32&ciclul=1&fbclid=IwAR3KTtscm0i30wHHa3H3LViZ3mVbttrd_8BJqw5CaUfEVDYkqJagjcPKamM").userAgent(USER_AGENT).method(Connection.Method.GET).execute();
    }

    public static void main(String[] args) {
        StudyPlanParser studyPlanParser = new StudyPlanParser();
        studyPlanParser.getRequestParameters();
    }
}

