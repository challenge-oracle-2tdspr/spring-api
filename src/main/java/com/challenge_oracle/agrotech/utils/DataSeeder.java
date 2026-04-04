package com.challenge_oracle.agrotech.utils;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner seedAdminUser() {
        return args -> {
            String adminEmail = "admin@admin.com";

            if (userRepository.existsByEmail(adminEmail)) {
                log.info("Admin user already exists, skipping seed.");
                return;
            }

            User admin = User.builder()
                    .email(adminEmail)
                    .password(passwordEncoder.encode("123456aB!"))
                    .cpf("00000000000")
                    .firstName("Admin")
                    .lastName("System")
                    .role(Role.ADMIN)
                    .build();

            userRepository.save(admin);
            log.info("Admin user created successfully: {}", adminEmail);
        };
    }
}
