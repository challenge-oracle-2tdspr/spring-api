package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.enums.AreaUnit;
import com.challenge_oracle.agrotech.enums.IrrigationType;
import com.challenge_oracle.agrotech.enums.SoilType;
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
public class FieldResponseDTO extends RepresentationModel<FieldResponseDTO> {
    private UUID id;
    private String title;
    private String description;
    private String crop;
    private BigDecimal fieldArea;
    private AreaUnit areaUnit;
    private SoilType soilType;
    private IrrigationType irrigationType;
    private UUID propertyId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static FieldResponseDTO fromField(Field field) {
        return FieldResponseDTO.builder()
                .id(field.getId())
                .title(field.getTitle())
                .description(field.getDescription())
                .crop(field.getCrop())
                .fieldArea(field.getFieldArea())
                .areaUnit(field.getAreaUnit())
                .soilType(field.getSoilType())
                .irrigationType(field.getIrrigationType())
                .propertyId(field.getProperty() != null ? field.getProperty().getId() : null)
                .createdAt(field.getCreatedAt())
                .updatedAt(field.getUpdatedAt())
                .build();
    }
}
