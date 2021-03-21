package com.flux.parsingservice.parser;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class StudyPlanParser {

    private final Environment env;
    private final ObjectMapper objectMapper;

    public StudyPlanParser(Environment env, ObjectMapper objectMapper) {
        this.env = env;
        this.objectMapper = objectMapper;
    }

    private static final String[] PARAMS = {"__EVENTTARGET", "__EVENTARGUMENT", "__LASTFOCUS", "__VIEWSTATE", "__VIEWSTATEGENERATOR", "__EVENTVALIDATION"};
    private static final String STUDY_PLAN_URL = "http://planstudii.usarb.md";

    private String responseBody;
    private final OkHttpClient httpClient = new OkHttpClient();

    public void getRequest() {
        String agentUrl = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.97 Safari/537.36";
        CloseableHttpClient closeableHttpClient = HttpClientBuilder.create()
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setUserAgent(agentUrl)
                .setKeepAliveStrategy(new DefaultConnectionKeepAliveStrategy())
                .build();
        HttpGet httpGet = new HttpGet(STUDY_PLAN_URL);
        try (CloseableHttpResponse response1 = closeableHttpClient.execute(httpGet)) {
            HttpEntity entity1 = response1.getEntity();
            responseBody = EntityUtils.toString(entity1);
            EntityUtils.consume(entity1);
        } catch (UndeclaredThrowableException | IOException ignored) {
            log.error("Unexpected code");
        }
    }

    @SneakyThrows
    public String generateWebRequest(String semester, String userVo) {
        String lang = objectMapper.readTree(userVo).get("userLanguage").asText();
        String group = objectMapper.readTree(userVo).get("userGroup").asText();
        // will be better to load from db list of possible commands
        List<String> ddValue = switch (group.toUpperCase()) {
            case "ME13M" -> List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Managementul educaţional (90)");
            case "DM11M" -> List.of("Ciclul II (Master)", "Ştiinţe ale educaţiei", "Didactica matematicii");
            default -> Collections.emptyList();
        };

        return ddValue.isEmpty() ? objectMapper.writeValueAsString(env.getProperty(lang + ".unsupported_group"))
                : getStudyPlan(ddValue, semester, userVo);
    }

    @SneakyThrows
    private String getStudyPlan(List<String> ddValue, String semester, String userVo) {
        getRequest();

        List<String> dd = List.of("ddCiclu", "ddDomeniul", "ddSpecialitatea");
        dd.forEach(withCounter((i, param) -> {
            FormBody.Builder formBody = new FormBody.Builder();
            Arrays.stream(PARAMS).forEach(p -> formBody.add(p, getHiddenParam(p, responseBody)));
            formBody.add(dd.get(i), ddValue.get(i));

            Request request = new Request.Builder()
                    .url(STUDY_PLAN_URL)
                    .addHeader("User-Agent", "OkHttp Bot")
                    .post(formBody.build())
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                responseBody = Objects.requireNonNull(response.body()).string();
            } catch (IOException ex) {
                log.error("Error: ", ex);
            }
        }));

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

    public static <T> Consumer<T> withCounter(BiConsumer<Integer, T> consumer) {
        AtomicInteger counter = new AtomicInteger(0);
        return item -> consumer.accept(counter.getAndIncrement(), item);
    }
}