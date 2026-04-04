package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.PropertyRepository;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import com.challenge_oracle.agrotech.gateways.requests.PropertyCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.PropertyUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.PropertyResponseDTO;
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
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Transactional
    public PropertyResponseDTO createProperty(PropertyCreateRequestDTO dto) {
        User owner = userRepository.findById(dto.getOwnerId())
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        if (propertyRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("Property title already exists");
        }

        Property property = dto.toProperty(owner);
        Property savedProperty = propertyRepository.save(property);

        return PropertyResponseDTO.fromProperty(savedProperty);
    }

    @Transactional(readOnly = true)
    public Page<PropertyResponseDTO> getAllProperties(Pageable pageable) {
        User user = getAuthenticatedUser();

        if (user.getRole() == Role.ADMIN) {
            return propertyRepository.findAll(pageable)
                    .map(PropertyResponseDTO::fromProperty);
        }

        return propertyRepository.findByOwnerOrMember(user.getId(), pageable)
                .map(PropertyResponseDTO::fromProperty);
    }

    @Transactional(readOnly = true)
    public PropertyResponseDTO getPropertyById(UUID id) {
        User user = getAuthenticatedUser();

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (user.getRole() != Role.ADMIN && !isOwnerOrMember(property, user)) {
            throw new AccessDeniedException("Access denied");
        }

        return PropertyResponseDTO.fromProperty(property);
    }

    @Transactional
    public PropertyResponseDTO updateProperty(UUID id, PropertyUpdateRequestDTO dto) {
        User user = getAuthenticatedUser();

        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        if (user.getRole() == Role.MANAGER && !isOwnerOrMember(property, user)) {
            throw new AccessDeniedException("Access denied");
        }

        if (dto.getTitle() != null && !property.getTitle().equals(dto.getTitle()) &&
                propertyRepository.existsByTitle(dto.getTitle())) {
            throw new IllegalArgumentException("Property title already exists");
        }

        dto.updateProperty(property);
        Property updatedProperty = propertyRepository.save(property);
        return PropertyResponseDTO.fromProperty(updatedProperty);
    }

    @Transactional
    public void deleteProperty(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));
        propertyRepository.delete(property);
    }

    private boolean isOwnerOrMember(Property property, User user) {
        if (property.getOwner().getId().equals(user.getId())) return true;
        return property.getMembers().stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));
    }
}