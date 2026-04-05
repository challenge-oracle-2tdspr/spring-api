package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.domains.SensorReading;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    @Query("""
        SELECT r FROM SensorReading r
        WHERE r.sensor.field.id = :fieldId
    """)
    Page<SensorReading> findByFieldId(@Param("fieldId") UUID fieldId, Pageable pageable);

    @Query("""
        SELECT r FROM SensorReading r
        WHERE r.sensor.field.id = :fieldId
        AND r.readingTime BETWEEN :startTime AND :endTime
    """)
    Page<SensorReading> findByFieldIdAndDateRange(
            @Param("fieldId") UUID fieldId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            Pageable pageable
    );

    // filtrado por ownership (manager/user)
    @Query("""
        SELECT r FROM SensorReading r
        WHERE r.sensor.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<SensorReading> findAllByOwnerOrMember(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT r FROM SensorReading r
        WHERE r.sensor.field.id = :fieldId
        AND r.sensor.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<SensorReading> findByFieldIdAndOwnerOrMember(
            @Param("fieldId") UUID fieldId,
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query("""
        SELECT r FROM SensorReading r
        WHERE r.sensor.field.id = :fieldId
        AND r.readingTime BETWEEN :startTime AND :endTime
        AND r.sensor.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<SensorReading> findByFieldIdAndDateRangeAndOwnerOrMember(
            @Param("fieldId") UUID fieldId,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("userId") UUID userId,
            Pageable pageable
    );
}