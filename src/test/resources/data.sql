CREATE SCHEMA IF NOT EXISTS management;

CREATE TABLE management.app_user
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name       VARCHAR(255)        NOT NULL,
    last_name        VARCHAR(255)        NOT NULL,
    email            VARCHAR(255) UNIQUE NOT NULL,
    password         VARCHAR(500)        NOT NULL,
    role             INT                 NOT NULL,
    certificate_keys VARCHAR(1500),
    active           BOOLEAN             NOT NULL DEFAULT false,
    email_verified   BOOLEAN             NOT NULL DEFAULT false,
    created_date     TIMESTAMP           NOT NULL DEFAULT now(),
    updated_date     TIMESTAMP
);


CREATE TABLE management.course
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject          VARCHAR(500) NOT NULL,
    description      TEXT,
    course_principal BIGINT       NOT NULL,
    created_date     TIMESTAMP    NOT NULL DEFAULT now(),
    updated_date     TIMESTAMP,
    created_by       BIGINT       NOT NULL,
    updated_by       VARCHAR(255),

    CONSTRAINT app_user_course_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email),
    CONSTRAINT app_user_course_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id),
    CONSTRAINT app_user_course_FK FOREIGN KEY (course_principal) REFERENCES management.app_user (id)
);


CREATE TABLE management.lesson
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    subject      VARCHAR(500) NOT NULL,
    lesson_text  TEXT,
    index        INT          NOT NULL,
    created_by   BIGINT       NOT NULL,
    updated_by   VARCHAR(255),
    created_date TIMESTAMP    NOT NULL DEFAULT now(),
    updated_date TIMESTAMP,
    course_id    BIGINT REFERENCES management.course (id),

    CONSTRAINT app_user_lesson_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id),
    CONSTRAINT app_user_lesson_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email)

);


CREATE TABLE management.course_user_authority
(

    course_id   BIGINT NOT NULL REFERENCES management.course (id),
    user_id     BIGINT NOT NULL REFERENCES management.app_user (id),
    authorities VARCHAR(500),

    CONSTRAINT course_id_course_authority_FK FOREIGN KEY (course_id) REFERENCES management.course (id),
    CONSTRAINT user_course_FK FOREIGN KEY (user_id) REFERENCES management.app_user (id)

);


CREATE TABLE management.app_user_course_reference
(
    app_user_id  BIGINT NOT NULL REFERENCES management.app_user (id),
    course_id    BIGINT NOT NULL REFERENCES management.course (id),
    status       INT,
    lesson_index INT    NOT NULL
);

CREATE TABLE management.app_user_exam_reference
(
    id              BIGINT PRIMARY KEY AUTO_INCREMENT,
    app_user_id     BIGINT NOT NULL REFERENCES management.app_user (id),
    course_id       BIGINT NOT NULL REFERENCES management.course (id),
    grade           INT    NOT NULL,
    evaluation_date DATE   NOT NULL,

    CONSTRAINT app_user_id_app_user_exam_FK FOREIGN KEY (app_user_id) REFERENCES management.app_user (id),
    CONSTRAINT course_id_course_FK FOREIGN KEY (course_id) REFERENCES management.course (id)
);



CREATE TABLE management.exam
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    course_id    BIGINT    NOT NULL REFERENCES management.course (id),
    max_grade    INT,
    min_grade    INT,
    created_by   BIGINT    NOT NULL,
    updated_by   VARCHAR(255),
    created_date TIMESTAMP NOT NULL DEFAULT now(),
    updated_date TIMESTAMP,

    CONSTRAINT app_user_exam_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id),
    CONSTRAINT app_user_exam_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email)
);


CREATE TABLE management.question
(
    id               BIGINT PRIMARY KEY AUTO_INCREMENT,
    exam_id          BIGINT    NOT NULL REFERENCES management.exam (id),
    question_text    TEXT      NOT NULL,
    amount_of_points INT       NOT NULL,
    index            INT       NOT NULL,
    created_by       BIGINT    NOT NULL,
    updated_by       VARCHAR(255),
    created_date     TIMESTAMP NOT NULL DEFAULT now(),
    updated_date     TIMESTAMP,

    CONSTRAINT app_user_question_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id),
    CONSTRAINT app_user_question_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email)

);


CREATE TABLE management.answer
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    question_id  BIGINT REFERENCES management.question (id),
    answer_value TEXT      NOT NULL,
    right_answer BOOLEAN   NOT NULL,
    created_by   BIGINT    NOT NULL,
    updated_by   VARCHAR(255),
    created_date TIMESTAMP NOT NULL DEFAULT now(),
    updated_date TIMESTAMP,

    CONSTRAINT app_user_answer_created_by_FK FOREIGN KEY (created_by) REFERENCES management.app_user (id),
    CONSTRAINT app_user_answer_updated_by_FK FOREIGN KEY (updated_by) REFERENCES management.app_user (email)

);

CREATE TABLE management.media
(
    id         BIGINT PRIMARY KEY AUTO_INCREMENT,
    lesson_id  BIGINT REFERENCES management.lesson (id),
    media_type INT NOT NULL,
    key        VARCHAR(500)
);

CREATE TABLE management.exam_result
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT REFERENCES management.app_user (id),
    exam_id      BIGINT REFERENCES management.exam (id),
    status       VARCHAR(80) NOT NULL DEFAULT 'DRAFT',
    answers_json TEXT        NOT NULL

);


CREATE SCHEMA IF NOT EXISTS system;
CREATE TABLE system.active_token
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    token        VARCHAR(1000) NOT NULL,
    created_date TIMESTAMP     NOT NULL,
    token_type   INT           NOT NULL,
    user_id      BIGINT        NOT NULL REFERENCES management.app_user (id)
);



CREATE TABLE system.scheduled_job
(
    id                      BIGINT PRIMARY KEY AUTO_INCREMENT,
    name                    VARCHAR(255) NOT NULL,
    occur_date              TIMESTAMP    NOT NULL default now(),
    impacted_accounts_count INTEGER      NOT NULL default 0,
    status                  INTEGER      NOT NULL,
    failure_reason          VARCHAR(255),
    start_by                VARCHAR(255) NOT NULL
);

ALTER TABLE management.course
    ADD COLUMN available BOOLEAN DEFAULT false;

INSERT INTO management.app_user (id, first_name, last_name, email, password, role, certificate_keys, active,
                                 email_verified,
                                 created_date, updated_date)
VALUES (1, 'Admin', 'TEST', 'admin@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 2,
        null, true, true, now(), null),
       (2, 'Student', 'TEST', 'student@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 0,
        null, true, true, now(), null),
       (3, 'Teacher', 'TEST', 'teacher@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 1,
        null, true, true, now(), null),
       (4, 'Teacher', 'TEST', 'teacher2@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 1,
        null, true, true, now(), null),
       (5, 'Admin', 'TEST', 'admin2@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 2,
        null, true, true, now(), null),
       (6, 'Student', 'TEST', 'student2@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 0,
        null, true, true, now(), null),
       (7, 'Student', 'TEST', 'student3@email.com', '$2a$12$ok0yBpD4F44qy5a0CTL9xewXj.JEKkKahd9t5PffAKvgLfPpDr3Yu', 0,
        null, true, true, now(), null);

INSERT INTO management.course
VALUES (3, 'TEST',
        'TEST', 3,
        '2023-02-07 16:38:46.708260', null, 1, null, false),
       (2, 'TEST',
        'TEST', 3,
        '2023-02-07 16:38:46.708260', null, 1, null, true);


INSERT INTO management.app_user_course_reference
VALUES (6, 2, 0, 0);

COMMIT
