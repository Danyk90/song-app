-- schema.sql for resource-service
CREATE TABLE mp3_file (
    id SERIAL PRIMARY KEY,
    data BYTEA NOT NULL
);