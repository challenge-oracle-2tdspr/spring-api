package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SensorUpdateRequestDTO {
    @Size(min = 3, max = 50, message = "sensorCode must be between 3 and 50 characters")
    private String sensorCode;

    private String model;

    private String manufacturer;

    private LocalDateTime installationDate;

    @Min(value = 0, message = "batteryLevel must be greater than or equal to 0")
    @Max(value = 100, message = "batteryLevel must be less than or equal to 100")
    private Integer batteryLevel;

    private LocalDateTime lastMaintenance;

    private SensorStatus status;

    public void updateSensor(Sensor sensor) {
        if (this.sensorCode != null) {
            sensor.setSensorCode(this.sensorCode);
        }
        if (this.model != null) {
            sensor.setModel(this.model);
        }
        if (this.manufacturer != null) {
            sensor.setManufacturer(this.manufacturer);
        }
        if (this.installationDate != null) {
            sensor.setInstallationDate(this.installationDate);
        }
        if (this.batteryLevel != null) {
            sensor.setBatteryLevel(this.batteryLevel);
        }
        if (this.lastMaintenance != null) {
            sensor.setLastMaintenance(this.lastMaintenance);
        }
        if (this.status != null) {
            sensor.setStatus(this.status);
        }
    }
}
