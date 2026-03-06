DROP TABLE IF EXISTS customer CASCADE;
DROP TABLE IF EXISTS person CASCADE;

-- Eliminar secuencias si existen
DROP SEQUENCE IF EXISTS person_person_id_seq CASCADE;

-- =====================================================
-- TABLA PERSON (Entidad Base)
-- =====================================================
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

-- =====================================================
-- TABLA CUSTOMER (Hereda de Person)
-- =====================================================
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

-- =====================================================
-- ÍNDICES ADICIONALES PARA OPTIMIZACIÓN
-- =====================================================

-- Índice para búsquedas por identificación
CREATE INDEX idx_person_identification ON person(identification);

-- Índice para búsquedas por nombre
CREATE INDEX idx_person_name ON person(name);

-- Índice para búsquedas por client_id
CREATE INDEX idx_customer_client_id ON customer(client_id);

-- Índice para búsquedas por estado activo
CREATE INDEX idx_customer_status ON customer(status);

-- Índice compuesto para consultas frecuentes
CREATE INDEX idx_person_name_identification ON person(name, identification);

-- =====================================================
-- COMENTARIOS EN TABLAS Y COLUMNAS
-- =====================================================

COMMENT ON TABLE person IS 'Tabla base para almacenar información de personas';
COMMENT ON COLUMN person.person_id IS 'Identificador único de la persona (PK)';
COMMENT ON COLUMN person.identification IS 'Número de identificación único (Cédula/Pasaporte)';

COMMENT ON TABLE customer IS 'Tabla para clientes que hereda de person';
COMMENT ON COLUMN customer.client_id IS 'Identificador único secuencial del cliente (auto-generado)';
COMMENT ON COLUMN customer.password IS 'Contraseña encriptada del cliente';
COMMENT ON COLUMN customer.status IS 'Estado activo/inactivo del cliente';

-- =====================================================
-- DATOS DE PRUEBA (OPCIONAL)
-- =====================================================

-- Insertar persona base
INSERT INTO person (name, gender, age, identification, address, phone) 
VALUES ('Juan Pérez', 'M', 30, '1234567890', 'Av. Principal 123', '+593987654321');

-- Insertar cliente (client_id se genera automáticamente)
INSERT INTO customer (person_id, password, status) 
VALUES (currval('person_person_id_seq'), '1234', TRUE);

-- Insertar otra persona
INSERT INTO person (name, gender, age, identification, address, phone) 
VALUES ('María González', 'F', 28, '0987654321', 'Calle Secundaria 456', '+593123456789');

-- Insertar otro cliente (client_id se genera automáticamente)
INSERT INTO customer (person_id, password, status) 
VALUES (currval('person_person_id_seq'), '1234', TRUE);

-- =====================================================
-- VISTAS ÚTILES
-- =====================================================

-- Vista para obtener información completa de clientes
CREATE OR REPLACE VIEW customer_full_info AS
SELECT 
    p.person_id,
    p.name,
    p.gender,
    p.age,
    p.identification,
    p.address,
    p.phone,
    c.client_id,
    c.status,
    CASE WHEN c.status THEN 'Activo' ELSE 'Inactivo' END as status_text
FROM person p
INNER JOIN customer c ON p.person_id = c.person_id;

-- =====================================================
-- CONSULTAS DE VERIFICACIÓN
-- =====================================================

-- Ver todos los customers con información completa
SELECT * FROM customer_full_info ORDER BY person_id;

-- Contar registros
SELECT 'person' as tabla, COUNT(*) as total FROM person
UNION ALL
SELECT 'customer' as tabla, COUNT(*) as total FROM customer;

-- Verificar secuencias
SELECT 
    schemaname,
    sequencename,
    last_value,
    increment_by
FROM pg_sequences 
WHERE sequencename LIKE '%person%' OR sequencename LIKE '%client%';

-- Verificar integridad referencial
SELECT 
    tc.constraint_name, 
    tc.table_name, 
    kcu.column_name,
    ccu.table_name AS foreign_table_name,
    ccu.column_name AS foreign_column_name 
FROM information_schema.table_constraints AS tc 
JOIN information_schema.key_column_usage AS kcu
  ON tc.constraint_name = kcu.constraint_name
  AND tc.table_schema = kcu.table_schema
JOIN information_schema.constraint_column_usage AS ccu
  ON ccu.constraint_name = tc.constraint_name
  AND ccu.table_schema = tc.table_schema
WHERE tc.constraint_type = 'FOREIGN KEY' 
AND tc.table_name IN ('customer', 'person');