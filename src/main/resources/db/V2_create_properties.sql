CREATE TABLE properties (
    id UUID PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    total_area NUMERIC(19, 2),
    area_unit VARCHAR(20) DEFAULT 'HECTARES',
    address VARCHAR(255),
    city VARCHAR(255),
    state VARCHAR(255),
    country VARCHAR(255),
    zip_code VARCHAR(255),
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    owner_id UUID,
    CONSTRAINT fk_property_owner FOREIGN KEY (owner_id) REFERENCES users (id)
);