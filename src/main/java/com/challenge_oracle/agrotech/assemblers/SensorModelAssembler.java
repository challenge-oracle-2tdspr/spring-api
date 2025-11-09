package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.FieldController;
import com.challenge_oracle.agrotech.gateways.controllers.SensorController;
import com.challenge_oracle.agrotech.gateways.responses.SensorResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SensorModelAssembler implements RepresentationModelAssembler<SensorResponseDTO, SensorResponseDTO> {

    @Override
    public SensorResponseDTO toModel(SensorResponseDTO sensor) {
        sensor.add(linkTo(methodOn(SensorController.class).getSensorById(sensor.getId())).withSelfRel());
        sensor.add(linkTo(methodOn(SensorController.class)
                .getAllSensors(0, 20, null))
                .withRel("sensors"));
        sensor.add(linkTo(methodOn(SensorController.class)
                .getSensorBySensorCode(sensor.getSensorCode()))
                .withRel("by-code"));
        sensor.add(linkTo(methodOn(SensorController.class)
                .updateSensor(sensor.getId(), null))
                .withRel("update"));
        sensor.add(linkTo(methodOn(SensorController.class)
                .deleteSensor(sensor.getId()))
                .withRel("delete"));

        if (sensor.getFieldId() != null) {
            sensor.add(linkTo(methodOn(FieldController.class)
                    .getFieldById(sensor.getFieldId()))
                    .withRel("field"));
            sensor.add(linkTo(methodOn(SensorController.class)
                    .getSensorsByField(sensor.getFieldId(), 0, 20, null))
                    .withRel("field-sensors"));
        }

        if (sensor.getStatus() != null) {
            sensor.add(linkTo(methodOn(SensorController.class)
                    .getSensorsByStatus(sensor.getStatus(), 0, 20, null))
                    .withRel("status-sensors"));
        }

        return sensor;
    }
}
