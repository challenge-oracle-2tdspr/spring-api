package com.challenge_oracle.agrotech.clients;

import com.challenge_oracle.agrotech.gateways.responses.SensorResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "sensor", url = "${services.apex.sensor}")
public interface SensorClient {

    @PostMapping("/create")
    void createApexSensor(@RequestBody SensorResponseDTO sensor);
}
