CREATE TABLE IF NOT EXISTS brands (
    id   BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS products (
    id   BIGINT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS prices (
    id         UUID PRIMARY KEY,
    brand_id   BIGINT NOT NULL REFERENCES brands(id),
    start_date TIMESTAMP NOT NULL,
    end_date   TIMESTAMP NOT NULL,
    price_list INT NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(id),
    priority   INT NOT NULL,
    price      DECIMAL(10, 2) NOT NULL,
    curr       CHAR(3) NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_prices_lookup ON prices (brand_id, product_id, start_date, end_date);
