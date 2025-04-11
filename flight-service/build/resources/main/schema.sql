CREATE TABLE IF NOT EXISTS flight (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    airline VARCHAR(255) NOT NULL,
    departure_time TIMESTAMP NOT NULL,
    arrival_time TIMESTAMP NOT NULL,
    departure_loc VARCHAR(10) NOT NULL,
    arrival_loc VARCHAR(10) NOT NULL
);