package com.example.ocean.service;

import com.example.ocean.dtos.CombinedResponse;
import com.example.ocean.dtos.GetLocation;
import com.example.ocean.dtos.MeteoData;
import com.example.ocean.dtos.WeatherResponse;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AnalyzerService {



    @Value("${google.api.key}")
    String apiKey;

    @Qualifier("googleApi")
    @Autowired
    WebClient webClient;

    @Autowired
    LocationService locationService;






    // Function to get user lat and lng from location
    public Mono<GetLocation> getLocation(String location){
        return  webClient
                .get()
                .uri("https://maps.googleapis.com/maps/api/geocode/json?address="+location+"&key="+apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .map(body -> {
                    JSONObject js = new JSONObject(body);
                    GetLocation loc = new GetLocation();
                    JSONObject locationObj = js.getJSONArray("results")
                            .getJSONObject(0)
                            .getJSONObject("geometry")
                            .getJSONObject("location");
                    double lat = locationObj.getDouble("lat");
                    double lng = locationObj.getDouble("lng");
                    loc.setLatitude(String.valueOf(lat));
                    loc.setLongitude(String.valueOf(lng));
                    return loc;
                });
    }


    // Function to get some weather related data
    public Mono<MeteoData> openMeteoApiData(String lat, String lng){
        HashMap<Integer, String> map = new HashMap<>();
        map.put(0, "Clear Sky");
        map.put(1, "Mainly clear");
        map.put(2, "Partly cloudy");
        map.put(3, "Overcast");
        map.put(45, "Fog");
        map.put(51, "Light drizzle");
        map.put(61, "Light rain");
        map.put(80, "Rain showers");
        map.put(95, "Thunderstorm");

        ZonedDateTime now = ZonedDateTime.now();
        ZonedDateTime later = now.plusHours(3);
        DateTimeFormatter formatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        String baseUrl = "https://api.open-meteo.com/v1/forecast";
        String url = baseUrl + "?latitude=" + lat +
                "&longitude=" + lng +
                "&hourly=temperature_2m,wind_speed_10m,uv_index,weather_code" +
                "&start=" + formatter.format(now) +
                "&end=" + formatter.format(later) +
                "&timezone=auto";
        return webClient
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .map( body->{
                    JSONObject js = new JSONObject(body);
                    MeteoData md = new MeteoData();
                    md.setTemperature(js.getJSONObject("hourly").getJSONArray("temperature_2m").getDouble(0));
                    md.setWind_speed(js.getJSONObject("hourly").getJSONArray("wind_speed_10m").getDouble(0));
                    md.setUv_index(js.getJSONObject("hourly").getJSONArray("uv_index").getDouble(0));
                    md.setWeatherCode(map.getOrDefault(js.getJSONObject("hourly").getJSONArray("weather_code").getInt(0), "Undefined"));
                    return md;
                });
    }

    // Function to combine all the data required for determining bad weather

    public Mono<CombinedResponse> combinedApi(String name){
           Mono<GetLocation> getLocationMono = getLocation(name);
           return getLocationMono.flatMap(location->{
               Mono<MeteoData> meteoDataMono = openMeteoApiData(location.getLatitude(), location.getLongitude());
               Mono<WeatherResponse> weatherResponseMono = locationService.weatherService(location.getLongitude(), location.getLatitude());

               return Mono.zip(meteoDataMono, weatherResponseMono)
                       .map(tuple -> {
                           MeteoData data = tuple.getT1();
                           WeatherResponse weatherResponse = tuple.getT2();
                           CombinedResponse cb = new CombinedResponse();
                           cb.setLongitude(location.getLongitude());
                           cb.setLatitude(location.getLatitude());
                           List<String> alert = getStrings(data, weatherResponse);
                           cb.setAlerts(alert);
                           cb.setCaution(totalScore(weatherResponse.getWaveHeight(), data.getWind_speed(), data.getWeatherCode()));
                           cb.setActivity(calc(weatherResponse.getWaveHeight(), data.getWind_speed(), data.getWeatherCode()));
                           cb.setCurrent(new CombinedResponse.Current(weatherResponse.getWaveHeight(),weatherResponse.getWaterTemperature(),data.getWind_speed(),data.getUv_index(),data.getWeatherCode()));

                           return cb;
                       });
           });

    }

    // Function to calculate alert
    private static List<String> getStrings(MeteoData data, WeatherResponse weatherResponse) {
        List<String> alert = new ArrayList<>();
        if(data.getUv_index() > 8){
            alert.add("High UV index: Wear sun protection.");
        }
        if(weatherResponse.getWaveHeight() > 2){
            alert.add("Rough seas: Caution for swimmers/surfers.");
        }
        if(weatherResponse.getWindSpeed() > 30){
            alert.add("Strong wind: Avoid boating activities.");
        }
        return alert;
    }


    // Overall score Calculator
    private static int totalScore(double waveHeight, double windSpeed, String weatherType){
        int score = 100;  // Start from 100

       // wave
        if (waveHeight > 3.0) {
            score -= 30;
        } else if (waveHeight > 2.0) {
            score -= 20;
        } else if (waveHeight > 1.0) {
            score -= 10;
        }

        // wind
        if (windSpeed > 30) {
            score -= 25;
        } else if (windSpeed > 20) {
            score -= 15;
        } else if (windSpeed > 10) {
            score -= 5;
        }

        // type
        String type = weatherType.toLowerCase();
        if (type.contains("thunderstorm")) {
            score -= 30;
        } else if (type.contains("rain")) {
            score -= 20;
        } else if (type.contains("fog") || type.contains("overcast")) {
            score -= 10;
        }
        return Math.max(0, score);
    }

    // Per Activity score Calculator
    public CombinedResponse.Activity calc(double waveHeight, double windSpeed, String weatherType){
        String type = weatherType.toLowerCase();

        // Base scores (start from 100)
        int swimming = 100;
        int surfing = 100;
        int boating = 100;
        int fishing = 100;
        int diving = 100;


        if (waveHeight > 3) {
            swimming -= 40;
            surfing -= 10;
            diving -= 30;
            boating -= 20;
        } else if (waveHeight > 2) {
            swimming -= 30;
            surfing -= 5;
            diving -= 20;
        } else if (waveHeight > 1.5) {
            swimming -= 20;
            diving -= 10;
        }


        if (windSpeed > 30) {
            surfing -= 25;
            boating -= 30;
            fishing -= 15;
        } else if (windSpeed > 20) {
            surfing -= 15;
            boating -= 20;
            fishing -= 10;
        } else if (windSpeed > 10) {
            boating -= 10;
        }


        if (type.contains("thunderstorm")) {
            swimming -= 30;
            surfing -= 30;
            boating -= 40;
            diving -= 40;
            fishing -= 20;
        } else if (type.contains("rain")) {
            swimming -= 15;
            boating -= 20;
            fishing -= 10;
        } else if (type.contains("fog") || type.contains("overcast")) {
            boating -= 15;
            diving -= 20;
        }

        swimming = Math.max(0, swimming);
        boating = Math.max(0, boating);
        fishing = Math.max(0, fishing);
        diving = Math.max(0, diving);
        surfing = Math.max(0, surfing);
        return new CombinedResponse.Activity(swimming, surfing, boating, fishing, diving);
    }



}
