package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.PropertyController;
import com.challenge_oracle.agrotech.gateways.controllers.UserController;
import com.challenge_oracle.agrotech.gateways.responses.PropertyResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class PropertyModelAssembler implements RepresentationModelAssembler<PropertyResponseDTO, PropertyResponseDTO> {

    @Override
    public PropertyResponseDTO toModel(PropertyResponseDTO property) {
        property.add(linkTo(methodOn(PropertyController.class).getPropertyById(property.getId())).withSelfRel());
        property.add(linkTo(methodOn(PropertyController.class)
                .getAllProperties(0, 20, "createdAt", "DESC", null))
                .withRel("properties"));
        property.add(linkTo(methodOn(PropertyController.class)
                .updateProperty(property.getId(), null))
                .withRel("update"));
        property.add(linkTo(methodOn(PropertyController.class)
                .deleteProperty(property.getId()))
                .withRel("delete"));

        if (property.getOwnerId() != null) {
            property.add(linkTo(methodOn(UserController.class)
                    .getUserById(property.getOwnerId()))
                    .withRel("owner"));
        }

        return property;
    }
}
