package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.FieldRepository;
import com.challenge_oracle.agrotech.gateways.repositories.PropertyRepository;
import com.challenge_oracle.agrotech.gateways.requests.FieldCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.FieldUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.FieldResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FieldService {

    private final FieldRepository fieldRepository;
    private final PropertyRepository propertyRepository;

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void checkManagerAccess(Field field, User user) {
        if (user.getRole() == Role.MANAGER) {
            boolean isOwnerOrMember = field.getProperty().getOwner().getId().equals(user.getId())
                    || field.getProperty().getMembers().stream()
                    .anyMatch(m -> m.getUser().getId().equals(user.getId()));
            if (!isOwnerOrMember) throw new AccessDeniedException("Access denied");
        }
    }

    @Transactional
    public FieldResponseDTO createField(FieldCreateRequestDTO dto) {
        User auth = getAuthenticatedUser();

        UUID propertyId;

        if (auth.getRole() == Role.ADMIN) {
            if (dto.getPropertyId() == null) {
                throw new IllegalArgumentException("propertyId is required for ADMIN");
            }
            propertyId = dto.getPropertyId();
        } else {
            if (dto.getPropertyId() == null) {
                throw new IllegalArgumentException("propertyId is required");
            }
            propertyId = dto.getPropertyId();
        }

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (auth.getRole() == Role.MANAGER) {
            boolean isOwnerOrMember = property.getOwner().getId().equals(auth.getId())
                    || property.getMembers().stream()
                    .anyMatch(m -> m.getUser().getId().equals(auth.getId()));
            if (!isOwnerOrMember) throw new AccessDeniedException("Access denied for this property");
        }

        if (fieldRepository.existsByTitleAndProperty(dto.getTitle(), property)) {
            throw new IllegalArgumentException("Field title already exists for this property");
        }

        Field field = dto.toField(property);
        return FieldResponseDTO.fromField(fieldRepository.save(field));
    }

    @Transactional(readOnly = true)
    public Page<FieldResponseDTO> getAllFields(Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return fieldRepository.findAll(pageable).map(FieldResponseDTO::fromField);
        }

        return fieldRepository.findAllByOwnerOrMember(auth.getId(), pageable)
                .map(FieldResponseDTO::fromField);
    }

    @Transactional(readOnly = true)
    public Page<FieldResponseDTO> getFieldsByProperty(UUID propertyId, Pageable pageable) {
        User auth = getAuthenticatedUser();

        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (auth.getRole() == Role.ADMIN) {
            Property property = propertyRepository.findById(propertyId).get();
            return fieldRepository.findByProperty(property, pageable)
                    .map(FieldResponseDTO::fromField);
        }

        return fieldRepository.findByPropertyAndOwnerOrMember(propertyId, auth.getId(), pageable)
                .map(FieldResponseDTO::fromField);
    }

    @Transactional(readOnly = true)
    public FieldResponseDTO getFieldById(UUID id) {
        User auth = getAuthenticatedUser();

        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (auth.getRole() != Role.ADMIN) {
            checkManagerAccess(field, auth);
        }

        return FieldResponseDTO.fromField(field);
    }

    @Transactional
    public FieldResponseDTO updateField(UUID id, FieldUpdateRequestDTO dto) {
        User auth = getAuthenticatedUser();

        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        checkManagerAccess(field, auth);

        if (dto.getTitle() != null && !field.getTitle().equals(dto.getTitle()) &&
                fieldRepository.existsByTitleAndProperty(dto.getTitle(), field.getProperty())) {
            throw new IllegalArgumentException("Field title already exists for this property");
        }

        dto.updateField(field);
        return FieldResponseDTO.fromField(fieldRepository.save(field));
    }

    @Transactional
    public void deleteField(UUID id) {
        User auth = getAuthenticatedUser();

        Field field = fieldRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        fieldRepository.delete(field);
    }
}