package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequestDTO {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    @Size(min = 11, max = 14)
    private String cpf;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    private Role role = Role.USER;

    public User toUser(String encodedPassword) {
        Role resolvedRole = (role == Role.USER || role == Role.MANAGER) ? role : Role.USER;
        return User.builder()
                .email(this.email)
                .password(encodedPassword)
                .cpf(this.cpf)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .role(resolvedRole)
                .build();
    }
}