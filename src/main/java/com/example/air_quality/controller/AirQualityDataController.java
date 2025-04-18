package com.example.air_quality.controller;

import com.example.air_quality.dto.AirQualityDataDto;
import com.example.air_quality.entity.AirQualityData;
import com.example.air_quality.repository.AirQualityDataRepository;
import com.example.air_quality.service.AirQualityDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/air-quality")
@RequiredArgsConstructor
public class AirQualityDataController {
    private final AirQualityDataService airQualityDataService;
    private final AirQualityDataRepository airQualityDataRepository;

    @PostMapping("/fetch")
    public ResponseEntity<String> fetchData(@RequestBody AirQualityDataDto dto) {
        airQualityDataService.fetchAndSendData(dto);
        return ResponseEntity.ok("Veri alındı ve Kafka'ya gönderildi.");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AirQualityData>> getAllData() {
        return ResponseEntity.ok(airQualityDataRepository.findAll());
    }

    @GetMapping("/anomalies")
    public ResponseEntity<List<AirQualityData>> getAnomalies() {
        return ResponseEntity.ok(airQualityDataService.getAnomalies());
    }

    @PostMapping("/date")
    public ResponseEntity<List<AirQualityData>> getByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {

        List<AirQualityData> filteredData = airQualityDataService.getAllData().stream()
                .filter(data -> !data.getTimestamp().isBefore(start) && !data.getTimestamp().isAfter(end))
                .collect(Collectors.toList());

        return ResponseEntity.ok(filteredData);
    }
}
