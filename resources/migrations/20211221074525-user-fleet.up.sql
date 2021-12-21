CREATE TABLE user_fleet(
    user_id UUID REFERENCES users(id) NOT NULL,
    fleet_id UUID REFERENCES fleets(id) NOT NULL
);
