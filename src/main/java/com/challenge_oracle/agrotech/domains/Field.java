package com.challenge_oracle.agrotech.domains;

import com.challenge_oracle.agrotech.enums.AreaUnit;
import com.challenge_oracle.agrotech.enums.IrrigationType;
import com.challenge_oracle.agrotech.enums.SoilType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "properties")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Field {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private String crop;

    @Column(name = "field_area")
    private BigDecimal fieldArea;

    @Column(name = "area_unit")
    @Enumerated(EnumType.STRING)
    private AreaUnit areaUnit = AreaUnit.HECTARES;

    @Column(name = "soil_type")
    @Enumerated(EnumType.STRING)
    private SoilType soilType;

    @Column(name = "irrigation_type")
    @Enumerated(EnumType.STRING)
    private IrrigationType irrigationType;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "property_id")
    @NotNull
    private Property property;

    @OneToOne(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Sensor sensor;

    @OneToMany(mappedBy = "field", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Harvest> harvests;

}
