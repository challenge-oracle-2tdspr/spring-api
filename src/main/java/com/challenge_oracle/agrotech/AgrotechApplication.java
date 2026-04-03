package com.challenge_oracle.agrotech;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableRabbit
@EnableFeignClients
public class AgrotechApplication {

	public static void main(String[] args) {
		SpringApplication.run(AgrotechApplication.class, args);
	}

}
