package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SensorResponseDTO extends RepresentationModel<SensorResponseDTO> {
    private UUID id;
    private String sensorCode;
    private String model;
    private String manufacturer;
    private LocalDateTime installationDate;
    private SensorStatus status;
    private Integer batteryLevel;
    private LocalDateTime lastMaintenance;
    private UUID fieldId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static SensorResponseDTO fromSensor(Sensor sensor) {
        return SensorResponseDTO.builder()
                .id(sensor.getId())
                .sensorCode(sensor.getSensorCode())
                .model(sensor.getModel())
                .manufacturer(sensor.getManufacturer())
                .installationDate(sensor.getInstallationDate())
                .status(sensor.getStatus())
                .batteryLevel(sensor.getBatteryLevel())
                .lastMaintenance(sensor.getLastMaintenance())
                .fieldId(sensor.getField() != null ? sensor.getField().getId() : null)
                .createdAt(sensor.getCreatedAt())
                .updatedAt(sensor.getUpdatedAt())
                .build();
    }
}
