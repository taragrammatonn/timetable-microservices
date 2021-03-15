package com.flux.parsingservice.parser;

import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class StudyPlanParser {

    private final Environment env;
    private final ObjectMapper objectMapper;

    public StudyPlanParser(Environment env, ObjectMapper objectMapper) {
        this.env = env;
        this.objectMapper = objectMapper;
    }

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

    @SneakyThrows
    public String generateWebRequest(String semester, String userVo) {
        List<String> ddValue;
        String group = objectMapper.readTree(userVo).get("userGroup").asText();
        switch (group.toUpperCase()) {
            case "ME13M" -> ddValue = List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Managementul educaţional (90)");
            case "DM11M" -> ddValue = List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Didactica matematicii");
            default -> throw new IllegalStateException("Unexpected value: " + group.toUpperCase());
        }
        return getStudyPlan(ddValue, semester, userVo);
    }

    @SneakyThrows
    private String getStudyPlan(List<String> ddValue, String semester, String userVo) {
        getRequest();

        List<String> dd = List.of("ddCiclu", "ddDomeniul", "ddSpecialitatea");
        for (int i = 0; i < dd.size(); i++) {
            FormBody.Builder formBody = new FormBody.Builder();
            for (String param: PARAMS) {
                formBody.add(param, getHiddenParam(param, responseBody));
            }
            formBody.add(dd.get(i), ddValue.get(i));

            Request request = new Request.Builder()
                    .url(STUDY_PLAN_URL)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .post(formBody.build())
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                responseBody = Objects.requireNonNull(response.body()).string();
            }
        }

        List<String> content = Jsoup.parse(responseBody)
                .getElementById(semester)
                .select("td").eachText();

        JsonNode jsonNode = objectMapper.readTree(userVo);
        String group = jsonNode.get("userGroup").asText();
        String lang = jsonNode.get("userLanguage").asText();
        StringBuilder sb = new StringBuilder();

        sb.append(env.getProperty(lang + ".study_plan")).append(group.toUpperCase()).append("\n")
        .append(env.getProperty(lang + (semester.equals("tbSemI") ? ".semester_I" : ".semester_II")));

        for (int i = 0; i < content.size() - 1; i += 2) {
            sb.append("- ").append(content.get(i)).append("\n--- ").append(content.get(i + 1)).append("\n");
        }

        return objectMapper.writeValueAsString(sb);
    }

    private static String getHiddenParam(String id, String body) {
        Matcher m = Pattern.compile(String.format("id=\"%s\"\\s+value=\"([^\"]+)\"", id)).matcher(body);

        return m.find() ? m.group(1) : StringUtils.EMPTY;
    }
}