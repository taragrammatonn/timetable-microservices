package com.flux.parsingservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

@SpringBootApplication
public class ParsingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(ParsingServiceApplication.class, args);
	}

	@Bean
	@LoadBalanced
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public CorsFilter corsFilter() {
		var config = new CorsConfiguration();
		config.addAllowedOrigin("http://127.0.0.1:3000");
		config.addAllowedOrigin("http://localhost:3000");
		config.addAllowedHeader("*");
		config.setAllowedMethods(Arrays.asList("OPTIONS", "GET"));

		var source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/actuator/**", config);

		return new CorsFilter(source);
	}
}
