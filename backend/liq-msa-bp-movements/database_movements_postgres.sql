

DROP TABLE IF EXISTS movements CASCADE;

-- Create movements table
CREATE TABLE movements (
    movement_id BIGSERIAL PRIMARY KEY,
    movement_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    client_id INTEGER NOT NULL,
    account_id INTEGER NOT NULL,
    movement_type VARCHAR(50) NOT NULL CHECK (movement_type IN ('DEPOSITO', 'RETIRO')),
    initial_balance DECIMAL(15,2) NOT NULL,
    movement_status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE' CHECK (movement_status IN ('ACTIVE', 'CANCELED', 'PENDING')),
    amount DECIMAL(15,2) NOT NULL,
    available_balance DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Foreign Keys
    CONSTRAINT fk_movements_account FOREIGN KEY (account_id) REFERENCES account(id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT fk_movements_client FOREIGN KEY (client_id) REFERENCES customer(client_id) ON DELETE RESTRICT ON UPDATE CASCADE
);
