CREATE TABLE geolocplace (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255),
  latitude FLOAT,
  longitude FLOAT,
  whoadded VARCHAR(255)
);

CREATE TABLE account (
  username VARCHAR(255) PRIMARY KEY,
  password VARCHAR(255),
  bio VARCHAR(255)
);

CREATE TABLE image (
  id SERIAL PRIMARY KEY,
  placeid INT REFERENCES geolocplace(id),
  media BYTEA,
  author VARCHAR(255) REFERENCES account(username)
);

CREATE TABLE song (
  id SERIAL PRIMARY KEY,
  placeid INT REFERENCES geolocplace(id),
  media BYTEA,
  author VARCHAR(255) REFERENCES account(username)
);

CREATE TABLE video (
  id SERIAL PRIMARY KEY,
  placeid INT REFERENCES geolocplace(id),
  media BYTEA,
  author VARCHAR(255) REFERENCES account(username)
);

CREATE TABLE comment (
  id SERIAL PRIMARY KEY,
  placeid INT REFERENCES geolocplace(id),
  username VARCHAR(255) REFERENCES account(username),
  message VARCHAR(255)
);

CREATE TABLE warning (
  id SERIAL PRIMARY KEY,
  placeid INT REFERENCES geolocplace(id),
  username VARCHAR(255) REFERENCES account(username),
  message VARCHAR(255)
);

CREATE TABLE follow (
  username VARCHAR(255) REFERENCES account(username),
  placeid INT REFERENCES geolocplace(id)
);