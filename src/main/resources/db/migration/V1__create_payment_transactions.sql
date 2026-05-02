CREATE TABLE IF NOT EXISTS payment_transactions (
    id BIGSERIAL PRIMARY KEY,
    external_id VARCHAR(100),
    customer_name VARCHAR(120) NOT NULL,
    email VARCHAR(120) NOT NULL,
    description VARCHAR(255) NOT NULL,
    amount NUMERIC(19,2) NOT NULL,
    status VARCHAR(30) NOT NULL,
    payment_method VARCHAR(40),
    created_at TIMESTAMP WITH TIME ZONE,
    updated_at TIMESTAMP WITH TIME ZONE
);
