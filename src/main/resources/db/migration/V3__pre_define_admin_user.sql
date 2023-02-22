BEGIN
TRANSACTION;

INSERT INTO management.app_user (first_name, last_name, email, password, role, certificate_keys, active, email_verified,
                                 created_date, updated_date)
VALUES ('Admin', 'TEST', 'test@email.com', '$2a$12$Onqopybl/uZPugsc0Nw3ZOMxDDPu.mx68ckCAUM.QQhn9253sGI.6', 2,
        null, true, true, now(), null),
       ('Student', 'TEST', 'student@email.com', '$2a$12$Onqopybl/uZPugsc0Nw3ZOMxDDPu.mx68ckCAUM.QQhn9253sGI.6', 0,
        null, true, true, now(), null),
       ('Teacher', 'TEST', 'teacher@email.com', '$2a$12$Onqopybl/uZPugsc0Nw3ZOMxDDPu.mx68ckCAUM.QQhn9253sGI.6', 1,
        null, true, true, now(), null);


COMMIT TRANSACTION;

