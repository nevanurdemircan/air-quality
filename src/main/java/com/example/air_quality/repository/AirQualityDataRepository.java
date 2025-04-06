package com.example.air_quality.repository;

import com.example.air_quality.entity.AirQualityData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface AirQualityDataRepository extends JpaRepository<AirQualityData, UUID> {
    List<AirQualityData> findByLongitudeAndLatitude(double longitude, double latitude);

}
