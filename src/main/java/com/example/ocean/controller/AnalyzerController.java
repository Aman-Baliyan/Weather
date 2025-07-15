package com.example.ocean.controller;

import com.example.ocean.dtos.CombinedResponse;
import com.example.ocean.dtos.GetLocation;
import com.example.ocean.dtos.MeteoData;
import com.example.ocean.dtos.WeatherResponse;
import com.example.ocean.service.AnalyzerService;
import com.example.ocean.service.LocationService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/search")
public class AnalyzerController {

    @Autowired
    AnalyzerService analyzerService;

    @Autowired
    LocationService locationService;

    @GetMapping("/analyzeData/{beach}")
    public Mono<CombinedResponse> analyzeBeach(@PathVariable("beach") String beach){
      return analyzerService.combinedApi(beach);
    }
}
