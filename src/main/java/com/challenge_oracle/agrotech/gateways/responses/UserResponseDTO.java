package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserResponseDTO (
    UUID id,
    String email,
    String cpf,
    Role role,
    String firstName,
    String lastName,
    String phoneNumber,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getCpf(),
                user.getRole(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
