package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record SensorResponseDTO(
        UUID id,
        String sensorCode,
        String model,
        String manufacturer,
        LocalDateTime installationDate,
        SensorStatus status,
        Integer batteryLevel,
        LocalDateTime lastMaintenance,
        UUID fieldId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static SensorResponseDTO fromSensor(Sensor sensor) {
        return new SensorResponseDTO(
                sensor.getId(),
                sensor.getSensorCode(),
                sensor.getModel(),
                sensor.getManufacturer(),
                sensor.getInstallationDate(),
                sensor.getStatus(),
                sensor.getBatteryLevel(),
                sensor.getLastMaintenance(),
                sensor.getField() != null ? sensor.getField().getId() : null,
                sensor.getCreatedAt(),
                sensor.getUpdatedAt()
        );
    }
}
