CREATE TABLE property_members (
    id UUID PRIMARY KEY,
    property_id UUID NOT NULL,
    user_id UUID NOT NULL,
    role VARCHAR(20) NOT NULL,
    joined_at TIMESTAMP,
    CONSTRAINT fk_member_property FOREIGN KEY (property_id) REFERENCES properties (id),
    CONSTRAINT fk_member_user FOREIGN KEY (user_id) REFERENCES users (id)
);