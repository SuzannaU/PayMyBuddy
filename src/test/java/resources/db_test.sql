-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0;
SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0;
SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE =
        'ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Database PayMyBuddy_test
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Database PayMyBuddy_test
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS PayMyBuddy_test DEFAULT CHARACTER SET utf8;
USE PayMyBuddy_test;

-- -----------------------------------------------------
-- Table PayMyBuddy_test.users
-- -----------------------------------------------------
DROP TABLE IF EXISTS PayMyBuddy_test.users;

CREATE TABLE IF NOT EXISTS PayMyBuddy_test.users
(
    user_id  INT            NOT NULL AUTO_INCREMENT,
    username VARCHAR(45)    NOT NULL UNIQUE,
    email    VARCHAR(100)   NOT NULL UNIQUE,
    password VARCHAR(250)   NOT NULL,
    balance  DECIMAL(10, 2) NULL,
    PRIMARY KEY (user_id)
)
    ENGINE = InnoDB;

INSERT INTO PayMyBuddy_test.users (username, email, password, balance)
VALUES ('user1', 'user1@example.com', '$2a$10$WKfBRvdtqBKR9wVgtBJ01eF7PQxr7L9ZfYIMoqvA8kOHZP1oRaxq6',
        100.00), -- password1 Bcrypt 10X encoded
       ('user2', 'user2@example.com', '$2a$10$jZ83XbNy9RiaKhQJceBI0uD/navfEy6Mn1bqRvrAHEG8rYPJNarjy',
        200.00), -- password2 Bcrypt 10X encoded
       ('user3', 'user3@example.com', '$2a$10$2GfbCe/Ys76VgUdpTw0xje.5FnAxbjswczCL636SBEEWKadZgiabC', 0.00);
-- password3 Bcrypt 10X encoded

-- -----------------------------------------------------
-- Table PayMyBuddy_test.transactions
-- -----------------------------------------------------
DROP TABLE IF EXISTS PayMyBuddy_test.transactions;

CREATE TABLE IF NOT EXISTS PayMyBuddy_test.transactions
(
    transaction_id INT            NOT NULL AUTO_INCREMENT,
    sender_id      INT            NOT NULL,
    receiver_id    INT            NOT NULL,
    description    VARCHAR(250)   NULL,
    amount         DECIMAL(10, 2) NOT NULL,
    fee            DECIMAL(10, 2) NULL,
    PRIMARY KEY (transaction_id),
    INDEX sender_id_idx (sender_id ASC),
    INDEX receiver_id_idx (receiver_id ASC),
    CONSTRAINT fk_sender
        FOREIGN KEY (sender_id)
            REFERENCES PayMyBuddy_test.users (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_receiver
        FOREIGN KEY (receiver_id)
            REFERENCES PayMyBuddy_test.users (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table PayMyBuddy_test.roles
-- -----------------------------------------------------
DROP TABLE IF EXISTS PayMyBuddy_test.roles;

CREATE TABLE IF NOT EXISTS PayMyBuddy_test.roles
(
    role_name VARCHAR(25) NOT NULL UNIQUE,
    PRIMARY KEY (role_name)
)
    ENGINE = InnoDB;

INSERT INTO PayMyBuddy_test.roles (role_name)
VALUES ('ADMIN'),
       ('USER');


-- -----------------------------------------------------
-- Table PayMyBuddy_test.user_role
-- -----------------------------------------------------
DROP TABLE IF EXISTS PayMyBuddy_test.user_role;

CREATE TABLE IF NOT EXISTS PayMyBuddy_test.user_role
(
    user_id   INT         NOT NULL,
    role_name VARCHAR(25) NOT NULL,
    PRIMARY KEY (user_id, role_name),
    CONSTRAINT user_id
        FOREIGN KEY (user_id)
            REFERENCES PayMyBuddy_test.users (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT role_name
        FOREIGN KEY (role_name)
            REFERENCES PayMyBuddy_test.roles (role_name)
            ON DELETE NO ACTION
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

INSERT INTO PayMyBuddy_test.user_role (user_id, role_name)
VALUES (1, 'ADMIN'),
       (1, 'USER'),
       (2, 'USER'),
       (3, 'USER');

-- -----------------------------------------------------
-- Table PayMyBuddy_test.user_user
-- -----------------------------------------------------
DROP TABLE IF EXISTS PayMyBuddy_test.user_user;

CREATE TABLE IF NOT EXISTS PayMyBuddy_test.user_user
(
    user1_id INT NOT NULL,
    user2_id INT NOT NULL,
    PRIMARY KEY (user1_id, user2_id),
    CONSTRAINT fk_user1
        FOREIGN KEY (user1_id)
            REFERENCES PayMyBuddy_test.users (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE,
    CONSTRAINT fk_user2
        FOREIGN KEY (user2_id)
            REFERENCES PayMyBuddy_test.users (user_id)
            ON DELETE CASCADE
            ON UPDATE CASCADE
)
    ENGINE = InnoDB;

INSERT INTO PayMyBuddy_test.user_user (user1_id, user2_id)
VALUES (1, 2),
       (1, 3);

-- -----------------------------------------------------
-- View PayMyBuddy_test.user_1connection_vw
-- -----------------------------------------------------
DROP VIEW IF EXISTS PayMyBuddy_test.user_1connection_vw;

CREATE VIEW user_1connection_vw AS
(
SELECT *
FROM users
         JOIN user_user ON users.user_id = user_user.user1_id);

-- -----------------------------------------------------
-- View PayMyBuddy_test.user_connections_vw
-- ----------------------------------------------------
DROP VIEW IF EXISTS PayMyBuddy_test.user_connections_vw;

CREATE VIEW user_connections_vw AS
(
SELECT users.username               AS 'User1',
       users.email                  AS 'User1_email',
       user_1connection_vw.username AS 'User2',
       user_1connection_vw.email    AS 'User2_email'
from users
         join user_1connection_vw on users.user_id = user_1connection_vw.user2_id);


SET SQL_MODE = @OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS;