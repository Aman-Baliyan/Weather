package com.example.ocean.dtos;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeatherResponse {
    private String latitude;
    private String longitude;
    private double windSpeed;
    private double waveHeight;
    private double airTemperature;
    private double cloudCover;
    private double precipitation;
    private String weatherType;

}
