CREATE TABLE queues
(
    id           SERIAL PRIMARY KEY,
    shop_id      INT NOT NULL,
    max_size     INT NOT NULL,
    current_size INT NOT NULL,
    FOREIGN KEY (shop_id) REFERENCES shops (id)
);

INSERT INTO queues(shop_id, max_size, current_size)
VALUES (1, 2, 0);
INSERT INTO queues(shop_id, max_size, current_size)
VALUES (2, 2, 0);
INSERT INTO queues(shop_id, max_size, current_size)
VALUES (3, 1, 0);