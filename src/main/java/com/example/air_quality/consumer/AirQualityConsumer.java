package com.example.air_quality.consumer;

import com.example.air_quality.entity.AirQualityData;
import com.example.air_quality.repository.AirQualityDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
@RequiredArgsConstructor
public class AirQualityConsumer {
    private final ObjectMapper objectMapper;
    private final AirQualityDataRepository airQualityDataRepository;

    @KafkaListener(topics = "air-quality-topic", groupId = "air-quality-group")
    public ResponseEntity<String> listen(String message) {
        try {
            JsonNode root = objectMapper.readTree(message);
            JsonNode components = root.get("list").get(0).get("components");

            AirQualityData data = new AirQualityData();
            data.setLatitude(root.get("coord").get("lat").asDouble());
            data.setLongitude(root.get("coord").get("lon").asDouble());
            data.setPm25(components.get("pm2_5").asDouble());
            data.setPm10(components.get("pm10").asDouble());
            data.setNo2(components.get("no2").asDouble());
            data.setSo2(components.get("so2").asDouble());
            data.setO3(components.get("o3").asDouble());

            long dt = root.get("list").get(0).get("dt").asLong();
            data.setTimestamp(LocalDateTime.ofInstant(Instant.ofEpochSecond(dt), ZoneId.systemDefault()));

            if (data.getPm25() != null && data.getPm25() > 50 ||
                    data.getPm10() != null && data.getPm10() > 80 ||
                    data.getNo2() != null && data.getNo2() > 100 ||
                    data.getO3() != null && data.getO3() > 120 ||
                    data.getSo2() != null && data.getSo2() > 75) {
                data.setAnomaly(true);
            } else {
                data.setAnomaly(false);
            }

            airQualityDataRepository.save(data);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Veri işleme sırasında bir hata oluştu: " + e.getMessage());        }
        return null;
    }
}