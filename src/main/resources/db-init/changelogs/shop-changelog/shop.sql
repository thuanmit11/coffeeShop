CREATE EXTENSION IF NOT EXISTS cube;
CREATE EXTENSION IF NOT EXISTS earthdistance;

CREATE TABLE shops
(
    id               SERIAL PRIMARY KEY,
    name             VARCHAR(255) NOT NULL,
    location         VARCHAR(255) NOT NULL,
    latitude         DECIMAL,
    longitude        DECIMAL,
    contact_details  VARCHAR(255) NOT NULL,
    max_queue_size   INT          NOT NULL,
    number_of_queues INT          NOT NULL,
    opening_time     TIME         NOT NULL,
    closing_time     TIME         NOT NULL
);

INSERT INTO shops (name, location, longitude, latitude, contact_details, max_queue_size, number_of_queues, opening_time,
                   closing_time)
VALUES ('Central Coffee', '123 Main St, New York, NY', -72.1928375355868, 40.9609608, '123-456-7890', 2, 1, '06:00:00',
        '20:00:00'),
       ('Downtown Espresso', '456 Market St, San Francisco, CA', -122.398638142857, 37.7912469693878, '987-654-3210', 2,
        2, '07:00:00', '22:00:00'),
       ('The Coffee Corner', '789 Broadway, Los Angeles, CA', -118.489994382353, 34.0180949411765, '555-555-5555', 1, 3,
        '05:30:00', '19:30:00');
