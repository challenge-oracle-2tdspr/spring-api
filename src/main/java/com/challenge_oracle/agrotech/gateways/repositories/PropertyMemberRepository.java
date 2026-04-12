package com.challenge_oracle.agrotech.gateways.repositories;

import com.challenge_oracle.agrotech.domains.PropertyMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PropertyMemberRepository extends JpaRepository<PropertyMember, UUID> {
}
