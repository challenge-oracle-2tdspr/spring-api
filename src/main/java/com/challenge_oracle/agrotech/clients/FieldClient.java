package com.challenge_oracle.agrotech.clients;

import com.challenge_oracle.agrotech.gateways.responses.FieldResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "feild-client", url = "${services.apex.field}")
public interface FieldClient {

    @PostMapping("/create")
    void createApexField(@RequestBody FieldResponseDTO field);
}
