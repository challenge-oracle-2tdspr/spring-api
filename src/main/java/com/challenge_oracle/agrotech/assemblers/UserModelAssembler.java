package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.UserController;
import com.challenge_oracle.agrotech.gateways.responses.UserResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserModelAssembler implements RepresentationModelAssembler<UserResponseDTO, UserResponseDTO> {

    @Override
    public UserResponseDTO toModel(UserResponseDTO user) {
        user.add(linkTo(methodOn(UserController.class).getUserById(user.getId())).withSelfRel());
        user.add(linkTo(methodOn(UserController.class).getAllUsers(0, 20, null)).withRel("users"));
        user.add(linkTo(methodOn(UserController.class).updateUser(user.getId(), null)).withRel("update"));
        user.add(linkTo(methodOn(UserController.class).deleteUser(user.getId())).withRel("delete"));
        return user;
    }
}
