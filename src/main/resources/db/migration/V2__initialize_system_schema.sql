BEGIN TRANSACTION;

CREATE SCHEMA IF NOT EXISTS system;
CREATE TABLE system.active_token
(
    id           SERIAL PRIMARY KEY,
    token        VARCHAR(1000) NOT NULL,
    created_date TIMESTAMP     NOT NULL,
    token_type   INT           NOT NULL,
    user_id      BIGINT        NOT NULL REFERENCES management.app_user (id)
);

COMMIT TRANSACTION ;