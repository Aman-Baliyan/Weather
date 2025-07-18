package com.example.ocean.dtos;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TravelData {
    String source;
    String destination;
    String startDate;
    String endDate;
}
