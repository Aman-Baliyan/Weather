package com.example.ocean.controller;

import com.example.ocean.dtos.TravelData;
import com.example.ocean.service.OpenAiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api")
public class OpenAiController {

    @Autowired
    private OpenAiService openAiService;

    @PostMapping("/generate")
    public Map<String, Object> generateItinerary(@RequestBody TravelData travelData) {
        String source = travelData.getSource();
        String destination = travelData.getDestination();
        LocalDate startDate = LocalDate.parse(travelData.getStartDate());
        LocalDate endDate = LocalDate.parse(travelData.getEndDate());
        int days = (int) ChronoUnit.DAYS.between(startDate, endDate) + 1;

        if (days <= 0) {
            return Map.of("error", "End date must be after start date.");
        }

        return openAiService.generateItinerary(source, destination, startDate.toString(), endDate.toString(), days);
    }
}
