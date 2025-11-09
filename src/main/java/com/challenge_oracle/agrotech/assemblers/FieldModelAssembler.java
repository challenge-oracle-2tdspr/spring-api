package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.FieldController;
import com.challenge_oracle.agrotech.gateways.controllers.PropertyController;
import com.challenge_oracle.agrotech.gateways.responses.FieldResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class FieldModelAssembler implements RepresentationModelAssembler<FieldResponseDTO, FieldResponseDTO> {

    @Override
    public FieldResponseDTO toModel(FieldResponseDTO field) {
        field.add(linkTo(methodOn(FieldController.class).getFieldById(field.getId())).withSelfRel());
        field.add(linkTo(methodOn(FieldController.class)
                .getAllFields(0, 20, null))
                .withRel("fields"));
        field.add(linkTo(methodOn(FieldController.class)
                .updateField(field.getId(), null))
                .withRel("update"));
        field.add(linkTo(methodOn(FieldController.class)
                .deleteField(field.getId()))
                .withRel("delete"));

        if (field.getPropertyId() != null) {
            field.add(linkTo(methodOn(PropertyController.class)
                    .getPropertyById(field.getPropertyId()))
                    .withRel("property"));
            field.add(linkTo(methodOn(FieldController.class)
                    .getFieldsByProperty(field.getPropertyId(), 0, 20, null))
                    .withRel("property-fields"));
        }

        return field;
    }
}
