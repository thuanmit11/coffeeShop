CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    menu_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    status VARCHAR(255) NOT NULL,
    queue_id BIGINT,
    FOREIGN KEY (menu_id) REFERENCES menu(id),
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (queue_id) REFERENCES queues(id)
);
