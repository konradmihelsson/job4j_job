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
