DELETE
FROM customer;
INSERT INTO customer(CREATIONTIME, UPDATETIME, VERSION, FIRSTNAME, LASTNAME, EMAIL, PHONE, PHOTOURL)
VALUES (TIMESTAMP '2017-10-10 08:45:56.468', TIMESTAMP '2017-10-10 08:45:56.468', 0, 'João', 'Oliveira',
        'oliveira@gmail.com', '111222333', 'user-profile-3.png'),
       (TIMESTAMP '2017-10-10 08:45:56.481', TIMESTAMP '2017-10-10 08:45:56.481', 0, 'João', 'Townsend',
        'townsend@gmail.com', '444555666', 'user-profile-2.png'),
       (TIMESTAMP '2017-10-10 08:45:56.482', TIMESTAMP '2017-10-10 08:45:56.482', 0, 'Sara', 'Lopes',
        'lopes@gmail.com', '777888999', 'user-profile.png');

DELETE
FROM account;
INSERT INTO account(ACCOUNT_TYPE, CREATIONTIME, UPDATETIME, VERSION, BALANCE, CUSTOMER_ID)
VALUES ('CheckingAccount', TIMESTAMP '2017-10-10 10:18:53.819', TIMESTAMP '2017-10-10 10:22:58.578', 2, 100.0, 1),
       ('SavingsAccount', TIMESTAMP '2017-10-10 10:23:02.194', TIMESTAMP '2017-10-10 10:23:19.801', 1, 50.5, 1),
       ('CheckingAccount', TIMESTAMP '2017-10-10 14:30:37.769', TIMESTAMP '2017-10-10 14:30:43.042', 1, 10.0, 2),
       ('SavingsAccount', TIMESTAMP '2017-10-10 14:30:38.426', TIMESTAMP '2017-10-10 14:30:46.471', 1, 150.0, 2),
       ('CheckingAccount', TIMESTAMP '2017-10-10 14:30:37.769', TIMESTAMP '2017-10-10 14:30:43.042', 1, 0.0, 1),
       ('CheckingAccount', TIMESTAMP '2017-10-10 14:30:38.426', TIMESTAMP '2017-10-10 14:30:46.471', 1, 0.0, 2),
       ('CheckingAccount', TIMESTAMP '2017-10-10 14:30:37.769', TIMESTAMP '2017-10-10 14:30:43.042', 1, 20.5, NULL);

DELETE
FROM recipient;
INSERT INTO recipient(CREATIONTIME, UPDATETIME, VERSION, ACCOUNTNUMBER, NAME, EMAIL, DESCRIPTION, PHONE,
                      CUSTOMER_ID)
VALUES (TIMESTAMP '2017-10-10 08:45:56.468', TIMESTAMP '2017-10-10 08:45:56.468', 1, 1, 'João Oliveira',
        'oliveira@gmail.com', 'My colleague Townsend from A/C', '11122233', 2),
       ( TIMESTAMP '2017-10-10 08:45:56.468', TIMESTAMP '2017-10-10 08:45:56.468', 1, 3, 'João Townsend',
        'townsend@gmail.com', 'My colleague Oliveira from A/C', '222333444', 1);