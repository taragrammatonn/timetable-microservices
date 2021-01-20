package com.flux.parsingservice;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class ParsingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParsingServiceApplication.class, args);
	}

	@Bean
	public Gson gson() {
		return new Gson();
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
}
