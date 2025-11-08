package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.gateways.repositories.FieldRepository;
import com.challenge_oracle.agrotech.gateways.repositories.HarvestRepository;
import com.challenge_oracle.agrotech.gateways.requests.HarvestCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.HarvestUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.HarvestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final HarvestRepository harvestRepository;
    private final FieldRepository fieldRepository;

    public HarvestResponseDTO createHarvest(HarvestCreateRequestDTO dto) {
        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found"));

        Harvest harvest = dto.toHarvest(field);
        Harvest savedHarvest = harvestRepository.save(harvest);

        return HarvestResponseDTO.fromHarvest(savedHarvest);
    }

    public Page<HarvestResponseDTO> getAllHarvests(Pageable pageable) {
        Page<Harvest> harvests = harvestRepository.findAll(pageable);
        return harvests.map(HarvestResponseDTO::fromHarvest);
    }

    public Page<HarvestResponseDTO> getHarvestsByField(UUID fieldId, Pageable pageable) {
        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        Page<Harvest> harvests = harvestRepository.findByField(field, pageable);
        return harvests.map(HarvestResponseDTO::fromHarvest);
    }

    public Page<HarvestResponseDTO> getHarvestsByStatus(HarvestStatus status, Pageable pageable) {
        Page<Harvest> harvests = harvestRepository.findByStatus(status, pageable);
        return harvests.map(HarvestResponseDTO::fromHarvest);
    }

    public HarvestResponseDTO getHarvestById(UUID id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));

        return HarvestResponseDTO.fromHarvest(harvest);
    }

    public HarvestResponseDTO updateHarvest(UUID id, HarvestUpdateRequestDTO dto) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));

        dto.updateHarvest(harvest);

        if (harvest.getTotalRevenue() != null && harvest.getProductionCost() != null) {
            harvest.setProfitMargin(
                    harvest.getTotalRevenue().subtract(harvest.getProductionCost())
            );
        }

        Harvest updatedHarvest = harvestRepository.save(harvest);
        return HarvestResponseDTO.fromHarvest(updatedHarvest);
    }

    public void deleteHarvest(UUID id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));
        harvestRepository.delete(harvest);
    }
}
