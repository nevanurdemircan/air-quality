package com.example.air_quality.controller;

import com.example.air_quality.dto.AirQualityDataDto;
import com.example.air_quality.service.AirQualityDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
public class AirQualityDataController {
    private final AirQualityDataService airQualityDataService;
    @PostMapping("/fetch")
    public ResponseEntity<String> fetchAirQuality(@RequestBody AirQualityDataDto dto) {
        airQualityDataService.fetchAndSendData(dto);
        return ResponseEntity.ok("Veri çekildi ve Kafka'ya gönderildi");
    }
}
