package com.challenge_oracle.agrotech.configurations;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String EXCHANGE = "sensor.exchange";
    public static final String READING_QUEUE = "sensor.reading.queue";
    public static final String READING_ROUTING = "sensor.reading";

    @Bean
    public TopicExchange sensorExchange() {
        return new TopicExchange(EXCHANGE, true, false);
    }

    @Bean
    public Queue sensorReadingQueue() {
        return QueueBuilder.durable(READING_QUEUE).build();
    }

    @Bean
    public Binding sensorReadingBinding() {
        return BindingBuilder.bind(sensorReadingQueue())
                .to(sensorExchange())
                .with(READING_ROUTING);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter());
        return factory;
    }
}