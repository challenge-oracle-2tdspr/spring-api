package com.challenge_oracle.agrotech.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    @Bean
    public Queue sensorQueue() {
        return QueueBuilder.durable("sensor-readings").build();
    }

    @Bean
    public Exchange sensorExchange() {
        return ExchangeBuilder.directExchange("sensor-readings").durable(true).build();
    }

    @Bean
    public Binding sensorBinding(Queue sensorQueue, Exchange sensorExchange) {
        return BindingBuilder.bind(sensorQueue).to(sensorExchange).with("sensor-reading-rk").noargs();
    }

    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
