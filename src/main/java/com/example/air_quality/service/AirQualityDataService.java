package com.example.air_quality.service;

import com.example.air_quality.dto.AirQualityDataDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
@RequiredArgsConstructor
public class AirQualityDataService {
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${openweathermap.api.key}")
    private String apiKey;

    public void fetchAndSendData(AirQualityDataDto dto) {
        String url = String.format(
                "https://api.openweathermap.org/data/2.5/air_pollution?lat=%f&lon=%f&appid=%s",
                dto.getLatitude(), dto.getLongitude(), apiKey
        );

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            String json = response.body();

            kafkaTemplate.send("air-quality-topic", json);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
