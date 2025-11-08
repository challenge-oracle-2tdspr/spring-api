package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.SensorReading;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record SensorReadingResponseDTO(
        UUID id,
        LocalDateTime readingTime,
        BigDecimal temperature,
        BigDecimal humidity,
        BigDecimal soilMoisture,
        BigDecimal windSpeed,
        String windDirection,
        BigDecimal rainfall,
        BigDecimal soilPh,
        BigDecimal lightIntensity,
        UUID sensorId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SensorReadingResponseDTO fromSensorReading(SensorReading reading) {
        return new SensorReadingResponseDTO(
                reading.getId(),
                reading.getReadingTime(),
                reading.getTemperature(),
                reading.getHumidity(),
                reading.getSoilMoisture(),
                reading.getWindSpeed(),
                reading.getWindDirection(),
                reading.getRainfall(),
                reading.getSoilPh(),
                reading.getLightIntensity(),
                reading.getSensor() != null ? reading.getSensor().getId() : null,
                reading.getCreatedAt(),
                reading.getUpdatedAt()
        );
    }
}
