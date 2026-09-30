CREATE TABLE coupons (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(255) NOT NULL UNIQUE,
    discount_percentage DECIMAL(5, 2),
    usage_limit INT,
    used_count INT DEFAULT 0,
    expiry_date TIMESTAMP,
    is_active BOOLEAN NOT NULL DEFAULT TRUE
);