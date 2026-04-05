package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.SensorReadingController;
import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SensorReadingModelAssembler implements RepresentationModelAssembler<SensorReadingResponseDTO, SensorReadingResponseDTO> {

    @Override
    public SensorReadingResponseDTO toModel(SensorReadingResponseDTO reading) {
        reading.add(linkTo(methodOn(SensorReadingController.class)
                .getReadingById(reading.getId()))
                .withSelfRel());

        reading.add(linkTo(methodOn(SensorReadingController.class)
                .getAllSensorReadings(0, 20, null))
                .withRel("sensor-readings"));

        if (reading.getSensorId() != null) {
            reading.add(linkTo(methodOn(SensorReadingController.class)
                    .getReadingsByField(reading.getFieldId(), 0, 20, null))
                    .withRel("field-readings"));
        }

        return reading;
    }
}