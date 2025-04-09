package com.example.air_quality.service;

import org.springframework.stereotype.Service;

@Service
public class AnomalyService {
    private static final double THRESHOLD = 2.0;

    public boolean isAnomalyDetected(double currentValue, double mean, double stdDev) {
        if (stdDev == 0) {
            return false;
        }
        double zScore = (currentValue - mean) / stdDev;
        return zScore > THRESHOLD;
    }
}