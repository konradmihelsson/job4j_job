CREATE TABLE posts
(
    id      SERIAL PRIMARY KEY,
    name    TEXT,
    visible BOOLEAN,
    city_id INT
);
CREATE TABLE candidates
(
    id          SERIAL PRIMARY KEY,
    name        TEXT,
    description TEXT,
    created     TIMESTAMP default localtimestamp,
    visible     BOOLEAN,
    photo       BYTEA
);
CREATE TABLE users
(
    id          SERIAL PRIMARY KEY,
    email       TEXT,
    password    TEXT
);
ALTER TABLE users ADD CONSTRAINT email_unique UNIQUE (email);
