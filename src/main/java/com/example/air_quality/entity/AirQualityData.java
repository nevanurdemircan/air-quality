package com.example.air_quality.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
public class AirQualityData {
    @Id
    @GeneratedValue
    private UUID id;
    private double latitude;
    private double longitude;
    private Double pm25;
    private Double pm10;
    private Double no2;
    private Double so2;
    private Double o3;
    private LocalDateTime timestamp;
    private Boolean anomaly;
    private String anomalyReason;
}
