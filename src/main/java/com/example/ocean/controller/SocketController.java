package com.example.ocean.controller;

import com.example.ocean.dtos.WeatherResponse;
import com.example.ocean.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SocketController {

    @Autowired
    LocationService locationService;


//    @MessageMapping("/sendMessage")
//    @SendTo("/weather/alert")
//    public String Message(String Message){
//
//        return "hello";
//    }

    @GetMapping("/weatherData")
    public String weatherResponse(@RequestParam("longitude") String longitude, @RequestParam("latitude") String latitude){
        WeatherResponse weatherResponse = locationService.weatherService(longitude, latitude).block();
        if(weatherResponse != null){
            return locationService.alertMessage(weatherResponse);
        }
        return null;
    }
}
