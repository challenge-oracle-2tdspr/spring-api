package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.gateways.repositories.PropertyRepository;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import com.challenge_oracle.agrotech.gateways.requests.PropertyCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.PropertyUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.PropertyResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final UserRepository userRepository;

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
        Page<Property> properties = propertyRepository.findAll(pageable);
        return properties.map(PropertyResponseDTO::fromProperty);
    }

    @Transactional(readOnly = true)
    public PropertyResponseDTO getPropertyById(UUID id) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

        return PropertyResponseDTO.fromProperty(property);
    }

    @Transactional
    public PropertyResponseDTO updateProperty(UUID id, PropertyUpdateRequestDTO dto) {
        Property property = propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found"));

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
}
