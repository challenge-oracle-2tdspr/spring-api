package com.challenge_oracle.agrotech.configurations;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfiguration {

    @Bean
    public OpenAPI customOpenAPI() {
        Server server = new Server();
        server.setUrl("http://localhost:8080");
        server.setDescription("OpenAPI Documentation - Development");

        Info info = new Info()
                .title("Agrotech API")
                .version("0.1.0")
                .description("Api para gerenciamento de plantações e safras");

        return new OpenAPI().info(info).servers(List.of(server));
    }
}
