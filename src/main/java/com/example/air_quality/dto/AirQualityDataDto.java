package com.example.air_quality.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AirQualityDataDto {
    private double latitude;
    private double longitude;
    private LocalDateTime start;
    private LocalDateTime end;
}
