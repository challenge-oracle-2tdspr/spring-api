package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.domains.SensorReading;
import com.challenge_oracle.agrotech.gateways.repositories.SensorReadingRepository;
import com.challenge_oracle.agrotech.gateways.repositories.SensorRepository;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    public SensorReadingResponseDTO createSensorReading(SensorReadingCreateRequestDTO dto) {
        Sensor sensor = sensorRepository.findById(dto.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        SensorReading reading = dto.toSensorReading(sensor);
        SensorReading savedReading = sensorReadingRepository.save(reading);

        return SensorReadingResponseDTO.fromSensorReading(savedReading);
    }

    public Page<SensorReadingResponseDTO> getAllSensorReadings(Pageable pageable) {
        Page<SensorReading> readings = sensorReadingRepository.findAll(pageable);
        return readings.map(SensorReadingResponseDTO::fromSensorReading);
    }

    public Page<SensorReadingResponseDTO> getReadingsBySensor(UUID sensorId, Pageable pageable) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        Page<SensorReading> readings = sensorReadingRepository.findBySensor(sensor, pageable);
        return readings.map(SensorReadingResponseDTO::fromSensorReading);
    }

    public Page<SensorReadingResponseDTO> getReadingsBySensorAndDateRange(
            UUID sensorId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    ) {
        Sensor sensor = sensorRepository.findById(sensorId)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        Page<SensorReading> readings = sensorReadingRepository.findBySensorAndReadingTimeBetween(
                sensor,
                startTime,
                endTime,
                pageable
        );
        return readings.map(SensorReadingResponseDTO::fromSensorReading);
    }

    public SensorReadingResponseDTO getReadingById(UUID id) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor reading not found"));

        return SensorReadingResponseDTO.fromSensorReading(reading);
    }

    public SensorReadingResponseDTO updateReading(UUID id, SensorReadingUpdateRequestDTO dto) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor reading not found"));

        dto.updateReading(reading);
        SensorReading updatedReading = sensorReadingRepository.save(reading);
        return SensorReadingResponseDTO.fromSensorReading(updatedReading);
    }

    public void deleteReading(UUID id) {
        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor reading not found"));
        sensorReadingRepository.delete(reading);
    }
}
