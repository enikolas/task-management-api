CREATE EXTENSION IF NOT EXISTS citext;

CREATE TABLE users
(
    id            UUID PRIMARY KEY,
    full_name     VARCHAR(255) NOT NULL,
    email         CITEXT       NOT NULL,
    password_hash VARCHAR(255) NOT NULL,
    deleted_at    TIMESTAMP,

    created_at    TIMESTAMP    NOT NULL DEFAULT now(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT now(),
    created_by    VARCHAR(255),
    updated_by    VARCHAR(255)
);

CREATE UNIQUE INDEX uq_users_email_active
    ON users (email)
    WHERE deleted_at IS NULL;
