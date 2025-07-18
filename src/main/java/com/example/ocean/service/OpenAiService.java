package com.example.ocean.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.*;

@Service
public class OpenAiService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    @Qualifier("googleApi")
    @Autowired
    WebClient webClient;

    public Map<String, Object> generateItinerary(String source, String destination, String startDate, String endDate, int days) {
        String prompt = String.format("""
        You are a travel planner. Return only a valid JSON object for the following itinerary request:
        
        Source: %s  
        Destination: %s  
        Start Date: %s  
        End Date: %s  
        Number of Days: %d  
        
        Return a JSON with the following fields:
        - "transport_options": best mode to reach destination and return (with estimated time and cost)
        - "local_transport": modes of travel available locally
        - "nature_spots": list of beaches, riversides, parks, etc.
        - "tourist_spots": list of tourist places and local foods
        - "day_wise_itinerary": array of daily plans with time slots (8 AM to 8 PM) and activities
        - "return_plan": return journey plan with time and mode
        - "budget_breakdown": object with keys: travel, stay, food, activities, and total
        
        Ensure the response is only a clean, valid JSON object. Do not include explanations, markdown, or greetings.
        """, source, destination, startDate, endDate, days);

        Map<String, Object> body = Map.of(
                "model", "gpt-4o-mini",
                "messages", List.of(
                        Map.of("role", "system", "content", "You are a helpful travel assistant."),
                        Map.of("role", "user", "content", prompt)
                )
        );

        Map<String, Object> response = webClient.post()
                .uri(apiUrl)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (response == null || !response.containsKey("choices")) {
            return Map.of("error", "No response from OpenAI.");
        }

        List<Map<String, Object>> choices = (List<Map<String, Object>>) response.get("choices");
        if (choices.isEmpty()) {
            return Map.of("error", "No choices received.");
        }

        Map<String, Object> messageData = (Map<String, Object>) choices.get(0).get("message");
        String content = (String) messageData.get("content");

        try {
            // Convert the JSON string response into a Map
            return new ObjectMapper().readValue(content, Map.class);
        } catch (Exception e) {
            return Map.of("error", "Failed to parse JSON", "raw", content);
        }
    }
}
