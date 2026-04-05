package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.enums.Role;
import com.challenge_oracle.agrotech.gateways.repositories.UserRepository;
import com.challenge_oracle.agrotech.gateways.requests.UserCreateRequestDTO;
import com.challenge_oracle.agrotech.gateways.requests.UserUpdateRequestDTO;
import com.challenge_oracle.agrotech.gateways.responses.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }

    @Transactional
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

    @Transactional(readOnly = true)
    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.ADMIN) {
            return userRepository.findAll(pageable)
                    .map(UserResponseDTO::fromUser);
        }

        return userRepository.findUsersByManagerProperties(auth.getId(), pageable)
                .map(UserResponseDTO::fromUser);
    }

    @Transactional(readOnly = true)
    public UserResponseDTO getUserById(UUID id) {
        User auth = getAuthenticatedUser();

        if (auth.getRole() == Role.USER && !auth.getId().equals(id)) {
            throw new AccessDeniedException("Access denied");
        }

        if (auth.getRole() == Role.MANAGER) {
            boolean isSelf = auth.getId().equals(id);
            boolean isInHisProperties = userRepository
                    .findUsersByManagerProperties(auth.getId(), Pageable.unpaged())
                    .stream()
                    .anyMatch(u -> u.getId().equals(id));

            if (!isSelf && !isInHisProperties) {
                throw new AccessDeniedException("Access denied");
            }
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponseDTO.fromUser(user);
    }

    @Transactional
    public UserResponseDTO updateUser(UUID id, UserUpdateRequestDTO dto) {
        if (dto.getEmail() != null) {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!existing.getEmail().equals(dto.getEmail()) &&
                    userRepository.existsByEmail(dto.getEmail())) {
                throw new IllegalArgumentException("Email already exists");
            }
        }
        if (dto.getCpf() != null) {
            User existing = userRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            if (!existing.getCpf().equals(dto.getCpf()) &&
                    userRepository.existsByCpf(dto.getCpf())) {
                throw new IllegalArgumentException("Cpf already exists");
            }
        }

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        dto.updateUser(user);
        return UserResponseDTO.fromUser(userRepository.save(user));
    }

    @Transactional
    public void deleteUser(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }
}