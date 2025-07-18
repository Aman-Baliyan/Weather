package com.example.ocean.dtos;

import lombok.*;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CombinedResponse {
     String latitude;
     String longitude;
     int caution;
     private Current current;
     private Activity activity;
     private List<String> alerts;

     @Getter
     @Setter
     @Builder
     @AllArgsConstructor
     public static class Current{
         double waveHeight;
         double waterTemperature;
         double windSpeed;
         double uv_index;
         String weatherType;
     }

     @Getter
     @Setter
     @Builder
     @AllArgsConstructor
     @NoArgsConstructor
     public static class Activity{
         int swimming;
         int surfing;
         int boating;
         int fishing;
         int diving;
     }

}
