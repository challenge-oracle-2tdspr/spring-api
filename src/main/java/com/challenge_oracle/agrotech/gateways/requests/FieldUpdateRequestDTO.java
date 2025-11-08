package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import com.challenge_oracle.agrotech.enums.IrrigationType;
import com.challenge_oracle.agrotech.enums.SoilType;
import jakarta.validation.constraints.Min;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class FieldUpdateRequestDTO {
    private String title;

    private String description;

    private String crop;

    @Min(value = 0, message = "fieldArea must be greater than 0")
    private BigDecimal fieldArea;

    private AreaUnit areaUnit;

    private SoilType soilType;

    private IrrigationType irrigationType;

    public void updateField(Field field) {
        if (this.title != null) {
            field.setTitle(this.title);
        }
        if (this.description != null) {
            field.setDescription(this.description);
        }
        if (this.crop != null) {
            field.setCrop(this.crop);
        }
        if (this.fieldArea != null) {
            field.setFieldArea(this.fieldArea);
        }
        if (this.areaUnit != null) {
            field.setAreaUnit(this.areaUnit);
        }
        if (this.soilType != null) {
            field.setSoilType(this.soilType);
        }
        if (this.irrigationType != null) {
            field.setIrrigationType(this.irrigationType);
        }
    }
}
