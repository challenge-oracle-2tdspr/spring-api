package com.challenge_oracle.agrotech.gateways.controllers;

import com.challenge_oracle.agrotech.assemblers.FieldModelAssembler;
import com.challenge_oracle.agrotech.gateways.requests.FieldCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.FieldUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.FieldResponseDTO;
import com.challenge_oracle.agrotech.services.FieldService;
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
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fields")
@Tag(name = "Fields", description = "Field managing endpoints")
public class FieldController {

    private final FieldService fieldService;
    private final FieldModelAssembler fieldModelAssembler;

    @PostMapping
    @Operation(summary = "Create new field")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Field created"),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Property not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Field title already exists for this property", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FieldResponseDTO> createField(
            @Valid
            @RequestBody
            FieldCreateRequestDTO dto
    ) {
        FieldResponseDTO field = fieldService.createField(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(fieldModelAssembler.toModel(field));
    }

    @GetMapping
    @Operation(summary = "Fetch all fields with pagination")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fields fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PagedModel<FieldResponseDTO>> getAllFields(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<FieldResponseDTO> pagedAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FieldResponseDTO> fields = fieldService.getAllFields(pageable);

        PagedModel<FieldResponseDTO> pagedModel = pagedAssembler.toModel(fields, fieldModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/property/{propertyId}")
    @Operation(summary = "Fetch all fields from a property")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Fields fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid pagination parameters", content = @Content),
            @ApiResponse(responseCode = "404", description = "Property not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<PagedModel<FieldResponseDTO>> getFieldsByProperty(
            @PathVariable UUID propertyId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            PagedResourcesAssembler<FieldResponseDTO> pagedAssembler
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<FieldResponseDTO> fields = fieldService.getFieldsByProperty(propertyId, pageable);

        PagedModel<FieldResponseDTO> pagedModel = pagedAssembler.toModel(fields, fieldModelAssembler);
        return ResponseEntity.ok(pagedModel);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Fetch one field by its id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Field fetched"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FieldResponseDTO> getFieldById(@PathVariable UUID id) {
        FieldResponseDTO field = fieldService.getFieldById(id);
        return ResponseEntity.ok(fieldModelAssembler.toModel(field));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update field")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Field updated"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or validation error", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "409", description = "Field title already exists for this property", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<FieldResponseDTO> updateField(
            @PathVariable UUID id,
            @Valid
            @RequestBody
            FieldUpdateRequestDTO dto
    ) {
        FieldResponseDTO field = fieldService.updateField(id, dto);
        return ResponseEntity.ok(fieldModelAssembler.toModel(field));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete field")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Field deleted"),
            @ApiResponse(responseCode = "400", description = "Invalid UUID format", content = @Content),
            @ApiResponse(responseCode = "404", description = "Field not found", content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content)
    })
    public ResponseEntity<Void> deleteField(@PathVariable UUID id) {
        fieldService.deleteField(id);
        return ResponseEntity.noContent().build();
    }
}
