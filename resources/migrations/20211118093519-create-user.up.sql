DROP TYPE IF EXISTS role;
--;;
CREATE TYPE role as ENUM ('driver','owner','admin');
--;;
CREATE TABLE IF NOT EXISTS test_users(
       id serial PRIMARY KEY,
       first_name text NOT NULL,
       last_name text NOT NULL,
       email text UNIQUE NOT NULL,
       password text NOT NULL,
       user_role role
);
