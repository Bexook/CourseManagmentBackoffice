BEGIN TRANSACTION;

ALTER TABLE management.media
    ADD COLUMN upload_status VARCHAR(150);

CREATE TABLE management.homework
(
    id            BIGSERIAL PRIMARY KEY,
    app_user_id   SERIAL REFERENCES management.app_user (id),
    media_id      SERIAL REFERENCES management.media (id),
    grade         INT,
    teacher_notes TEXT,
    evaluated     BOOLEAN,
    created_date  TIMESTAMP NOT NULL DEFAULT now(),
    updated_date  TIMESTAMP,
    created_by    BIGINT    NOT NULL,
    updated_by    VARCHAR(255),
    CONSTRAINT app_user_course_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email),
    CONSTRAINT app_user_course_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id)
);

COMMIT TRANSACTION;

