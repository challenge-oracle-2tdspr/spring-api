package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class SensorCreateRequestDTO {
    @NotBlank(message = "sensorCode is required")
    @Size(min = 3, max = 50, message = "sensorCode must be between 3 and 50 characters")
    private String sensorCode;

    private String model;

    private String manufacturer;

    private LocalDateTime installationDate;

    @Min(value = 0, message = "batteryLevel must be greater than or equal to 0")
    @Max(value = 100, message = "batteryLevel must be less than or equal to 100")
    private Integer batteryLevel;

    @NotNull(message = "fieldId is required")
    private UUID fieldId;

    public Sensor toSensor(Field field) {
        return Sensor.builder()
                .sensorCode(this.sensorCode)
                .model(this.model)
                .manufacturer(this.manufacturer)
                .installationDate(this.installationDate)
                .batteryLevel(this.batteryLevel)
                .status(SensorStatus.ACTIVE)
                .field(field)
                .build();
    }
}
