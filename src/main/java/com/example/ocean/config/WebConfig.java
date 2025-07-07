package com.example.ocean.config;

//import lombok.Value;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.reactive.function.client.WebClientAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebConfig{

    @Value("${api.key}")
    private String apiKey;

    @Bean
    public WebClient webClient(){
        return WebClient.builder()
                .baseUrl("https://api.stormglass.io/v2/weather/point")
                .defaultHeader("Authorization", apiKey)
                .build();
    }


}

