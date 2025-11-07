package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class PropertyCreateRequestDTO {
    @NotBlank
    private String title;
    private String description;
    @NotNull
    @Min(0)
    private BigDecimal totalArea;
    @NotNull
    private AreaUnit areaUnit;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;
    @NotNull
    private UUID ownerId;

    public Property toProperty(User owner) {
        return Property.builder()
            .title(this.title)
            .description(this.description)
            .totalArea(this.totalArea)
            .areaUnit(this.areaUnit)
            .address(this.address)
            .city(this.city)
            .state(this.state)
            .country(this.country)
            .zipCode(this.zipCode)
            .owner(owner)
            .build();
    }
}
