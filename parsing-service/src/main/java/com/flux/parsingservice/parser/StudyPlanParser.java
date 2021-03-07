package com.flux.parsingservice.parser;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import lombok.SneakyThrows;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StudyPlanParser {

    @Autowired
    private ObjectMapper objectMapper;

    private final String[] PARAMS = {"__EVENTTARGET", "__EVENTARGUMENT", "__LASTFOCUS", "__VIEWSTATE", "__VIEWSTATEGENERATOR", "__EVENTVALIDATION"};
    private final String STUDY_PLAN_URL = "http://planstudii.usarb.md";

    private String responseBody;
    private final OkHttpClient httpClient = new OkHttpClient();

    public void getRequest() {
        String AGENT_URL = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setUserAgent(AGENT_URL)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();
        HttpGet httpGet = new HttpGet(STUDY_PLAN_URL);
        try (CloseableHttpResponse response1 = httpClient.execute(httpGet)) {
            HttpEntity entity1 = response1.getEntity();
            responseBody = EntityUtils.toString(entity1);
            EntityUtils.consume(entity1);
        } catch (UndeclaredThrowableException | IOException ignored) {
            System.out.println("Unexpected code");
        }
    }

    public String generateWebRequest(String group, String semester) {
        List<String> dd, ddValue;
        switch (group.toUpperCase()) {
            case "ME13M" -> {
                dd = List.of("ddCiclu", "ddDomeniul", "ddSpecialitatea");
                ddValue = List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Managementul educaţional (90)");
            }
            case "DM11M" -> {
                dd = List.of("ddCiclu", "ddDomeniul", "ddSpecialitatea");
                ddValue = List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Didactica matematicii");
            }
            default -> throw new IllegalStateException("Unexpected value: " + group.toUpperCase());
        }
        return getStudyPlan(dd, ddValue, group, semester);
    }

    @SneakyThrows
    private String getStudyPlan(List<String> dd, List<String> ddValue, String group, String semester) {

        getRequest();

        for (int i = 0; i < dd.size(); i++) {
            RequestBody formBody = new FormBody.Builder()
                    .add(PARAMS[0], getHiddenParam(PARAMS[0], responseBody))
                    .add(PARAMS[1], getHiddenParam(PARAMS[1], responseBody))
                    .add(PARAMS[2], getHiddenParam(PARAMS[2], responseBody))
                    .add(PARAMS[3], getHiddenParam(PARAMS[3], responseBody))
                    .add(PARAMS[4], getHiddenParam(PARAMS[4], responseBody))
                    .add(PARAMS[5], getHiddenParam(PARAMS[5], responseBody))
                    .add(dd.get(i), ddValue.get(i))
                    .build();

            Request request = new Request.Builder()
                    .url(STUDY_PLAN_URL)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .post(formBody)
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                responseBody = Objects.requireNonNull(response.body()).string();
            }
        }

        List<String> content = Jsoup.parse(responseBody)
                .getElementById(semester)
                .select("td").eachText();

        StringBuilder sb = new StringBuilder();

        sb.append("Planul de studii -- ").append(group).append("\n");
        if (semester.equals("tbSemI")) sb.append("====  Semestru I  ====\n");
        else sb.append("====  Semestru II  ====\n");

        for (int i = 0; i < content.size() - 1; i += 2) {
            sb.append("- ").append(content.get(i)).append("\n--- ").append(content.get(i + 1)).append("\n");
        }

        return objectMapper.writeValueAsString(sb);
    }

    private static String getHiddenParam(String id, String body) {

        Matcher m = Pattern.compile(String.format("id=\"%s\"\\s+value=\"([^\"]+)\"", id)).matcher(body);

        if (m.find()) {
            return m.group(1);
        }

        return StringUtils.EMPTY;
    }
}