package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.QualityGrade;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record HarvestResponseDTO(
        UUID id,
        String harverstSeason,
        String crop,
        LocalDate plantingDate,
        LocalDate expectedHarvestDate,
        LocalDate actualStartDate,
        LocalDate actualEndDate,
        BigDecimal plantedArea,
        BigDecimal harvestedArea,
        BigDecimal expectedYield,
        BigDecimal actualYield,
        BigDecimal yieldPerHectare,
        QualityGrade qualityGrade,
        BigDecimal marketPricePerTon,
        BigDecimal totalRevenue,
        BigDecimal productionCost,
        BigDecimal profitMargin,
        String harvestNotes,
        String weatherConditions,
        HarvestStatus status,
        UUID fieldId,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static HarvestResponseDTO fromHarvest(Harvest harvest) {
        return new HarvestResponseDTO(
                harvest.getId(),
                harvest.getHarverstSeason(),
                harvest.getCrop(),
                harvest.getPlantingDate(),
                harvest.getExpectedHarvestDate(),
                harvest.getActualStartDate(),
                harvest.getActualEndDate(),
                harvest.getPlantedArea(),
                harvest.getHarvestedArea(),
                harvest.getExpectedYield(),
                harvest.getActualYield(),
                harvest.getYieldPerHectare(),
                harvest.getQualityGrade(),
                harvest.getMarketPricePerTon(),
                harvest.getTotalRevenue(),
                harvest.getProductionCost(),
                harvest.getProfitMargin(),
                harvest.getHarvestNotes(),
                harvest.getWeatherConditions(),
                harvest.getStatus(),
                harvest.getField() != null ? harvest.getField().getId() : null,
                harvest.getCreatedAt(),
                harvest.getUpdatedAt()
        );
    }
}
