CREATE TABLE fields (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    crop VARCHAR(255) NOT NULL,
    field_area NUMERIC(19, 2),
    area_unit VARCHAR(20) DEFAULT 'HECTARES',
    soil_type VARCHAR(50),
    irrigation_type VARCHAR(50),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    property_id UUID NOT NULL,
    CONSTRAINT fk_field_property FOREIGN KEY (property_id) REFERENCES properties (id)
);