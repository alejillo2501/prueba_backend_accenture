-- Tabla: franchises
CREATE TABLE IF NOT EXISTS franchises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Tabla: branches
CREATE TABLE IF NOT EXISTS branches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (franchise_id) REFERENCES franchises(id) ON DELETE CASCADE,
    INDEX idx_branches_franchise (franchise_id)
);

-- Tabla: products (con campo precio)
CREATE TABLE IF NOT EXISTS products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    precio DECIMAL(10, 2) NOT NULL DEFAULT 0.00,  -- NUEVO CAMPO
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE,
    INDEX idx_products_branch (branch_id),
    INDEX idx_products_stock (stock DESC)
);

-- ============================================
-- DATOS DE EJEMPLO
-- ============================================

-- Franquicia: TechStore
INSERT INTO franchises (name) VALUES ('TechStore');
SET @franchise_techstore = LAST_INSERT_ID();

-- Sucursales de TechStore
INSERT INTO branches (franchise_id, name) VALUES 
    (@franchise_techstore, 'Sucursal Centro');
SET @branch_centro = LAST_INSERT_ID();

INSERT INTO branches (franchise_id, name) VALUES 
    (@franchise_techstore, 'Sucursal Norte');
SET @branch_norte = LAST_INSERT_ID();

-- Productos de Sucursal Centro
INSERT INTO products (branch_id, name, stock, precio) VALUES
    (@branch_centro, 'Laptop Dell XPS 15', 15, 1299.99),
    (@branch_centro, 'Mouse Logitech MX', 50, 79.99),
    (@branch_centro, 'Teclado Mecánico RGB', 30, 149.99),
    (@branch_centro, 'Monitor 4K 27"', 20, 599.99),
    (@branch_centro, 'Auriculares Bluetooth', 100, 89.99);

-- Productos de Sucursal Norte
INSERT INTO products (branch_id, name, stock, precio) VALUES
    (@branch_norte, 'Laptop Dell XPS 15', 8, 1299.99),
    (@branch_norte, 'Mouse Logitech MX', 25, 79.99),
    (@branch_norte, 'Webcam 4K', 15, 199.99),
    (@branch_norte, 'USB-C Hub', 40, 49.99),
    (@branch_norte, 'Tablet Samsung', 12, 449.99);

-- Franquicia: SuperFood
INSERT INTO franchises (name) VALUES ('SuperFood');
SET @franchise_superfood = LAST_INSERT_ID();

-- Sucursales de SuperFood
INSERT INTO branches (franchise_id, name) VALUES 
    (@franchise_superfood, 'Sucursal A');
SET @branch_a = LAST_INSERT_ID();

-- Productos de SuperFood Sucursal A
INSERT INTO products (branch_id, name, stock, precio) VALUES
    (@branch_a, 'Arroz 1kg', 200, 2.50),
    (@branch_a, 'Frijoles 500g', 150, 1.80),
    (@branch_a, 'Leche Descremada', 80, 1.20),
    (@branch_a, 'Pollo Fresco 1kg', 50, 5.99),
    (@branch_a, 'Pan Integral', 100, 2.00);

-- Verificar datos insertados
SELECT f.name as Franquicia, b.name as Sucursal, p.name as Producto, p.stock, p.precio
FROM franchises f
JOIN branches b ON f.id = b.franchise_id
JOIN products p ON b.id = p.branch_id;