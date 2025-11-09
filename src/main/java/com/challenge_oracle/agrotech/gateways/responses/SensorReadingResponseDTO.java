package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.SensorReading;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SensorReadingResponseDTO extends RepresentationModel<SensorReadingResponseDTO> {
    private UUID id;
    private LocalDateTime readingTime;
    private BigDecimal temperature;
    private BigDecimal humidity;
    private BigDecimal soilMoisture;
    private BigDecimal windSpeed;
    private String windDirection;
    private BigDecimal rainfall;
    private BigDecimal soilPh;
    private BigDecimal lightIntensity;
    private UUID sensorId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SensorReadingResponseDTO fromSensorReading(SensorReading reading) {
        return SensorReadingResponseDTO.builder()
                .id(reading.getId())
                .readingTime(reading.getReadingTime())
                .temperature(reading.getTemperature())
                .humidity(reading.getHumidity())
                .soilMoisture(reading.getSoilMoisture())
                .windSpeed(reading.getWindSpeed())
                .windDirection(reading.getWindDirection())
                .rainfall(reading.getRainfall())
                .soilPh(reading.getSoilPh())
                .lightIntensity(reading.getLightIntensity())
                .sensorId(reading.getSensor() != null ? reading.getSensor().getId() : null)
                .createdAt(reading.getCreatedAt())
                .updatedAt(reading.getUpdatedAt())
                .build();
    }
}
