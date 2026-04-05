package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {
    Page<Field> findByProperty(Property property, Pageable pageable);

    boolean existsByTitleAndProperty(String title, Property property);

    @Query("""
        SELECT f FROM Field f
        WHERE f.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<Field> findAllByOwnerOrMember(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT f FROM Field f
        WHERE f.property.id = :propertyId
        AND f.property.id IN (
            SELECT DISTINCT p.id FROM Property p
            LEFT JOIN p.members m
            WHERE p.owner.id = :userId OR m.user.id = :userId
        )
    """)
    Page<Field> findByPropertyAndOwnerOrMember(
            @Param("propertyId") UUID propertyId,
            @Param("userId") UUID userId,
            Pageable pageable
    );
}
