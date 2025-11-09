package com.challenge_oracle.agrotech.assemblers;

import com.challenge_oracle.agrotech.gateways.controllers.SensorController;
import com.challenge_oracle.agrotech.gateways.controllers.SensorReadingController;
import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SensorReadingModelAssembler implements RepresentationModelAssembler<SensorReadingResponseDTO, SensorReadingResponseDTO> {

    @Override
    public SensorReadingResponseDTO toModel(SensorReadingResponseDTO reading) {
        reading.add(linkTo(methodOn(SensorReadingController.class).getReadingById(reading.getId())).withSelfRel());
        reading.add(linkTo(methodOn(SensorReadingController.class)
                .getAllSensorReadings(0, 20, null))
                .withRel("sensor-readings"));
        reading.add(linkTo(methodOn(SensorReadingController.class)
                .updateReading(reading.getId(), null))
                .withRel("update"));
        reading.add(linkTo(methodOn(SensorReadingController.class)
                .deleteReading(reading.getId()))
                .withRel("delete"));

        if (reading.getSensorId() != null) {
            reading.add(linkTo(methodOn(SensorController.class)
                    .getSensorById(reading.getSensorId()))
                    .withRel("sensor"));
            reading.add(linkTo(methodOn(SensorReadingController.class)
                    .getReadingsBySensor(reading.getSensorId(), 0, 20, null))
                    .withRel("sensor-readings-list"));
        }

        return reading;
    }
}
