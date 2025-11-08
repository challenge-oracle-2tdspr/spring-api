package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.gateways.requests.HarvestCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.HarvestUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.HarvestResponseDTO;
import com.challenge_oracle.agrotech.services.HarvestService;
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
@RequestMapping("/api/harvests")
@Tag(name = "Harvests", description = "Harvest managing endpoints")
public class HarvestController {

    private final HarvestService harvestService;

    @PostMapping
    @Operation(summary = "Create new harvest")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Harvest created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<HarvestResponseDTO> createHarvest(
            @Valid
            @RequestBody
            HarvestCreateRequestDTO dto
    ) {
        HarvestResponseDTO harvest = harvestService.createHarvest(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(harvest);
    }

    @GetMapping
    @Operation(summary = "Fetch all harvests with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Harvests fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<HarvestResponseDTO>> getAllHarvests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HarvestResponseDTO> harvests = harvestService.getAllHarvests(pageable);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/field/{fieldId}")
    @Operation(summary = "Fetch all harvests from a field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Harvests fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<HarvestResponseDTO>> getHarvestsByField(
            @PathVariable UUID fieldId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HarvestResponseDTO> harvests = harvestService.getHarvestsByField(fieldId, pageable);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Fetch harvests by status")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Harvests fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid status or pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<HarvestResponseDTO>> getHarvestsByStatus(
            @PathVariable HarvestStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<HarvestResponseDTO> harvests = harvestService.getHarvestsByStatus(status, pageable);
        return ResponseEntity.ok(harvests);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one harvest by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Harvest fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Harvest not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<HarvestResponseDTO> getHarvestById(@PathVariable UUID id) {
        HarvestResponseDTO harvest = harvestService.getHarvestById(id);
        return ResponseEntity.ok(harvest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update harvest")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Harvest updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Harvest not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<HarvestResponseDTO> updateHarvest(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            HarvestUpdateRequestDTO dto
    ) {
        HarvestResponseDTO harvest = harvestService.updateHarvest(id, dto);
        return ResponseEntity.ok(harvest);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete harvest")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Harvest deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Harvest not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteHarvest(@PathVariable UUID id) {
        harvestService.deleteHarvest(id);
        return ResponseEntity.noContent().build();
    }
}
