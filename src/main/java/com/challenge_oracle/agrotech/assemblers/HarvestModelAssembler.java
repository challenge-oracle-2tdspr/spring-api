package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.FieldController;
import com.challenge_oracle.agrotech.gateways.controllers.HarvestController;
import com.challenge_oracle.agrotech.gateways.responses.HarvestResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HarvestModelAssembler implements RepresentationModelAssembler<HarvestResponseDTO, HarvestResponseDTO> {

    @Override
    public HarvestResponseDTO toModel(HarvestResponseDTO harvest) {
        harvest.add(linkTo(methodOn(HarvestController.class).getHarvestById(harvest.getId())).withSelfRel());
        harvest.add(linkTo(methodOn(HarvestController.class)
                .getAllHarvests(0, 20, null))
                .withRel("harvests"));
        harvest.add(linkTo(methodOn(HarvestController.class)
                .updateHarvest(harvest.getId(), null))
                .withRel("update"));
        harvest.add(linkTo(methodOn(HarvestController.class)
                .deleteHarvest(harvest.getId()))
                .withRel("delete"));

        if (harvest.getFieldId() != null) {
            harvest.add(linkTo(methodOn(FieldController.class)
                    .getFieldById(harvest.getFieldId()))
                    .withRel("field"));
            harvest.add(linkTo(methodOn(HarvestController.class)
                    .getHarvestsByField(harvest.getFieldId(), 0, 20, null))
                    .withRel("field-harvests"));
        }

        if (harvest.getStatus() != null) {
            harvest.add(linkTo(methodOn(HarvestController.class)
                    .getHarvestsByStatus(harvest.getStatus(), 0, 20, null))
                    .withRel("status-harvests"));
        }

        return harvest;
    }
}
