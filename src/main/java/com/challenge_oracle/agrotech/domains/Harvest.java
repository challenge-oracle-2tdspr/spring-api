package com.challenge_oracle.agrotech.domains;

import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.QualityGrade;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
@Table(name = "harvests")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Harvest {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "harverst_season")
    private String harverstSeason;

    @NotNull
    private String crop;

    @Column(name = "planting_date")
    @NotNull
    private LocalDate plantingDate;

    @Column(name = "expected_harvest_date")
    private LocalDate expectedHarvestDate;

    @Column(name = "actual_start_date")
    private LocalDate actualStartDate;

    @Column(name = "actual_end_date")
    private LocalDate actualEndDate;

    @Column(name = "planted_area", precision = 10, scale = 2)
    private BigDecimal plantedArea; // hectares

    @Column(name = "harvested_area", precision = 10, scale = 2)
    private BigDecimal harvestedArea; // hectares

    @Column(name = "expected_yield", precision = 10, scale = 2)
    private BigDecimal expectedYield; // tons

    @Column(name = "actual_yield", precision = 10, scale = 2)
    private BigDecimal actualYield; // tons

    @Column(name = "yield_per_hectare", precision = 8, scale = 2)
    private BigDecimal yieldPerHectare;

    @Column(name = "quality_grade")
    @Enumerated(EnumType.STRING)
    private QualityGrade qualityGrade;

    @Column(name = "market_price_per_ton", precision = 10, scale = 2)
    private BigDecimal marketPricePerTon;

    @Column(name = "total_revenue", precision = 12, scale = 2)
    private BigDecimal totalRevenue;

    @Column(name = "production_cost", precision = 12, scale = 2)
    private BigDecimal productionCost;

    @Column(name = "profit_margin", precision = 12, scale = 2)
    private BigDecimal profitMargin;

    @Column(name = "harvest_notes", length = 1000)
    private String harvestNotes;

    @Column(name = "weather_conditions", length = 500)
    private String weatherConditions;

    @Enumerated(EnumType.STRING)
    private HarvestStatus status = HarvestStatus.PLANNED;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "field_id")
    @NotNull
    private Field field;

}
