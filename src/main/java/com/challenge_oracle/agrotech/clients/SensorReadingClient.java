package com.challenge_oracle.agrotech.clients;

import com.challenge_oracle.agrotech.gateways.responses.SensorReadingResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sensor-reading-client", url = "${services.apex.sensor-reading}")
public interface SensorReadingClient {

    @PostMapping("/create")
    void createApexSensorReading(@RequestBody SensorReadingResponseDTO sensorReading);
}
