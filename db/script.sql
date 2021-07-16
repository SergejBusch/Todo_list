CREATE TABLE items (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR NOT NULL,
                         description VARCHAR NOT NULL,
                         created timestamp,
                         done boolean
);