package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.QualityGrade;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HarvestUpdateRequestDTO {
    private String harverstSeason;

    private String crop;

    private LocalDate expectedHarvestDate;

    private LocalDate actualStartDate;

    private LocalDate actualEndDate;

    @DecimalMin(value = "0", message = "harvestedArea must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "harvestedArea must have at most 10 integer digits and 2 decimal places")
    private BigDecimal harvestedArea;

    @DecimalMin(value = "0", message = "actualYield must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "actualYield must have at most 10 integer digits and 2 decimal places")
    private BigDecimal actualYield;

    @DecimalMin(value = "0", message = "yieldPerHectare must be greater than or equal to 0")
    @Digits(integer = 8, fraction = 2, message = "yieldPerHectare must have at most 8 integer digits and 2 decimal places")
    private BigDecimal yieldPerHectare;

    private QualityGrade qualityGrade;

    @DecimalMin(value = "0", message = "marketPricePerTon must be greater than or equal to 0")
    @Digits(integer = 10, fraction = 2, message = "marketPricePerTon must have at most 10 integer digits and 2 decimal places")
    private BigDecimal marketPricePerTon;

    @DecimalMin(value = "0", message = "totalRevenue must be greater than or equal to 0")
    @Digits(integer = 12, fraction = 2, message = "totalRevenue must have at most 12 integer digits and 2 decimal places")
    private BigDecimal totalRevenue;

    @DecimalMin(value = "0", message = "productionCost must be greater than or equal to 0")
    @Digits(integer = 12, fraction = 2, message = "productionCost must have at most 12 integer digits and 2 decimal places")
    private BigDecimal productionCost;

    private HarvestStatus status;

    private String harvestNotes;

    private String weatherConditions;

    public void updateHarvest(Harvest harvest) {
        if (this.harverstSeason != null) {
            harvest.setHarverstSeason(this.harverstSeason);
        }
        if (this.crop != null) {
            harvest.setCrop(this.crop);
        }
        if (this.expectedHarvestDate != null) {
            harvest.setExpectedHarvestDate(this.expectedHarvestDate);
        }
        if (this.actualStartDate != null) {
            harvest.setActualStartDate(this.actualStartDate);
        }
        if (this.actualEndDate != null) {
            harvest.setActualEndDate(this.actualEndDate);
        }
        if (this.harvestedArea != null) {
            harvest.setHarvestedArea(this.harvestedArea);
        }
        if (this.actualYield != null) {
            harvest.setActualYield(this.actualYield);
        }
        if (this.yieldPerHectare != null) {
            harvest.setYieldPerHectare(this.yieldPerHectare);
        }
        if (this.qualityGrade != null) {
            harvest.setQualityGrade(this.qualityGrade);
        }
        if (this.marketPricePerTon != null) {
            harvest.setMarketPricePerTon(this.marketPricePerTon);
        }
        if (this.totalRevenue != null) {
            harvest.setTotalRevenue(this.totalRevenue);
        }
        if (this.productionCost != null) {
            harvest.setProductionCost(this.productionCost);
        }
        if (this.status != null) {
            harvest.setStatus(this.status);
        }
        if (this.harvestNotes != null) {
            harvest.setHarvestNotes(this.harvestNotes);
        }
        if (this.weatherConditions != null) {
            harvest.setWeatherConditions(this.weatherConditions);
        }
    }
}
