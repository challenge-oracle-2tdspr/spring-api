package com.challenge_oracle.agrotech.utils;

import com.challenge_oracle.agrotech.clients.SensorClient;
import com.challenge_oracle.agrotech.domains.*;
import com.challenge_oracle.agrotech.enums.*;
import com.challenge_oracle.agrotech.gateways.repositories.*;
import com.challenge_oracle.agrotech.gateways.responses.SensorResponseDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMemberRepository propertyMemberRepository;
    private final FieldRepository fieldRepository;
    private final SensorRepository sensorRepository;
    private final PasswordEncoder passwordEncoder;
    private final SensorClient sensorClient;

    @Bean
    public CommandLineRunner seedAdminUser() {
        return args -> {
            if (userRepository.existsByEmail("manager1@agrotech.com")) {
                log.info("Seed data already exists, skipping.");
                return;
            }

            // ----------------------------------------------------------------
            // USERS
            // ----------------------------------------------------------------
            // User admin = saveUser("admin@admin.com",    "Admin",   "System",  "00000000000", Role.ADMIN);
            User manager1 = saveUser("manager1@agrotech.com", "Carlos",  "Silva",   "11111111111", Role.MANAGER);
            User manager2 = saveUser("manager2@agrotech.com", "Fernanda","Souza",   "22222222222", Role.MANAGER);
            User user1    = saveUser("user1@agrotech.com",    "João",    "Oliveira","33333333333", Role.USER);
            User user2    = saveUser("user2@agrotech.com",    "Ana",     "Costa",   "44444444444", Role.USER);
            User user3    = saveUser("user3@agrotech.com",    "Pedro",   "Lima",    "55555555555", Role.USER);
            User user4    = saveUser("user4@agrotech.com",    "Lucia",   "Martins", "66666666666", Role.USER);

            // ----------------------------------------------------------------
            // PROPERTIES
            // ----------------------------------------------------------------
            Property prop1 = propertyRepository.save(Property.builder()
                    .title("Fazenda São João")
                    .description("Propriedade principal de soja e milho")
                    .totalArea(new BigDecimal("500.00"))
                    .areaUnit(AreaUnit.HECTARES)
                    .address("Estrada Rural, Km 12")
                    .city("Ribeirão Preto")
                    .state("SP")
                    .country("Brasil")
                    .zipCode("14000-000")
                    .owner(manager1)
                    .build());

            Property prop2 = propertyRepository.save(Property.builder()
                    .title("Sítio Boa Esperança")
                    .description("Produção de café e cana-de-açúcar")
                    .totalArea(new BigDecimal("320.00"))
                    .areaUnit(AreaUnit.HECTARES)
                    .address("Rodovia Estadual, Km 45")
                    .city("Campinas")
                    .state("SP")
                    .country("Brasil")
                    .zipCode("13000-000")
                    .owner(manager2)
                    .build());

            // ----------------------------------------------------------------
            // PROPERTY MEMBERS
            // ----------------------------------------------------------------
            saveMember(prop1, manager1, Role.MANAGER);
            saveMember(prop1, user1,    Role.USER);
            saveMember(prop1, user2,    Role.USER);

            saveMember(prop2, manager2, Role.MANAGER);
            saveMember(prop2, user3,    Role.USER);
            saveMember(prop2, user4,    Role.USER);

            // ----------------------------------------------------------------
            // FIELDS — 3 por property
            // ----------------------------------------------------------------
            Field field1 = saveField("Talhão Norte",     "Soja",   prop1, SoilType.CLAY,  IrrigationType.PIVOT,     new BigDecimal("80.00"));
            Field field2 = saveField("Talhão Sul",       "Milho",  prop1, SoilType.LOAM,  IrrigationType.DRIP,      new BigDecimal("120.00"));
            Field field3 = saveField("Talhão Leste",     "Trigo",  prop1, SoilType.SILT,  IrrigationType.SPRINKLER, new BigDecimal("60.00"));
            Field field4 = saveField("Bloco Cafeeiro A", "Café",   prop2, SoilType.LOAM,  IrrigationType.DRIP,      new BigDecimal("90.00"));
            Field field5 = saveField("Bloco Cafeeiro B", "Café",   prop2, SoilType.CLAY,  IrrigationType.DRIP,      new BigDecimal("70.00"));
            Field field6 = saveField("Área Cana",        "Cana",   prop2, SoilType.SAND,  IrrigationType.FLOOD,     new BigDecimal("110.00"));

            // ----------------------------------------------------------------
            // SENSORS — 1 por field
            // ----------------------------------------------------------------
            saveSensorAndSync(field1, "SNS-001", "AgriSense X1", "AgroTech Sensors");
            saveSensorAndSync(field2, "SNS-002", "AgriSense X1", "AgroTech Sensors");
            saveSensorAndSync(field3, "SNS-003", "AgriSense X2", "AgroTech Sensors");
            saveSensorAndSync(field4, "SNS-004", "SoilPro 3000", "FarmTech Ind.");
            saveSensorAndSync(field5, "SNS-005", "SoilPro 3000", "FarmTech Ind.");
            saveSensorAndSync(field6, "SNS-006", "WeatherNode V2","ClimaSens");

            log.info("Seed data created successfully.");
        };
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private User saveUser(String email, String firstName, String lastName, String cpf, Role role) {
        return userRepository.save(User.builder()
                .email(email)
                .password(passwordEncoder.encode("123456aB!"))
                .cpf(cpf)
                .firstName(firstName)
                .lastName(lastName)
                .role(role)
                .build());
    }

    private void saveMember(Property property, User user, Role role) {
        propertyMemberRepository.save(PropertyMember.builder()
                .property(property)
                .user(user)
                .role(role)
                .build());
    }

    private Field saveField(String title, String crop, Property property,
                            SoilType soilType, IrrigationType irrigationType,
                            BigDecimal area) {
        return fieldRepository.save(Field.builder()
                .title(title)
                .crop(crop)
                .property(property)
                .soilType(soilType)
                .irrigationType(irrigationType)
                .fieldArea(area)
                .areaUnit(AreaUnit.HECTARES)
                .build());
    }

    private void saveSensorAndSync(Field field, String code, String model, String manufacturer) {
        Sensor sensor = sensorRepository.save(Sensor.builder()
                .sensorCode(code)
                .model(model)
                .manufacturer(manufacturer)
                .installationDate(LocalDateTime.now())
                .status(SensorStatus.ACTIVE)
                .batteryLevel(100)
                .field(field)
                .build());

        try {
            sensorClient.createApexSensor(SensorResponseDTO.fromSensor(sensor));
            log.info("Sensor {} synced to sensor-api.", code);
        } catch (Exception e) {
            log.warn("Could not sync sensor {} to sensor-api: {}", code, e.getMessage());
        }
    }
}