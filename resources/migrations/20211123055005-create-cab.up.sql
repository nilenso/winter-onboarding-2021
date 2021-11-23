CREATE EXTENSION "uuid-ossp";
--;;
CREATE TABLE cabs(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4() ,
    licence_plate TEXT NOT NULL,
    name TEXT NOT NULL,
    distance_travelled BIGINT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
