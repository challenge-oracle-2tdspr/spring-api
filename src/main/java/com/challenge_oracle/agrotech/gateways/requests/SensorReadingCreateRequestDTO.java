package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.domains.SensorReading;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SensorReadingCreateRequestDTO {
    @NotNull(message = "readingTime is required")
    private LocalDateTime readingTime;

    @DecimalMin(value = "-50", message = "temperature must be greater than or equal to -50")
    @DecimalMax(value = "70", message = "temperature must be less than or equal to 70")
    @Digits(integer = 3, fraction = 2, message = "temperature must have at most 3 integer digits and 2 decimal places")
    private BigDecimal temperature;

    @DecimalMin(value = "0", message = "humidity must be greater than or equal to 0")
    @DecimalMax(value = "100", message = "humidity must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "humidity must have at most 3 integer digits and 2 decimal places")
    private BigDecimal humidity;

    @DecimalMin(value = "0", message = "soilMoisture must be greater than or equal to 0")
    @DecimalMax(value = "100", message = "soilMoisture must be less than or equal to 100")
    @Digits(integer = 3, fraction = 2, message = "soilMoisture must have at most 3 integer digits and 2 decimal places")
    private BigDecimal soilMoisture;

    @DecimalMin(value = "0", message = "windSpeed must be greater than or equal to 0")
    @Digits(integer = 3, fraction = 2, message = "windSpeed must have at most 3 integer digits and 2 decimal places")
    private BigDecimal windSpeed;

    @Size(max = 10, message = "windDirection must have at most 10 characters")
    private String windDirection;

    @DecimalMin(value = "0", message = "rainfall must be greater than or equal to 0")
    @Digits(integer = 4, fraction = 2, message = "rainfall must have at most 4 integer digits and 2 decimal places")
    private BigDecimal rainfall;

    @DecimalMin(value = "0", message = "soilPh must be greater than or equal to 0")
    @DecimalMax(value = "14", message = "soilPh must be less than or equal to 14")
    @Digits(integer = 2, fraction = 2, message = "soilPh must have at most 2 integer digits and 2 decimal places")
    private BigDecimal soilPh;

    @DecimalMin(value = "0", message = "lightIntensity must be greater than or equal to 0")
    @Digits(integer = 6, fraction = 2, message = "lightIntensity must have at most 6 integer digits and 2 decimal places")
    private BigDecimal lightIntensity;

    @NotNull(message = "sensorId is required")
    private UUID sensorId;

    public SensorReading toSensorReading(Sensor sensor) {
        return SensorReading.builder()
                .readingTime(this.readingTime)
                .temperature(this.temperature)
                .humidity(this.humidity)
                .soilMoisture(this.soilMoisture)
                .windSpeed(this.windSpeed)
                .windDirection(this.windDirection)
                .rainfall(this.rainfall)
                .soilPh(this.soilPh)
                .lightIntensity(this.lightIntensity)
                .sensor(sensor)
                .build();
    }
}
