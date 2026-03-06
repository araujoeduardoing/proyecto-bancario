DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS person CASCADE;

-- Eliminar secuencias si existen
DROP SEQUENCE IF EXISTS person_person_id_seq CASCADE;

CREATE TABLE person (
    person_id BIGSERIAL NOT NULL,
    name VARCHAR(100) NOT NULL,
    gender VARCHAR(100),
    age INTEGER,
    identification VARCHAR(100) NOT NULL,
    address VARCHAR(100),
    phone VARCHAR(100),
    
    -- Claves primarias y restricciones
    PRIMARY KEY (person_id),
    CONSTRAINT uk_person_identification UNIQUE (identification)
);

CREATE TABLE customer (
    person_id BIGINT NOT NULL,
    client_id BIGSERIAL NOT NULL,
    password VARCHAR(100) NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    
    -- Claves primarias y restricciones
    PRIMARY KEY (person_id),
    CONSTRAINT uk_customer_client_id UNIQUE (client_id),
    
    -- Clave foránea hacia person
    CONSTRAINT fk_customer_person 
        FOREIGN KEY (person_id) 
        REFERENCES person(person_id) 
        ON DELETE CASCADE 
        ON UPDATE CASCADE
);

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

-- Inserción de datos iniciales para usuarios
-- Insertar personas
INSERT INTO person (name, identification, address, phone) VALUES 
('Jose Lema', '1234567890', 'Otavalo sn y principal', '098254785'),
('Marianela Montalvo', '0987654321', 'Amazonas y NNUU', '097548965'),
('Juan Osorio', '1122334455', '13 junio y Equinoccial', '098874587');

-- Insertar clientes/usuarios (usando los person_id generados automáticamente)
INSERT INTO customer (person_id, password, status) VALUES 
((SELECT person_id FROM person WHERE identification = '1234567890'), '1234', true),
((SELECT person_id FROM person WHERE identification = '0987654321'), '5678', true),
((SELECT person_id FROM person WHERE identification = '1122334455'), '1245', true);

-- Inserción de cuentas para los usuarios
INSERT INTO account (account_number, account_type, initial_balance, status, client_id) VALUES 
('478758', 'Ahorro', 2000.00, true, 
    (SELECT c.client_id FROM customer c 
     JOIN person p ON c.person_id = p.person_id 
     WHERE p.name = 'Jose Lema')),
('225487', 'Corriente', 100.00, true, 
    (SELECT c.client_id FROM customer c 
     JOIN person p ON c.person_id = p.person_id 
     WHERE p.name = 'Marianela Montalvo')),
('495878', 'Ahorros', 0.00, true, 
    (SELECT c.client_id FROM customer c 
     JOIN person p ON c.person_id = p.person_id 
     WHERE p.name = 'Juan Osorio')),
('496825', 'Ahorros', 540.00, true, 
    (SELECT c.client_id FROM customer c 
     JOIN person p ON c.person_id = p.person_id 
     WHERE p.name = 'Marianela Montalvo'));

-- Inserción de movimientos iniciales
INSERT INTO movements (client_id, account_id, movement_type, initial_balance, amount, available_balance) VALUES 
-- Retiro de 575 en cuenta 478758 (Jose Lema)
((SELECT c.client_id FROM customer c 
  JOIN person p ON c.person_id = p.person_id 
  WHERE p.name = 'Jose Lema'),
 (SELECT id FROM account WHERE account_number = '478758'),
 'RETIRO', 2000.00, 575.00, 1425.00),

-- Depósito de 600 en cuenta 225487 (Marianela Montalvo)
((SELECT c.client_id FROM customer c 
  JOIN person p ON c.person_id = p.person_id 
  WHERE p.name = 'Marianela Montalvo' LIMIT 1),
 (SELECT id FROM account WHERE account_number = '225487'),
 'DEPOSITO', 100.00, 600.00, 700.00),

-- Depósito de 150 en cuenta 495878 (Juan Osorio)
((SELECT c.client_id FROM customer c 
  JOIN person p ON c.person_id = p.person_id 
  WHERE p.name = 'Juan Osorio'),
 (SELECT id FROM account WHERE account_number = '495878'),
 'DEPOSITO', 0.00, 150.00, 150.00),

-- Retiro de 540 en cuenta 496825 (Marianela Montalvo)
((SELECT c.client_id FROM customer c 
  JOIN person p ON c.person_id = p.person_id 
  WHERE p.name = 'Marianela Montalvo' LIMIT 1),
 (SELECT id FROM account WHERE account_number = '496825'),
 'RETIRO', 540.00, 540.00, 0.00);
