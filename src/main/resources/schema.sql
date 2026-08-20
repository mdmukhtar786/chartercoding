-- Drop tables if they exist (safe re-run)
DROP TABLE IF EXISTS transaction;
DROP TABLE IF EXISTS customer;

-- Customer table
CREATE TABLE customer (
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    name       VARCHAR(100) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE
);

-- Transaction table
CREATE TABLE transaction (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    customer_id     BIGINT NOT NULL,
    amount          DECIMAL(10, 2) NOT NULL,
    transaction_date DATE NOT NULL,
    CONSTRAINT fk_customer FOREIGN KEY (customer_id) REFERENCES customer(id)
);
