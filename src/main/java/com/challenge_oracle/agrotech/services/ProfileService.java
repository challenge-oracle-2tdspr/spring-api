package com.challenge_oracle.agrotech.services;

import com.challenge_oracle.agrotech.domains.Property;
import com.challenge_oracle.agrotech.domains.User;
import com.challenge_oracle.agrotech.gateways.repositories.PropertyRepository;
import com.challenge_oracle.agrotech.gateways.responses.ProfileResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final PropertyRepository propertyRepository;

    @Transactional(readOnly = true)
    public ProfileResponseDTO getProfile() {
        User user = getAuthenticatedUser();

        List<Property> properties = propertyRepository.findAllByOwnerOrMember(user.getId());

        return ProfileResponseDTO.fromUser(user, properties);
    }

    private User getAuthenticatedUser() {
        return (User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();
    }
}