package com.example.ocean.service;

import com.example.ocean.dtos.WeatherResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class LocationService {

    @Autowired
    WebClient webClient;

    public Mono<WeatherResponse> weatherService(String longitude, String latitude){
            try{
                return   webClient
                        .get()
                        .uri("?lat="+latitude+"&lng="+longitude+"&params=windSpeed,waveHeight,airTemperature,cloudCover,precipitation,waterTemperature")
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(body -> {
                            JSONObject json = new JSONObject(body);
                            JSONObject hourData = json.getJSONArray("hours").getJSONObject(0);

                            WeatherResponse response = new WeatherResponse();
                            response.setLatitude(latitude);
                            response.setLongitude(longitude);
                            response.setWindSpeed(hourData.getJSONObject("windSpeed").getDouble("noaa"));
                            response.setWaterTemperature(hourData.getJSONObject("waterTemperature").getDouble("noaa"));
                            if (hourData.has("waveHeight") && hourData.getJSONObject("waveHeight").has("noaa")) {
                                response.setWaveHeight(hourData.getJSONObject("waveHeight").getDouble("noaa"));
                            } else {
                                response.setWaveHeight(0.0); // or null, depending on your design
                            }
//
                            response.setAirTemperature(hourData.getJSONObject("airTemperature").getDouble("noaa"));
                            response.setCloudCover(hourData.getJSONObject("cloudCover").getDouble("noaa"));
                            response.setPrecipitation(hourData.getJSONObject("precipitation").getDouble("noaa"));


                            double cloud = response.getCloudCover();
                            double rain = response.getPrecipitation();
                            String type;

                            if (rain > 0.2) type = "Rainy";
                            else if (cloud < 20) type = "Sunny";
                            else if (cloud < 50) type = "Partly Cloudy";
                            else type = "Cloudy";

                            response.setWeatherType(type);
                            return response;
                        });
            }
            catch(Exception e){
                System.out.println("Error");
                return null;
            }



    }
    public String alertMessage(WeatherResponse weatherResponse){
        boolean danger = false;
        String dangerReason = "";

        if (weatherResponse.getWindSpeed() > 15) {
            danger = true;
            dangerReason += "Strong winds. ";
        }
        if (weatherResponse.getWaveHeight() > 2.5) {
            danger = true;
            dangerReason += "High waves. ";
        }
        if (weatherResponse.getAirTemperature() > 45 || weatherResponse.getAirTemperature() < 0) {
            danger = true;
            dangerReason += "Extreme temperature. ";
        }
        if (weatherResponse.getPrecipitation() > 20) {
            danger = true;
            dangerReason += "Heavy rain. ";
        }
        if(!danger){
            return "safe";
        }
        return dangerReason;
    }


}
