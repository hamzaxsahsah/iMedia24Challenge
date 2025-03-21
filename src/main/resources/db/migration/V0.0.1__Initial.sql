CREATE TABLE products
(
    sku         VARCHAR(16)     NOT NULL
        CONSTRAINT pk_product_id PRIMARY KEY,
    name        VARCHAR(125)    NOT NULL,
    description VARCHAR(125),
    price       DECIMAL         NOT NULL,
    stock       INT             NOT NULL DEFAULT 0,
    created_at  TIMESTAMP       NOT NULL,
    updated_at  TIMESTAMP       NOT NULL
);