package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.enums.SensorStatus;
import com.challenge_oracle.agrotech.gateways.requests.SensorCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.SensorUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.SensorResponseDTO;
import com.challenge_oracle.agrotech.services.SensorService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sensors")
@Tag(name = "Sensors", description = "Sensor managing endpoints")
public class SensorController {

    private final SensorService sensorService;

    @PostMapping
    @Operation(summary = "Create new sensor")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sensor created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Sensor code already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorResponseDTO> createSensor(
            @Valid
            @RequestBody
            SensorCreateRequestDTO dto
    ) {
        SensorResponseDTO sensor = sensorService.createSensor(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(sensor);
    }

    @GetMapping
    @Operation(summary = "Fetch all sensors with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensors fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorResponseDTO>> getAllSensors(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorResponseDTO> sensors = sensorService.getAllSensors(pageable);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/field/{fieldId}")
    @Operation(summary = "Fetch all sensors from a field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensors fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorResponseDTO>> getSensorsByField(
            @PathVariable UUID fieldId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorResponseDTO> sensors = sensorService.getSensorsByField(fieldId, pageable);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Fetch sensors by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensors fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid status or pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<SensorResponseDTO>> getSensorsByStatus(
            @PathVariable SensorStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<SensorResponseDTO> sensors = sensorService.getSensorsByStatus(status, pageable);
        return ResponseEntity.ok(sensors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one sensor by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorResponseDTO> getSensorById(@PathVariable UUID id) {
        SensorResponseDTO sensor = sensorService.getSensorById(id);
        return ResponseEntity.ok(sensor);
    }

    @GetMapping("/code/{sensorCode}")
    @Operation(summary = "Fetch sensor by sensor code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor fetched"),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorResponseDTO> getSensorBySensorCode(@PathVariable String sensorCode) {
        SensorResponseDTO sensor = sensorService.getSensorBySensorCode(sensorCode);
        return ResponseEntity.ok(sensor);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update sensor")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Sensor updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Sensor code already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<SensorResponseDTO> updateSensor(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            SensorUpdateRequestDTO dto
    ) {
        SensorResponseDTO sensor = sensorService.updateSensor(id, dto);
        return ResponseEntity.ok(sensor);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete sensor")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Sensor deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Sensor not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteSensor(@PathVariable UUID id) {
        sensorService.deleteSensor(id);
        return ResponseEntity.noContent().build();
    }
}
