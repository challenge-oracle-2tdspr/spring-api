CREATE TABLE sensors (
     id UUID PRIMARY KEY,
     sensor_code VARCHAR(255) UNIQUE,
     model VARCHAR(255),
     manufacturer VARCHAR(255),
     installation_date TIMESTAMP,
     status VARCHAR(20) DEFAULT 'ACTIVE',
     battery_level INTEGER,
     last_maintenance TIMESTAMP,
     created_at TIMESTAMP,
     updated_at TIMESTAMP,
     field_id UUID NOT NULL UNIQUE,
     CONSTRAINT fk_sensor_field FOREIGN KEY (field_id) REFERENCES fields (id)
);