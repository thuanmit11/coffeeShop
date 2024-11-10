CREATE TABLE roles
(
    id          INT PRIMARY KEY,
    name        VARCHAR(255)   NOT NULL
);

INSERT INTO roles VALUES (1,'ADMIN'), (2,'CUSTOMER'), (3,'OPERATOR');