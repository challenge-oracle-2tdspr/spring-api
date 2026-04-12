package com.challenge_oracle.agrotech.messaging;

import com.challenge_oracle.agrotech.configurations.RabbitMqConfig;
import com.challenge_oracle.agrotech.gateways.requests.SensorReadingCreateRequestDTO;
import com.challenge_oracle.agrotech.services.SensorReadingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SensorReadingConsumer {

    private final SensorReadingService sensorReadingService;

    @RabbitListener(queues = RabbitMqConfig.READING_QUEUE)
    public void receive(SensorReadingCreateRequestDTO dto) {
        log.info("Reading received — sensor={} time={}", dto.getSensorId(), dto.getReadingTime());
        try {
            sensorReadingService.createSensorReading(dto);
        } catch (Exception e) {
            log.error("Failed to process reading for sensor {}: {}", dto.getSensorId(), e.getMessage());
            throw e;
        }
    }
}