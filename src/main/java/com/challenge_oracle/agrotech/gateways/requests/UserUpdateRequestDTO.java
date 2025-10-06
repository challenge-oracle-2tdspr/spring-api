package com.challenge_oracle.agrotech.gateways.requests;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Optional;

@Data
public class UserUpdateRequestDTO {

    @Email(message = "email must have a valid format")
    private String email;

    @Size(min = 8, message = "password must have at least 8 characters")
    private String password;

    @Size(min = 11, max = 14, message = "cpf must have between 11 and 14 characters")
    private String cpf;

    private Role role;
    private String firstName;
    private String lastName;
    private String phoneNumber;

    public void updateUser(User user) {
        if (this.email != null) {
            user.setEmail(this.email);
        }
        if (this.password != null) {
            user.setPassword(this.password);
        }
        if (this.cpf != null) {
            user.setCpf(this.cpf);
        }
        if (this.role != null) {
            user.setRole(this.role);
        }
        if (this.firstName != null) {
            user.setFirstName(this.firstName);
        }
        if (this.lastName != null) {
            user.setLastName(this.lastName);
        }
        if (this.phoneNumber != null) {
            user.setPhoneNumber(this.phoneNumber);
        }
    }
}
