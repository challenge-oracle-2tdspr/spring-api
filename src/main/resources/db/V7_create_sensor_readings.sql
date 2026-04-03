CREATE TABLE sensor_readings (
    id UUID PRIMARY KEY,
    reading_time TIMESTAMP NOT NULL,
    temperature NUMERIC(5, 2),
    humidity NUMERIC(5, 2),
    soil_moisture NUMERIC(5, 2),
    wind_speed NUMERIC(5, 2),
    wind_direction VARCHAR(255),
    rainfall NUMERIC(6, 2),
    soil_ph NUMERIC(4, 2),
    light_intensity NUMERIC(8, 2),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    sensor_id UUID NOT NULL,
    CONSTRAINT fk_reading_sensor FOREIGN KEY (sensor_id) REFERENCES sensors (id)
);