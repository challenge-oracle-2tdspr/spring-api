package com.challenge_oracle.agrotech.gateways.responses;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProfileResponseDTO extends RepresentationModel<ProfileResponseDTO> {

    private UUID id;
    private String email;
    private String cpf;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    private List<ProfilePropertyDTO> properties;
    private LocalDateTime createdAt;

    public ProfileResponseDTO(UUID id, String email, String cpf, String firstName,
                              String lastName, String phoneNumber, Role role,
                              List<ProfilePropertyDTO> properties, LocalDateTime createdAt) {
        this.id = id;
        this.email = email;
        this.cpf = cpf;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.properties = properties;
        this.createdAt = createdAt;
    }

    public static ProfileResponseDTO fromUser(User user, List<Property> properties) {
        List<ProfilePropertyDTO> propertyDTOs = properties.stream()
                .map(p -> ProfilePropertyDTO.fromProperty(p, user.getId()))
                .toList();

        return new ProfileResponseDTO(
                user.getId(),
                user.getEmail(),
                user.getCpf(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhoneNumber(),
                user.getRole(),
                propertyDTOs,
                user.getCreatedAt()
        );
    }
}