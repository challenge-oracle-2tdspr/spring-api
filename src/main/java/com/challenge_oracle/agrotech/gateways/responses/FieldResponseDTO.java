package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import com.challenge_oracle.agrotech.enums.IrrigationType;
import com.challenge_oracle.agrotech.enums.SoilType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record FieldResponseDTO(
        UUID id,
        String title,
        String description,
        String crop,
        BigDecimal fieldArea,
        AreaUnit areaUnit,
        SoilType soilType,
        IrrigationType irrigationType,
        UUID propertyId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static FieldResponseDTO fromField(Field field) {
        return new FieldResponseDTO(
                field.getId(),
                field.getTitle(),
                field.getDescription(),
                field.getCrop(),
                field.getFieldArea(),
                field.getAreaUnit(),
                field.getSoilType(),
                field.getIrrigationType(),
                field.getProperty() != null ? field.getProperty().getId() : null,
                field.getCreatedAt(),
                field.getUpdatedAt()
        );
    }
}
