package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.enums.SensorStatus;
import com.challenge_oracle.agrotech.gateways.repositories.FieldRepository;
import com.challenge_oracle.agrotech.gateways.repositories.SensorRepository;
import com.challenge_oracle.agrotech.gateways.requests.SensorCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.SensorUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.SensorResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorService {

    private final SensorRepository sensorRepository;
    private final FieldRepository fieldRepository;

    public SensorResponseDTO createSensor(SensorCreateRequestDTO dto) {
        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (sensorRepository.existsBySensorCode(dto.getSensorCode())) {
            throw new IllegalArgumentException("Sensor code already exists");
        }

        Sensor sensor = dto.toSensor(field);
        Sensor savedSensor = sensorRepository.save(sensor);

        return SensorResponseDTO.fromSensor(savedSensor);
    }

    public Page<SensorResponseDTO> getAllSensors(Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findAll(pageable);
        return sensors.map(SensorResponseDTO::fromSensor);
    }

    public Page<SensorResponseDTO> getSensorsByField(UUID fieldId, Pageable pageable) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        Page<Sensor> sensors = sensorRepository.findByField(field, pageable);
        return sensors.map(SensorResponseDTO::fromSensor);
    }

    public Page<SensorResponseDTO> getSensorsByStatus(SensorStatus status, Pageable pageable) {
        Page<Sensor> sensors = sensorRepository.findByStatus(status, pageable);
        return sensors.map(SensorResponseDTO::fromSensor);
    }

    public SensorResponseDTO getSensorById(UUID id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        return SensorResponseDTO.fromSensor(sensor);
    }

    public SensorResponseDTO getSensorBySensorCode(String sensorCode) {
        Sensor sensor = sensorRepository.findBySensorCode(sensorCode)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        return SensorResponseDTO.fromSensor(sensor);
    }

    public SensorResponseDTO updateSensor(UUID id, SensorUpdateRequestDTO dto) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        if (dto.getSensorCode() != null && !sensor.getSensorCode().equals(dto.getSensorCode()) &&
                sensorRepository.existsBySensorCode(dto.getSensorCode())) {
            throw new IllegalArgumentException("Sensor code already exists");
        }

        dto.updateSensor(sensor);
        Sensor updatedSensor = sensorRepository.save(sensor);
        return SensorResponseDTO.fromSensor(updatedSensor);
    }

    public void deleteSensor(UUID id) {
        Sensor sensor = sensorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor not found"));
        sensorRepository.delete(sensor);
    }
}
