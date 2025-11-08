package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.domains.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public interface SensorReadingRepository extends JpaRepository<SensorReading, UUID> {
    Page<SensorReading> findBySensor(Sensor sensor, Pageable pageable);

    Page<SensorReading> findBySensorAndReadingTimeBetween(
            Sensor sensor,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    );
}
