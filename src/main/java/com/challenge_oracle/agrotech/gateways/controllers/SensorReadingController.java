package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.assemblers.SensorReadingModelAssembler;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingCreateRequestDTO;
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
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensor-readings")
@Tag(name = "Sensor Readings", description = "Sensor reading managing endpoints")
public class SensorReadingController {

    private final SensorReadingService sensorReadingService;
    private final SensorReadingModelAssembler sensorReadingModelAssembler;

    @PostMapping
    @PreAuthorize("hasRole('WEBHOOK')")
    @Operation(summary = "Create new sensor reading (webhook only)")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sensor reading created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "401", description = "Missing or invalid API Key", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorReadingResponseDTO> createSensorReading(
            @Valid @RequestBody SensorReadingCreateRequestDTO dto
    ) {
        SensorReadingResponseDTO reading = sensorReadingService.createSensorReading(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensorReadingModelAssembler.toModel(reading));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @Operation(summary = "Fetch all sensor readings with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PagedModel<SensorReadingResponseDTO>> getAllSensorReadings(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<SensorReadingResponseDTO> pagedAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService.getAllSensorReadings(pageable);
        PagedModel<SensorReadingResponseDTO> pagedModel = pagedAssembler.toModel(readings, sensorReadingModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/field/{fieldId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @Operation(summary = "Fetch all readings from a field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PagedModel<SensorReadingResponseDTO>> getReadingsByField(
            @PathVariable UUID fieldId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<SensorReadingResponseDTO> pagedAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService.getReadingsByField(fieldId, pageable);
        PagedModel<SensorReadingResponseDTO> pagedModel = pagedAssembler.toModel(readings, sensorReadingModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/field/{fieldId}/range")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @Operation(summary = "Fetch readings from a field within a date range")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor readings fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid date range", content = @Content),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PagedModel<SensorReadingResponseDTO>> getReadingsByFieldAndDateRange(
            @PathVariable UUID fieldId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<SensorReadingResponseDTO> pagedAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorReadingResponseDTO> readings = sensorReadingService
                .getReadingsByFieldAndDateRange(fieldId, startTime, endTime, pageable);
        PagedModel<SensorReadingResponseDTO> pagedModel = pagedAssembler.toModel(readings, sensorReadingModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER', 'USER')")
    @Operation(summary = "Fetch one sensor reading by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor reading fetched"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor reading not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorReadingResponseDTO> getReadingById(@PathVariable UUID id) {
        SensorReadingResponseDTO reading = sensorReadingService.getReadingById(id);
        return ResponseEntity.ok(sensorReadingModelAssembler.toModel(reading));
    }
}