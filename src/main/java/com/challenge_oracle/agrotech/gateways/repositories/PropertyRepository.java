package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface PropertyRepository extends JpaRepository<Property, UUID> {

    boolean existsByTitle(String title);

    @Query("""
        SELECT DISTINCT p FROM Property p
        LEFT JOIN p.members m
        WHERE p.owner.id = :userId OR m.user.id = :userId
    """)
    Page<Property> findByOwnerOrMember(@Param("userId") UUID userId, Pageable pageable);

    @Query("""
        SELECT DISTINCT p FROM Property p
        LEFT JOIN p.members m
        WHERE p.owner.id = :userId OR m.user.id = :userId
    """)
    List<Property> findAllByOwnerOrMember(@Param("userId") UUID userId);
}