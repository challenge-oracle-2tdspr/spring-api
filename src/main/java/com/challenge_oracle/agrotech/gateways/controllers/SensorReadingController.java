package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.gateways.requests.SensorReadingCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import com.challenge_oracle.agrotech.services.SensorReadingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensor-readings")
@Tag(name = "Sensor Readings", description = "Sensor reading managing endpoints")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;

    @PostMapping
    @Operation(summary = "Create new sensor reading")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sensor reading created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorReadingResponseDTO> createSensorReading(
            @Valid
            @RequestBody
            SensorReadingCreateRequestDTO dto
    ) {
        SensorReadingResponseDTO reading = sensorReadingService.createSensorReading(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(reading);
    }

    @GetMapping
    @Operation(summary = "Fetch all sensor readings with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorReadingResponseDTO>> getAllSensorReadings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService.getAllSensorReadings(pageable);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/sensor/{sensorId}")
    @Operation(summary = "Fetch all readings from a sensor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorReadingResponseDTO>> getReadingsBySensor(
            @PathVariable UUID sensorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService.getReadingsBySensor(sensorId, pageable);
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/sensor/{sensorId}/range")
    @Operation(summary = "Fetch readings from a sensor within a date range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid parameters or date range", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorReadingResponseDTO>> getReadingsBySensorAndDateRange(
            @PathVariable UUID sensorId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService.getReadingsBySensorAndDateRange(
                sensorId,
                startTime,
                endTime,
                pageable
        );
        return ResponseEntity.ok(readings);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one sensor reading by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor reading fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorReadingResponseDTO> getReadingById(@PathVariable UUID id) {
        SensorReadingResponseDTO reading = sensorReadingService.getReadingById(id);
        return ResponseEntity.ok(reading);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sensor reading")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor reading updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorReadingResponseDTO> updateReading(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            SensorReadingUpdateRequestDTO dto
    ) {
        SensorReadingResponseDTO reading = sensorReadingService.updateReading(id, dto);
        return ResponseEntity.ok(reading);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor reading")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sensor reading deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteReading(@PathVariable UUID id) {
        sensorReadingService.deleteReading(id);
        return ResponseEntity.noContent().build();
    }
}
