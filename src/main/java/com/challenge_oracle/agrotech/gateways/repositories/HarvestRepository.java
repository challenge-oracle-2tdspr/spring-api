package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Harvest;
import com.challenge_oracle.agrotech.enums.HarvestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface HarvestRepository extends JpaRepository<Harvest, UUID> {

    Page<Harvest> findByField(Field field, Pageable pageable);

    Page<Harvest> findByStatus(HarvestStatus status, Pageable pageable);

    @Query("""
        SELECT h FROM Harvest h
        WHERE h.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<Harvest> findAllByOwnerOrMember(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT h FROM Harvest h
        WHERE h.field.id = :fieldId
        AND h.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<Harvest> findByFieldAndOwnerOrMember(
            @Param("fieldId") UUID fieldId,
            @Param("userId") UUID userId,
            Pageable pageable
    );

    @Query("""
        SELECT h FROM Harvest h
        WHERE h.status = :status
        AND h.field.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<Harvest> findByStatusAndOwnerOrMember(
            @Param("status") HarvestStatus status,
            @Param("userId") UUID userId,
            Pageable pageable
    );
}