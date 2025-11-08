package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.Field;
import com.challenge_oracle.agrotech.domains.Property;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {
    Page<Field> findByProperty(Property property, Pageable pageable);

    boolean existsByTitleAndProperty(String title, Property property);
}
