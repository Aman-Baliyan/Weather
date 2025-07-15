package com.example.ocean.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfigWeather {

    @Value("${google.api.key}")
    String apiKey;
    @Bean(name = "googleApi")
    public WebClient webWeather(){
        return WebClient.builder()
                .build();
    }
}
