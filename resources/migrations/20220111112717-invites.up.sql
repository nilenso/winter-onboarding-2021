CREATE TABLE invites(
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    token TEXT NOT NULL UNIQUE,
    created_by UUID REFERENCES users(id) NOT NULL,
    valid_until TIMESTAMP WITHOUT TIME ZONE DEFAULT (NOW() AT TIME ZONE 'UTC'),
    usage_limit INTEGER NOT NULL DEFAULT 1,
    role TEXT NOT NULL,
    is_active BOOLEAN,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
--;;
ALTER TABLE users ADD COLUMN invite_id UUID DEFAULT NULL REFERENCES invites(id);
