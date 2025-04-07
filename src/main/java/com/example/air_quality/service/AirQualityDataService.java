package com.example.air_quality.service;

import com.example.air_quality.dto.AirQualityDataDto;
import com.example.air_quality.entity.AirQualityData;
import com.example.air_quality.repository.AirQualityDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AirQualityDataService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final AirQualityDataRepository airQualityDataRepository;

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
    public List<AirQualityData> getAnomalies() {
        return airQualityDataRepository.findAll().stream()
                .filter(data ->
                        data.getPm25() != null && data.getPm25() > 50 ||
                                data.getPm10() != null && data.getPm10() > 80 ||
                                data.getNo2() != null && data.getNo2() > 100 ||
                                data.getO3() != null && data.getO3() > 120 ||
                                data.getSo2() != null && data.getSo2() > 75
                ).collect(Collectors.toList());
    }
    public List<AirQualityData> getByDateRange(LocalDateTime start, LocalDateTime end) {
        return airQualityDataRepository.findByTimestampBetween(start, end);
    }
}
