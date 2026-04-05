package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    Optional<User> findByCpf(String cpf);

    boolean existsByEmail(@NotBlank @Email String email);

    boolean existsByCpf(String cpf);

    @Query("""
        SELECT DISTINCT u FROM User u
        JOIN u.propertyMemberships pm
        JOIN pm.property p
        JOIN p.members m
        WHERE p.owner.id = :managerId OR m.user.id = :managerId
    """)
    Page<User> findUsersByManagerProperties(@Param("managerId") UUID managerId, Pageable pageable);

}
