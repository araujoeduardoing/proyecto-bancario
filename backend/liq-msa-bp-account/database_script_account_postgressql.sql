-- Account Table for PostgreSQL
DROP TABLE IF EXISTS account CASCADE;
CREATE TABLE account (
    id SERIAL PRIMARY KEY,
    account_number VARCHAR(20) UNIQUE NOT NULL,
    account_type VARCHAR(50) NOT NULL,
    initial_balance DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    status BOOLEAN NOT NULL DEFAULT true,
    client_id INTEGER NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (client_id) REFERENCES customer(client_id)
);