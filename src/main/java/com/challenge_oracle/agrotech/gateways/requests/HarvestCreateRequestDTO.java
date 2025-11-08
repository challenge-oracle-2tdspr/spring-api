package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.QualityGrade;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class HarvestCreateRequestDTO {
    private String harverstSeason;

    @NotBlank(message = "crop is required")
    private String crop;

    @NotNull(message = "plantingDate is required")
    private LocalDate plantingDate;

    private LocalDate expectedHarvestDate;

    @NotNull(message = "fieldId is required")
    private UUID fieldId;

    @DecimalMin(value = "0", message = "plantedArea must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "plantedArea must have at most 10 integer digits and 2 decimal places")
    private BigDecimal plantedArea;

    @DecimalMin(value = "0", message = "expectedYield must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "expectedYield must have at most 10 integer digits and 2 decimal places")
    private BigDecimal expectedYield;

    private QualityGrade qualityGrade;

    @DecimalMin(value = "0", message = "marketPricePerTon must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "marketPricePerTon must have at most 10 integer digits and 2 decimal places")
    private BigDecimal marketPricePerTon;

    @DecimalMin(value = "0", message = "productionCost must be greater than or equal to 0")
    @Digits(integer = 12, fraction = 2, message = "productionCost must have at most 12 integer digits and 2 decimal places")
    private BigDecimal productionCost;

    private String harvestNotes;

    private String weatherConditions;

    public Harvest toHarvest(Field field) {
        return Harvest.builder()
                .harverstSeason(this.harverstSeason)
                .crop(this.crop)
                .plantingDate(this.plantingDate)
                .expectedHarvestDate(this.expectedHarvestDate)
                .plantedArea(this.plantedArea)
                .expectedYield(this.expectedYield)
                .qualityGrade(this.qualityGrade)
                .marketPricePerTon(this.marketPricePerTon)
                .productionCost(this.productionCost)
                .harvestNotes(this.harvestNotes)
                .weatherConditions(this.weatherConditions)
                .status(HarvestStatus.PLANNED)
                .field(field)
                .build();
    }
}
