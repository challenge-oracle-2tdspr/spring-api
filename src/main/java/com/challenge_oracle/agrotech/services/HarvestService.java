package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.FieldRepository;
import com.challenge_oracle.agrotech.gateways.repositories.HarvestRepository;
import com.challenge_oracle.agrotech.gateways.requests.HarvestCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.HarvestUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.HarvestResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HarvestService {

    private final HarvestRepository harvestRepository;
    private final FieldRepository fieldRepository;

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    private void checkAccess(Harvest harvest, User user) {
        if (user.getRole() == Role.ADMIN) return;

        boolean isOwnerOrMember = harvest.getField().getProperty().getOwner().getId().equals(user.getId())
                || harvest.getField().getProperty().getMembers().stream()
                .anyMatch(m -> m.getUser().getId().equals(user.getId()));

        if (!isOwnerOrMember) throw new AccessDeniedException("Access denied");
    }

    @Transactional
    public HarvestResponseDTO createHarvest(HarvestCreateRequestDTO dto) {
        User auth = getAuthenticatedUser();

        Field field = fieldRepository.findById(dto.getFieldId())
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (auth.getRole() == Role.MANAGER) {
            boolean isOwnerOrMember = field.getProperty().getOwner().getId().equals(auth.getId())
                    || field.getProperty().getMembers().stream()
                    .anyMatch(m -> m.getUser().getId().equals(auth.getId()));
            if (!isOwnerOrMember) throw new AccessDeniedException("Access denied for this field");
        }

        Harvest harvest = dto.toHarvest(field);
        return HarvestResponseDTO.fromHarvest(harvestRepository.save(harvest));
    }

    @Transactional(readOnly = true)
    public Page<HarvestResponseDTO> getAllHarvests(Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return harvestRepository.findAll(pageable).map(HarvestResponseDTO::fromHarvest);
        }

        return harvestRepository.findAllByOwnerOrMember(auth.getId(), pageable)
                .map(HarvestResponseDTO::fromHarvest);
    }

    @Transactional(readOnly = true)
    public Page<HarvestResponseDTO> getHarvestsByField(UUID fieldId, Pageable pageable) {
        User auth = getAuthenticatedUser();

        Field field = fieldRepository.findById(fieldId)
                .orElseThrow(() -> new RuntimeException("Field not found"));

        if (auth.getRole() == Role.ADMIN) {
            return harvestRepository.findByField(field, pageable)
                    .map(HarvestResponseDTO::fromHarvest);
        }

        return harvestRepository.findByFieldAndOwnerOrMember(fieldId, auth.getId(), pageable)
                .map(HarvestResponseDTO::fromHarvest);
    }

    @Transactional(readOnly = true)
    public Page<HarvestResponseDTO> getHarvestsByStatus(HarvestStatus status, Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return harvestRepository.findByStatus(status, pageable)
                    .map(HarvestResponseDTO::fromHarvest);
        }

        return harvestRepository.findByStatusAndOwnerOrMember(status, auth.getId(), pageable)
                .map(HarvestResponseDTO::fromHarvest);
    }

    @Transactional(readOnly = true)
    public HarvestResponseDTO getHarvestById(UUID id) {
        User auth = getAuthenticatedUser();

        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));

        checkAccess(harvest, auth);

        return HarvestResponseDTO.fromHarvest(harvest);
    }

    @Transactional
    public HarvestResponseDTO updateHarvest(UUID id, HarvestUpdateRequestDTO dto) {
        User auth = getAuthenticatedUser();

        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));

        checkAccess(harvest, auth);

        dto.updateHarvest(harvest);

        if (harvest.getTotalRevenue() != null && harvest.getProductionCost() != null) {
            harvest.setProfitMargin(
                    harvest.getTotalRevenue().subtract(harvest.getProductionCost())
            );
        }

        return HarvestResponseDTO.fromHarvest(harvestRepository.save(harvest));
    }

    @Transactional
    public void deleteHarvest(UUID id) {
        Harvest harvest = harvestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Harvest not found"));
        harvestRepository.delete(harvest);
    }
}