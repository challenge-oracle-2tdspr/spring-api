package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PropertyUpdateRequestDTO {
    private String title;
    private String description;
    private BigDecimal totalArea;
    private AreaUnit areaUnit;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipCode;

    public void updateProperty(Property property) {
        if (title != null) property.setTitle(title);
        if (description != null) property.setDescription(description);
        if (totalArea != null) property.setTotalArea(totalArea);
        if (areaUnit != null) property.setAreaUnit(areaUnit);
        if (address != null) property.setAddress(address);
        if (city != null) property.setCity(city);
        if (state != null) property.setState(state);
        if (country != null) property.setCountry(country);
        if (zipCode != null) property.setZipCode(zipCode);
    }
}
