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
        for Example- {
                                 "transport_options": {
                                     "to_destination": {
                                         {
                                            "mode": "Flight",
                                             "estimated_time": "2 hours",
                                             "cost": "6000 INR"
                                         },
                                         {
                                            "mode": "Train",
                                             "estimated_time": "2 hours",
                                             "cost": "6000 INR"
                                         },
                                         {
                                            "mode": "Bus",
                                             "estimated_time": "2 hours",
                                             "cost": "6000 INR"
                                         }
                                         
                                     }
                                  
                                 },
                                 "local_transport": [
                                     "Bike Rentals",
                                     "Scooter Rentals",
                                     "Taxis",
                                     "Local Buses"
                                 ],
                                 "nature_spots": [
                                     "Calangute Beach",
                                     "Baga Beach",
                                     "Anjuna Beach",
                                     "Mandrem Beach",
                                     "Dudhsagar Waterfalls"
                                 ],
                                 "tourist_spots": [
                                     "Fort Aguada",
                                     "Basilica of Bom Jesus",
                                     "Chapora Fort",
                                     "Anjuna Flea Market",
                                     "Goan Fish Curry",
                                     "Prawn Vindaloo",
                                     "Bebinca"
                                 ],
                                 "day_wise_itinerary": [
                                     {
                                         "day": "2025-08-01",
                                         "activities": [
                                             {
                                                 "time": "8 AM",
                                                 "activity": "Flight from Delhi to Goa"
                                             },
                                             {
                                                 "time": "10 AM",
                                                 "activity": "Check-in at hotel"
                                             },
                                             {
                                                 "time": "12 PM",
                                                 "activity": "Lunch at local restaurant"
                                             },
                                             {
                                                 "time": "2 PM",
                                                 "activity": "Visit Calangute Beach"
                                             },
                                             {
                                                 "time": "5 PM",
                                                 "activity": "Relax at the beach"
                                             },
                                             {
                                                 "time": "7 PM",
                                                 "activity": "Dinner at beach shack"
                                             }
                                         ]
                                     },
                                     {
                                         "day": "2025-08-02",
                                         "activities": [
                                             {
                                                 "time": "8 AM",
                                                 "activity": "Breakfast at hotel"
                                             },
                                             {
                                                 "time": "10 AM",
                                                 "activity": "Visit Fort Aguada"
                                             },
                                             {
                                                 "time": "1 PM",
                                                 "activity": "Lunch nearby"
                                             },
                                             {
                                                 "time": "3 PM",
                                                 "activity": "Explore Basilica of Bom Jesus"
                                             },
                                             {
                                                 "time": "6 PM",
                                                 "activity": "Shopping at local market"
                                             },
                                             {
                                                 "time": "8 PM",
                                                 "activity": "Dinner at local restaurant"
                                             }
                                         ]
                                     },
                                     {
                                         "day": "2025-08-03",
                                         "activities": [
                                             {
                                                 "time": "8 AM",
                                                 "activity": "Breakfast at hotel"
                                             },
                                             {
                                                 "time": "10 AM",
                                                 "activity": "Visit Anjuna Beach"
                                             },
                                             {
                                                 "time": "1 PM",
                                                 "activity": "Lunch at beachside cafe"
                                             },
                                             {
                                                 "time": "3 PM",
                                                 "activity": "Explore Anjuna Flea Market"
                                             },
                                             {
                                                 "time": "6 PM",
                                                 "activity": "Watch sunset at Anjuna"
                                             },
                                             {
                                                 "time": "8 PM",
                                                 "activity": "Dinner at local restaurant"
                                             }
                                         ]
                                     },
                                     {
                                         "day": "2025-08-04",
                                         "activities": [
                                             {
                                                 "time": "8 AM",
                                                 "activity": "Breakfast at hotel"
                                             },
                                             {
                                                 "time": "10 AM",
                                                 "activity": "Day trip to Dudhsagar Waterfalls"
                                             },
                                             {
                                                 "time": "1 PM",
                                                 "activity": "Lunch on trip"
                                             },
                                             {
                                                 "time": "5 PM",
                                                 "activity": "Return to hotel"
                                             },
                                             {
                                                 "time": "7 PM",
                                                 "activity": "Dinner at local restaurant"
                                             },
                                             {
                                                 "time": "9 PM",
                                                 "activity": "Pack for departure"
                                             }
                                         ]
                                     },
                                     {
                                         "day": "2025-08-05",
                                         "activities": [
                                             {
                                                 "time": "8 AM",
                                                 "activity": "Breakfast at hotel"
                                             },
                                             {
                                                 "time": "10 AM",
                                                 "activity": "Check-out from hotel"
                                             },
                                             {
                                                 "time": "12 PM",
                                                 "activity": "Flight from Goa to Delhi"
                                             }
                                         ]
                                     }
                                 ],
                                 "return_plan": {
                                     "time": "12 PM",
                                     "mode": "Flight"
                                 },
                                 "budget_breakdown": {
                                     "travel": "12000 INR",
                                     "stay": "10000 INR",
                                     "food": "5000 INR",
                                     "activities": "3000 INR",
                                     "total": "30000 INR"
                                 }
                             }
         
        Ensure the response is only a clean, valid JSON object. Do not include explanations, markdown, or greetings and the heading names should be same as given in the example
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
