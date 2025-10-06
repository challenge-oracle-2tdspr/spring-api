package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateRequestDTO {
    @NotBlank(message = "email is mandatory")
    @Email(message = "email must have a valid format")
    private String email;

    @NotBlank(message = "password is mandatory")
    @Size(min = 8, message = "password must have at least 8 characters")
    private String password;

    @NotBlank(message = "CPF is mandatory")
    @Size(min = 11, max = 14, message = "CPF must have between 11 and 14 characters")
    private String cpf;

    @NotNull(message = "Role is mandatory")
    private Role role;

    private String firstName;
    private String lastName;
    private String phoneNumber;

    public User toUser() {
        return User.builder()
                .email(this.email)
                .password(this.password)
                .cpf(this.cpf)
                .role(this.role)
                .firstName(this.firstName)
                .lastName(this.lastName)
                .phoneNumber(this.phoneNumber)
                .build();
    }

}
