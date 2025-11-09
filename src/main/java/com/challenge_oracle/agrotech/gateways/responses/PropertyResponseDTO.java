package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class PropertyResponseDTO extends RepresentationModel<PropertyResponseDTO> {
    private UUID id;
    private String title;
    private String description;
    private BigDecimal totalArea;
    private AreaUnit areaUnit;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    private UUID ownerId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static PropertyResponseDTO fromProperty(Property property) {
        return PropertyResponseDTO.builder()
                .id(property.getId())
                .title(property.getTitle())
                .description(property.getDescription())
                .totalArea(property.getTotalArea())
                .areaUnit(property.getAreaUnit())
                .address(property.getAddress())
                .city(property.getCity())
                .state(property.getState())
                .country(property.getCountry())
                .zipCode(property.getZipCode())
                .ownerId(property.getOwner() != null ? property.getOwner().getId() : null)
                .createdAt(property.getCreatedAt())
                .updatedAt(property.getUpdatedAt())
                .build();
    }
}
