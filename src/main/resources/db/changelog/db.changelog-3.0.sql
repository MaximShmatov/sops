INSERT INTO roles(role_name)
VALUES ('POSTER'),
       ('PROCESSOR');

INSERT INTO order_status(status)
VALUES ('READY'),
       ('IN_PROCESS'),
       ('PROCESSED');

INSERT INTO users(user_name, password, email, role)
VALUES ('User1', '$2a$10$e1GhWgjtZ6nrYjz8rx7Zuuoay64ezWNngHGmuY9i.toO7ZmI7dvNu', 'user1@mail.org', 'POSTER'),
       ('User2', '$2a$10$xqHKyMvWb2T3jisJEsQBQuDbK4R03tIBThlSZ8oMoT85SZZOfjJD6', 'user2@mail.org', 'POSTER'),
       ('User3', '$2a$10$XAuqsc9DIDg8r/BbG0TcpOcxm7x9c92i/zzAA0K./vb3hv2gBb6QC', 'user3@mail.org', 'PROCESSOR');

INSERT INTO orders(title, description, status, created_by, processed_by)
VALUES ('Title1', 'Description1', 'READY', 1, 3),
       ('Title2', 'Description2', 'READY', 2, 3),
       ('Title3', 'Description3', 'IN_PROCESS', 3, 2),
       ('Title4', 'Description4', 'IN_PROCESS', 3, 3),
       ('Title5', 'Description5', 'PROCESSED', 3, 3);