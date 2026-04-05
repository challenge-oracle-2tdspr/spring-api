package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Sensor;
import com.challenge_oracle.agrotech.domains.SensorReading;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.SensorReadingRepository;
import com.challenge_oracle.agrotech.gateways.repositories.SensorRepository;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SensorReadingService {

    private final SensorReadingRepository sensorReadingRepository;
    private final SensorRepository sensorRepository;

    private boolean isWebhook() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_WEBHOOK"));
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Transactional
    public SensorReadingResponseDTO createSensorReading(SensorReadingCreateRequestDTO dto) {
        Sensor sensor = sensorRepository.findById(dto.getSensorId())
                .orElseThrow(() -> new RuntimeException("Sensor not found"));

        SensorReading reading = dto.toSensorReading(sensor);
        return SensorReadingResponseDTO.fromSensorReading(sensorReadingRepository.save(reading));
    }

    @Transactional(readOnly = true)
    public Page<SensorReadingResponseDTO> getAllSensorReadings(Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return sensorReadingRepository.findAll(pageable)
                    .map(SensorReadingResponseDTO::fromSensorReading);
        }

        return sensorReadingRepository.findAllByOwnerOrMember(auth.getId(), pageable)
                .map(SensorReadingResponseDTO::fromSensorReading);
    }

    @Transactional(readOnly = true)
    public Page<SensorReadingResponseDTO> getReadingsByField(UUID fieldId, Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return sensorReadingRepository.findByFieldId(fieldId, pageable)
                    .map(SensorReadingResponseDTO::fromSensorReading);
        }

        return sensorReadingRepository.findByFieldIdAndOwnerOrMember(fieldId, auth.getId(), pageable)
                .map(SensorReadingResponseDTO::fromSensorReading);
    }

    @Transactional(readOnly = true)
    public Page<SensorReadingResponseDTO> getReadingsByFieldAndDateRange(
            UUID fieldId,
            LocalDateTime startTime,
            LocalDateTime endTime,
            Pageable pageable
    ) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return sensorReadingRepository.findByFieldIdAndDateRange(fieldId, startTime, endTime, pageable)
                    .map(SensorReadingResponseDTO::fromSensorReading);
        }

        return sensorReadingRepository.findByFieldIdAndDateRangeAndOwnerOrMember(
                        fieldId, startTime, endTime, auth.getId(), pageable)
                .map(SensorReadingResponseDTO::fromSensorReading);
    }

    @Transactional(readOnly = true)
    public SensorReadingResponseDTO getReadingById(UUID id) {
        User auth = getAuthenticatedUser();

        SensorReading reading = sensorReadingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Sensor reading not found"));

        if (auth.getRole() != Role.ADMIN) {
            boolean hasAccess = reading.getSensor().getField().getProperty().getOwner().getId().equals(auth.getId())
                    || reading.getSensor().getField().getProperty().getMembers().stream()
                    .anyMatch(m -> m.getUser().getId().equals(auth.getId()));
            if (!hasAccess) throw new AccessDeniedException("Access denied");
        }

        return SensorReadingResponseDTO.fromSensorReading(reading);
    }
}