-- schema.sql for song-service
-- schema.sql for songs_metadata table
CREATE TABLE songs_metadata (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    artist VARCHAR(255) NOT NULL,
    album VARCHAR(255) NOT NULL,
    duration VARCHAR(255),
    "year" VARCHAR(255) NOT NULL
);