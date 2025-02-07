-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Database PayMyBuddy
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Database PayMyBuddy
-- -----------------------------------------------------
CREATE DATABASE IF NOT EXISTS `PayMyBuddy` DEFAULT CHARACTER SET utf8 ;
USE `PayMyBuddy` ;

-- -----------------------------------------------------
-- Table `PayMyBuddy`.`users`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `PayMyBuddy`.`users` ;

CREATE TABLE IF NOT EXISTS `PayMyBuddy`.`users` (
                                                    `user_id` INT NOT NULL AUTO_INCREMENT,
                                                    `username` VARCHAR(45) NOT NULL UNIQUE,
    `email` VARCHAR(200) NOT NULL UNIQUE,
    `password` VARCHAR(45) NOT NULL,
    `balance` FLOAT NULL,
    PRIMARY KEY (`user_id`),
    ENGINE = InnoDB;

    INSERT INTO `PayMyBuddy`.`users` (`username`, `email`, `password`, `balance`) VALUES
('user1', 'user1@example.com', 'password1', 100.00),
('user2', 'user2@example.com', 'password2', 200.00),
('user3', 'user3@example.com', 'password3', 0.00);

    -- -----------------------------------------------------
-- Table `PayMyBuddy`.`transactions`
-- -----------------------------------------------------
    DROP TABLE IF EXISTS `PayMyBuddy`.`transactions` ;

    CREATE TABLE IF NOT EXISTS `PayMyBuddy`.`transactions` (
                                                               `transaction_id` INT NOT NULL AUTO_INCREMENT,
                                                               `sender_id` INT NOT NULL,
                                                               `receiver_id` INT NOT NULL,
                                                               `description` VARCHAR(250) NULL,
    `amount` FLOAT NOT NULL,
    PRIMARY KEY (`transaction_id`),
    INDEX `sender_id_idx` (`sender_id` ASC),
    INDEX `receiver_id_idx` (`receiver_id` ASC),
    CONSTRAINT `fk_sender`
    FOREIGN KEY (`sender_id`)
    REFERENCES `PayMyBuddy`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_receiver`
    FOREIGN KEY (`receiver_id`)
    REFERENCES `PayMyBuddy`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

    INSERT INTO `PayMyBuddy`.`transactions` (`sender_id`, `receiver_id`, `description`, `amount`) VALUES
(1, 2, 'Payment for lunch', 25.00),
(2, 3, 'Refund for movie tickets', 15.00),
(1, 2, 'Shared groceries', 50.00);

    -- -----------------------------------------------------
-- Table `PayMyBuddy`.`user_user`
-- -----------------------------------------------------
    DROP TABLE IF EXISTS `PayMyBuddy`.`user_user` ;

    CREATE TABLE IF NOT EXISTS `PayMyBuddy`.`user_user` (
                                                            `user1_id` INT NOT NULL,
                                                            `user2_id` INT NOT NULL,
                                                            PRIMARY KEY (`user1_id`, `user2_id`),
    CONSTRAINT `fk_user1`
    FOREIGN KEY (`user1_id`)
    REFERENCES `PayMyBuddy`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `fk_user2`
    FOREIGN KEY (`user2_id`)
    REFERENCES `PayMyBuddy`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
    ENGINE = InnoDB;

    INSERT INTO `PayMyBuddy`.`user_user` (`user1_id`, `user2_id`) VALUES
(1, 2),
(1, 3);

    -- -----------------------------------------------------
-- View `PayMyBuddy`.`user_1connection_vw`
-- -----------------------------------------------------
    DROP VIEW IF EXISTS `PayMyBuddy`.`user_1connection_vw` ;

    CREATE VIEW user_1connection_vw AS(
                                          SELECT *
                                          FROM users
                                          JOIN user_user ON users.user_id = user_user.user1_id);

    -- -----------------------------------------------------
-- View `PayMyBuddy`.`user_connections_vw`
-- ----------------------------------------------------
    DROP VIEW IF EXISTS `PayMyBuddy`.`user_connections_vw` ;

    CREATE VIEW user_connections_vw AS(
                                          SELECT users.username AS 'User1', users.email AS 'User1_email', user_connection1_vw.username AS 'User2', user_connection1_vw.email AS 'User2_email'
                                          from users
                                          join user_connection1_vw on users.user_id = user_connection1_vw.user2_id);


    SET SQL_MODE=@OLD_SQL_MODE;
    SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
    SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;