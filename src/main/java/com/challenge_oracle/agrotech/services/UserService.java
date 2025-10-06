package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import com.challenge_oracle.agrotech.gateways.requests.UserCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.UserUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserResponseDTO createUser(UserCreateRequestDTO dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        if (userRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("Cpf already exists");
        }

        User user = dto.toUser();
        User savedUser = userRepository.save(user);

        return UserResponseDTO.fromUser(savedUser);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        Page<User> users = userRepository.findAll(pageable);
        return users.map(UserResponseDTO::fromUser);
    }

    public UserResponseDTO getUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponseDTO.fromUser(user);
    }

    public UserResponseDTO updateUser(UUID id, UserUpdateRequestDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (dto.getEmail() != null && !user.getEmail().equals(dto.getEmail()) &&
                userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("email already exists");
        }
        if (dto.getCpf() != null && !user.getCpf().equals(dto.getCpf()) &&
                userRepository.existsByCpf(dto.getCpf())) {
            throw new IllegalArgumentException("cpf already exists");
        }

        dto.updateUser(user);
        User updatedUser = userRepository.save(user);
        return UserResponseDTO.fromUser(updatedUser);
    }

    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}