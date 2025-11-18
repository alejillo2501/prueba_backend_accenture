CREATE TABLE franchises (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE branches (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    franchise_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    FOREIGN KEY (franchise_id) REFERENCES franchises(id) ON DELETE CASCADE
);

CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    branch_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    stock INT NOT NULL DEFAULT 0,
    FOREIGN KEY (branch_id) REFERENCES branches(id) ON DELETE CASCADE
);

CREATE INDEX idx_branches_franchise ON branches(franchise_id);
CREATE INDEX idx_products_branch ON products(branch_id);
CREATE INDEX idx_products_stock ON products(stock DESC);