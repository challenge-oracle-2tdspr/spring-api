package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.QualityGrade;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class HarvestResponseDTO extends RepresentationModel<HarvestResponseDTO> {
    private UUID id;
    private String harverstSeason;
    private String crop;
    private LocalDate plantingDate;
    private LocalDate expectedHarvestDate;
    private LocalDate actualStartDate;
    private LocalDate actualEndDate;
    private BigDecimal plantedArea;
    private BigDecimal harvestedArea;
    private BigDecimal expectedYield;
    private BigDecimal actualYield;
    private BigDecimal yieldPerHectare;
    private QualityGrade qualityGrade;
    private BigDecimal marketPricePerTon;
    private BigDecimal totalRevenue;
    private BigDecimal productionCost;
    private BigDecimal profitMargin;
    private String harvestNotes;
    private String weatherConditions;
    private HarvestStatus status;
    private UUID fieldId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static HarvestResponseDTO fromHarvest(Harvest harvest) {
        return HarvestResponseDTO.builder()
                .id(harvest.getId())
                .harverstSeason(harvest.getHarverstSeason())
                .crop(harvest.getCrop())
                .plantingDate(harvest.getPlantingDate())
                .expectedHarvestDate(harvest.getExpectedHarvestDate())
                .actualStartDate(harvest.getActualStartDate())
                .actualEndDate(harvest.getActualEndDate())
                .plantedArea(harvest.getPlantedArea())
                .harvestedArea(harvest.getHarvestedArea())
                .expectedYield(harvest.getExpectedYield())
                .actualYield(harvest.getActualYield())
                .yieldPerHectare(harvest.getYieldPerHectare())
                .qualityGrade(harvest.getQualityGrade())
                .marketPricePerTon(harvest.getMarketPricePerTon())
                .totalRevenue(harvest.getTotalRevenue())
                .productionCost(harvest.getProductionCost())
                .profitMargin(harvest.getProfitMargin())
                .harvestNotes(harvest.getHarvestNotes())
                .weatherConditions(harvest.getWeatherConditions())
                .status(harvest.getStatus())
                .fieldId(harvest.getField() != null ? harvest.getField().getId() : null)
                .createdAt(harvest.getCreatedAt())
                .updatedAt(harvest.getUpdatedAt())
                .build();
    }
}
