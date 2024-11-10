CREATE TABLE menu (
    id SERIAL PRIMARY KEY,
    shop_id INT NOT NULL,
    item_name VARCHAR(255) NOT NULL,
    description VARCHAR(255) NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (shop_id) REFERENCES shops(id)
);

INSERT INTO menu (shop_id, item_name, description, price)
VALUES (1, 'Espresso', 'Rich and bold espresso shot', 2.50),
       (1, 'Latte', 'Espresso with steamed milk', 3.50),
       (2, 'Cappuccino', 'Espresso with steamed milk and foam', 3.75),
       (2, 'Mocha', 'Espresso with chocolate and steamed milk', 4.00),
       (3, 'Americano', 'Espresso diluted with water', 2.75),
       (3, 'Flat White', 'Espresso with micro-foam milk', 3.50);
