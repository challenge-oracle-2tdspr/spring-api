package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SensorRepository extends JpaRepository<Sensor, UUID> {
    Page<Sensor> findByField(Field field, Pageable pageable);

    Page<Sensor> findByStatus(SensorStatus status, Pageable pageable);

    Optional<Sensor> findBySensorCode(String sensorCode);

    boolean existsBySensorCode(String sensorCode);
}
