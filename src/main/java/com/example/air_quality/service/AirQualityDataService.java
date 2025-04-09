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

        double minLat = 36.0;
        double maxLat = 42.0;
        double minLon = 26.0;
        double maxLon = 45.0;
        double step = 1.0;

        long start = 1681000000;
        long end = 1681600000;

        for (double lat = minLat; lat <= maxLat; lat += step) {
            for (double lon = minLon; lon <= maxLon; lon += step) {
                String url = String.format(
                        "http://api.openweathermap.org/data/2.5/air_pollution/history?bbox=%f,%f,%f,%f&appid=%s",
                        lat, lon, start, end, apiKey
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
                }}}}

                public List<AirQualityData> getAnomalies () {
                    return airQualityDataRepository.findAll().stream()
                            .filter(data ->
                                    data.getPm25() != null && data.getPm25() > 50 ||
                                            data.getPm10() != null && data.getPm10() > 80 ||
                                            data.getNo2() != null && data.getNo2() > 100 ||
                                            data.getO3() != null && data.getO3() > 120 ||
                                            data.getSo2() != null && data.getSo2() > 75
                            ).collect(Collectors.toList());
                }
                public List<AirQualityData> getAllData () {
                    return airQualityDataRepository.findAll();
                }
            }
