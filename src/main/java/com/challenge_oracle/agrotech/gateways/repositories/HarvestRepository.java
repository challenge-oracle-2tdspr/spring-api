package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, UUID> {
    Page<Harvest> findByField(Field field, Pageable pageable);

    Page<Harvest> findByStatus(HarvestStatus status, Pageable pageable);
}
