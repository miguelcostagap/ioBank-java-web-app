DROP TABLE IF EXISTS account;
DROP TABLE IF EXISTS customer;

CREATE TABLE customer
(
    id            SERIAL PRIMARY KEY,
    email         VARCHAR(255),
    first_name    VARCHAR(255),
    last_name     VARCHAR(255),
    phone         VARCHAR(255),
    photo_url     VARCHAR(255),
    creation_time TIMESTAMP,
    update_time   TIMESTAMP
);

CREATE TABLE account
(
    account_type  VARCHAR(255) NOT NULL,
    id            SERIAL PRIMARY KEY,
    balance       NUMERIC      NOT NULL,
    customer_id   INT          NOT NULL,
    creation_time TIMESTAMP,
    update_time   TIMESTAMP,
    FOREIGN KEY (customer_id) REFERENCES customer (id)
);

INSERT INTO customer (creation_time, update_time, first_name, last_name, email, phone, photo_url)
VALUES (TIMESTAMP '2018-11-11 09:44:44', TIMESTAMP '2020-11-11 09:55:55', 'Joao', 'Oliveira', 'oliveirea@gamil.com',
        '919983743', 'user-profile-3.png'),
       (TIMESTAMP '2018-01-11 09:24:44', TIMESTAMP '2020-01-11 09:35:55', 'Joao', 'Townsend', 'townsend@gamil.com',
        '919983563', 'user-profile-2.png'),
       (TIMESTAMP '2018-12-11 09:34:44', TIMESTAMP '2020-12-11 09:25:55', 'Sara', 'Lopes', 'lopes@gamil.com',
        '919565743', 'user-profile-1.png');

INSERT INTO account (account_type, creation_time, update_time, balance, customer_id)
VALUES ('CHECKING', TIMESTAMP '2018-11-11 19:44:44', TIMESTAMP '2020-11-11 19:14:44', '1111.1', 1),
       ('SAVINGS', TIMESTAMP '2018-11-12 19:44:44', TIMESTAMP '2020-11-12 19:14:44', '544.1', 1),
       ('CHECKING', TIMESTAMP '2018-11-13 19:44:44', TIMESTAMP '2020-11-13 19:14:44', '0', 1),
       ('CHECKING', TIMESTAMP '2018-11-14 19:44:44', TIMESTAMP '2020-11-14 19:14:44', '111121.1', 2),
       ('SAVINGS', TIMESTAMP '2018-11-15 19:44:44', TIMESTAMP '2020-11-15 19:14:44', '2122.1', 2),
       ('CHECKING', TIMESTAMP '2018-11-16 19:44:44', TIMESTAMP '2020-11-16 19:14:44', '11111.1', 3),
       ('SAVINGS', TIMESTAMP '2018-11-17 19:44:44', TIMESTAMP '2020-11-17 19:14:44', '90.1', 3);
