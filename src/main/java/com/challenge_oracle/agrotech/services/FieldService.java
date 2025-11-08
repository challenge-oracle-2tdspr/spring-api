package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.gateways.repositories.FieldRepository;
import com.challenge_oracle.agrotech.gateways.repositories.PropertyRepository;
import com.challenge_oracle.agrotech.gateways.requests.FieldCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.FieldUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.FieldResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;
    private final PropertyRepository propertyRepository;

    public FieldResponseDTO createField(FieldCreateRequestDTO dto) {
        Property property = propertyRepository.findById(dto.getPropertyId())
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (fieldRepository.existsByTitleAndProperty(dto.getTitle(), property)) {
            throw new IllegalArgumentException("Field title already exists for this property");
        }

        Field field = dto.toField(property);
        Field savedField = fieldRepository.save(field);

        return FieldResponseDTO.fromField(savedField);
    }

    public Page<FieldResponseDTO> getAllFields(Pageable pageable) {
        Page<Field> fields = fieldRepository.findAll(pageable);
        return fields.map(FieldResponseDTO::fromField);
    }

    public Page<FieldResponseDTO> getFieldsByProperty(UUID propertyId, Pageable pageable) {
        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        Page<Field> fields = fieldRepository.findByProperty(property, pageable);
        return fields.map(FieldResponseDTO::fromField);
    }

    public FieldResponseDTO getFieldById(UUID id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        return FieldResponseDTO.fromField(field);
    }

    public FieldResponseDTO updateField(UUID id, FieldUpdateRequestDTO dto) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (dto.getTitle() != null && !field.getTitle().equals(dto.getTitle()) &&
                fieldRepository.existsByTitleAndProperty(dto.getTitle(), field.getProperty())) {
            throw new IllegalArgumentException("Field title already exists for this property");
        }

        dto.updateField(field);
        Field updatedField = fieldRepository.save(field);
        return FieldResponseDTO.fromField(updatedField);
    }

    public void deleteField(UUID id) {
        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));
        fieldRepository.delete(field);
    }
}
