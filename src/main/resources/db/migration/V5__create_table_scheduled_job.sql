CREATE TABLE system.scheduled_job
(
    id                      SERIAL PRIMARY KEY,
    name                    VARCHAR(255) NOT NULL,
    occur_date              TIMESTAMP    NOT NULL default now(),
    impacted_accounts_count INTEGER      NOT NULL default 0,
    status                  INTEGER      NOT NULL,
    failure_reason          VARCHAR(255),
    start_by                VARCHAR(255) NOT NULL
)