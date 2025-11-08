package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.enums.AreaUnit;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PropertyResponseDTO (
        UUID id,
        String title,
        String description,
        BigDecimal totalArea,
        AreaUnit areaUnit,
        String address,
        String city,
        String state,
        String country,
        String zipCode,
        UUID ownerId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static PropertyResponseDTO fromProperty(Property property) {
        return new PropertyResponseDTO(
                property.getId(),
                property.getTitle(),
                property.getDescription(),
                property.getTotalArea(),
                property.getAreaUnit(),
                property.getAddress(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getZipCode(),
                property.getOwner() != null ? property.getOwner().getId() : null,
                property.getCreatedAt(),
                property.getUpdatedAt()
        );
    }
}
