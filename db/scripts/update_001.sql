CREATE TABLE posts
(
    id      SERIAL PRIMARY KEY,
    name    TEXT,
    visible BOOLEAN,
    city_id INT
);
