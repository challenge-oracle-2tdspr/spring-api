package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProfilePropertyDTO extends RepresentationModel<ProfilePropertyDTO> {

    private UUID id;
    private String title;
    private String city;
    private String state;
    private BigDecimal totalArea;
    private AreaUnit areaUnit;
    private boolean owner;

    public ProfilePropertyDTO(UUID id, String title, String city, String state,
                              BigDecimal totalArea, AreaUnit areaUnit, boolean owner) {
        this.id = id;
        this.title = title;
        this.city = city;
        this.state = state;
        this.totalArea = totalArea;
        this.areaUnit = areaUnit;
        this.owner = owner;
    }

    public static ProfilePropertyDTO fromProperty(Property property, UUID userId) {
        boolean isOwner = property.getOwner().getId().equals(userId);
        return new ProfilePropertyDTO(
                property.getId(),
                property.getTitle(),
                property.getCity(),
                property.getState(),
                property.getTotalArea(),
                property.getAreaUnit(),
                isOwner
        );
    }
}