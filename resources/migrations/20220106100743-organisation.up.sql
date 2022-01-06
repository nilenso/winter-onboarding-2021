CREATE TABLE organisations(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name TEXT NOT NULL,
    created_by UUID REFERENCES users(id) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
); 
--;;
ALTER TABLE users ADD COLUMN org_id UUID REFERENCES organisations(id) DEFAULT NULL;
--;;
ALTER TABLE cabs ADD COLUMN org_id UUID REFERENCES organisations(id) DEFAULT NULL;
--;;
ALTER TABLE fleets ADD COLUMN org_id UUID REFERENCES organisations(id) DEFAULT NULL;
