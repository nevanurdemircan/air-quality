package com.example.air_quality;

import com.example.air_quality.entity.AirQualityData;
import com.example.air_quality.repository.AirQualityDataRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class AirQualityConsumer {
    private final ObjectMapper objectMapper;
    private final AirQualityDataRepository repository;

    public AirQualityConsumer(AirQualityDataRepository repository) {
        this.repository = repository;
        this.objectMapper = new ObjectMapper();
    }

    @KafkaListener(topics = "air-quality-topic", groupId = "air-quality-group")
    public void listen(String message) {
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

            repository.save(data);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
