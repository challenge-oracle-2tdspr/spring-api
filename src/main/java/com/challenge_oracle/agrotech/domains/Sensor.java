package com.challenge_oracle.agrotech.domains;

import com.challenge_oracle.agrotech.enums.SensorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "sensors")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "sensor_code", unique = true)
    private String sensorCode;

    private String model;

    private String manufacturer;

    @Column(name = "installation_date")
    private LocalDateTime installationDate;

    @Enumerated(EnumType.STRING)
    private SensorStatus status = SensorStatus.ACTIVE;

    @Column(name = "battery_level")
    private Integer batteryLevel;

    @Column(name = "last_maintenance")
    private LocalDateTime lastMaintenance;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @NotNull
    private Field field;

    @OneToMany(mappedBy = "sensor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SensorReading> readings;
}
