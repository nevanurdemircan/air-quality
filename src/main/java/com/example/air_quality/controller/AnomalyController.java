package com.example.air_quality.controller;

import com.example.air_quality.service.AnomalyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AnomalyController {
    private AnomalyService anomalyService;

    @GetMapping("/api/check-anomaly")
    public boolean checkAnomaly(
            @RequestParam double currentValue,
            @RequestParam double mean,
            @RequestParam double stdDev) {
        return anomalyService.isAnomalyDetected(currentValue, mean, stdDev);
    }
}
