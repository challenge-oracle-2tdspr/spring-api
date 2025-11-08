package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.gateways.requests.PropertyCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.PropertyUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.PropertyResponseDTO;
import com.challenge_oracle.agrotech.services.PropertyService;
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
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/properties")
@Tag(name = "Properties", description = "Property managing endpoints")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping
    @Operation(summary = "Create new property")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Property created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Owner not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Property title already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PropertyResponseDTO> createProperty(
            @Valid
            @RequestBody
            PropertyCreateRequestDTO dto
    ) {
        PropertyResponseDTO property = propertyService.createProperty(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(property);
    }

    @GetMapping
    @Operation(summary = "Fetch all properties with pagination and sorting")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Properties fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Page<PropertyResponseDTO>> getAllProperties(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort.Direction direction = sortDirection.equalsIgnoreCase("ASC")
                ? Sort.Direction.ASC
                : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));
        Page<PropertyResponseDTO> properties = propertyService.getAllProperties(pageable);
        return ResponseEntity.ok(properties);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one property by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Property fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Property not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PropertyResponseDTO> getPropertyById(@PathVariable UUID id) {
        PropertyResponseDTO property = propertyService.getPropertyById(id);
        return ResponseEntity.ok(property);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update property")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Property updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Property not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Property title already exists", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PropertyResponseDTO> updateProperty(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            PropertyUpdateRequestDTO dto
    ) {
        PropertyResponseDTO property = propertyService.updateProperty(id, dto);
        return ResponseEntity.ok(property);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete property")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Property deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Property not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteProperty(@PathVariable UUID id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.noContent().build();
    }
}
