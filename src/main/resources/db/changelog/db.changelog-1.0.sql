DROP TABLE IF EXISTS roles CASCADE;
CREATE TABLE IF NOT EXISTS roles
(
    id        SERIAL PRIMARY KEY,
    role_name VARCHAR(12) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS users CASCADE;
CREATE TABLE IF NOT EXISTS users
(
    id        SERIAL PRIMARY KEY,
    user_name VARCHAR(64) NOT NULL UNIQUE,
    password  VARCHAR(64) NOT NULL,
    email     VARCHAR(128),
    role      VARCHAR(12) NOT NULL REFERENCES roles (role_name)
);

DROP TABLE IF EXISTS order_status CASCADE;
CREATE TABLE IF NOT EXISTS order_status
(
    id     SERIAL PRIMARY KEY,
    status VARCHAR(12) NOT NULL UNIQUE
);

DROP TABLE IF EXISTS orders CASCADE;
CREATE TABLE IF NOT EXISTS orders
(
    id           SERIAL PRIMARY KEY,
    title        VARCHAR(128) NOT NULL,
    description  VARCHAR(256),
    status       VARCHAR(12)  NOT NULL REFERENCES order_status (status),
    created_by   INT          NOT NULL REFERENCES users (id),
    processed_by INT REFERENCES users (id),
    created_date TIMESTAMPTZ  NOT NULL DEFAULT now(),
    updated_date TIMESTAMPTZ
);
