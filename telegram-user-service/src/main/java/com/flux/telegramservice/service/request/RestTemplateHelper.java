package com.flux.telegramservice.service.request;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.MessageChannel;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static com.flux.telegramservice.util.Links.LOGISTIC_SERVICE;

@Slf4j
public abstract class RestTemplateHelper {

    @Autowired protected RestTemplate restTemplate;

    @Autowired protected ObjectMapper objectMapper;


    public <T> T getForObject(Class<T> clazz, String url, Object...uriVariables) {
        try {
            ResponseEntity<String> response = restTemplate.getForEntity(LOGISTIC_SERVICE + url, String.class, uriVariables);
            JavaType javaType = objectMapper.getTypeFactory().constructType(clazz);
            return readValue(response, javaType);
        } catch (HttpClientErrorException exception) {
            if (exception.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.error("No data found {}", url);
            } else {
                log.error("rest client exception: " + exception.getMessage());
            }
        }
        return null;
    }

    private <T> T readValue(ResponseEntity<String> response, JavaType javaType) {
        T result = null;
        if (response.getStatusCode() == HttpStatus.OK ||
                response.getStatusCode() == HttpStatus.CREATED) {
            try {
                result = objectMapper.readValue(response.getBody(), javaType);
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        } else {
            log.error("No data found {}", response.getStatusCode());
        }
        return result;
    }
}
