package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import com.challenge_oracle.agrotech.enums.IrrigationType;
import com.challenge_oracle.agrotech.enums.SoilType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FieldCreateRequestDTO {
    @NotBlank(message = "title is required")
    private String title;

    private String description;

    @NotBlank(message = "crop is required")
    private String crop;

    @NotNull(message = "fieldArea is required")
    @Min(value = 0, message = "fieldArea must be greater than 0")
    private BigDecimal fieldArea;

    private AreaUnit areaUnit = AreaUnit.HECTARES;

    private SoilType soilType;

    private IrrigationType irrigationType;

    @NotNull(message = "propertyId is required")
    private UUID propertyId;

    public Field toField(Property property) {
        return Field.builder()
                .title(this.title)
                .description(this.description)
                .crop(this.crop)
                .fieldArea(this.fieldArea)
                .areaUnit(this.areaUnit != null ? this.areaUnit : AreaUnit.HECTARES)
                .soilType(this.soilType)
                .irrigationType(this.irrigationType)
                .property(property)
                .build();
    }
}
