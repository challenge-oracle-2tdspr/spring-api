package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import com.challenge_oracle.agrotech.gateways.requests.LoginRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.RegisterRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.AuthResponseDTO;
import com.challenge_oracle.agrotech.gateways.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponseDTO register(RegisterRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .cpf(dto.getCpf())
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .phoneNumber(dto.getPhoneNumber())
                .role(Role.USER)
                .build();

        User saved = userRepository.save(user);

        return AuthResponseDTO.builder()
                .token(jwtService.generateToken(saved))
                .user(UserResponseDTO.fromUser(saved))
                .build();
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        return AuthResponseDTO.builder()
                .token(jwtService.generateToken(user))
                .user(UserResponseDTO.fromUser(user))
                .build();
    }
}