-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `mydb` ;

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`user_auth`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_auth` ;

CREATE TABLE IF NOT EXISTS `mydb`.`user_auth` (
    `uid` VARCHAR(13) NOT NULL,
    `email` VARCHAR(32) NOT NULL,
    `email_company` VARCHAR(25) NOT NULL,
    PRIMARY KEY (`uid`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`diary`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`diary` ;

CREATE TABLE IF NOT EXISTS `mydb`.`diary` (
                                              `diary_id` INT NOT NULL AUTO_INCREMENT,
                                              `diary_date` DATE NOT NULL,
                                              `content` VARCHAR(255) NOT NULL,
    `reg_date` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `is_deleted` TINYINT(1) NOT NULL DEFAULT '0',
    `uid` VARCHAR(13) NOT NULL,
    PRIMARY KEY (`diary_id`),
    INDEX `fk_diary_user_auth1_idx` (`uid` ASC) VISIBLE,
    CONSTRAINT `fk_diary_user_auth1`
    FOREIGN KEY (`uid`)
    REFERENCES `mydb`.`user_auth` (`uid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    AUTO_INCREMENT = 46
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`diary_analytics`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`diary_analytics` ;

CREATE TABLE IF NOT EXISTS `mydb`.`diary_analytics` (
                                                        `diary_id` INT NOT NULL,
                                                        `neutral` FLOAT NOT NULL DEFAULT '0',
                                                        `joy` FLOAT NOT NULL DEFAULT '0',
                                                        `sadness` FLOAT NOT NULL DEFAULT '0',
                                                        `anger` FLOAT NOT NULL DEFAULT '0',
                                                        `fear` FLOAT NOT NULL DEFAULT '0',
                                                        PRIMARY KEY (`diary_id`),
    CONSTRAINT `FK_Diary_TO_Diary_Analytics_1`
    FOREIGN KEY (`diary_id`)
    REFERENCES `mydb`.`diary` (`diary_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`diary_analytics_sentiment`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`diary_analytics_sentiment` ;

CREATE TABLE IF NOT EXISTS `mydb`.`diary_analytics_sentiment` (
                                                                  `diary_id` INT NOT NULL,
                                                                  `sentiment` FLOAT NOT NULL,
                                                                  `accuracy` FLOAT NOT NULL,
                                                                  PRIMARY KEY (`diary_id`),
    CONSTRAINT `FK_Diary_TO_Diary_Analytics_Sentiment_1`
    FOREIGN KEY (`diary_id`)
    REFERENCES `mydb`.`diary` (`diary_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`music_info`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`music_info` ;

CREATE TABLE IF NOT EXISTS `mydb`.`music_info` (
                                                   `mid` INT NOT NULL AUTO_INCREMENT,
                                                   `music_title` VARCHAR(100) NOT NULL,
    `music_artist` VARCHAR(32) NOT NULL,
    `video_id` VARCHAR(255) NULL DEFAULT NULL,
    `music_genre` VARCHAR(20) NOT NULL,
    `file_path` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`mid`))
    ENGINE = InnoDB
    AUTO_INCREMENT = 96
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`diary_music`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`diary_music` ;

CREATE TABLE IF NOT EXISTS `mydb`.`diary_music` (
                                                    `diary_id` INT NOT NULL,
                                                    `mid` INT NOT NULL,
                                                    PRIMARY KEY (`diary_id`),
    INDEX `fk_diary_music_music_info1_idx` (`mid` ASC) VISIBLE,
    CONSTRAINT `fk_diary_music_music_info1`
    FOREIGN KEY (`mid`)
    REFERENCES `mydb`.`music_info` (`mid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
    CONSTRAINT `FK_Diary_TO_Diary_Music_1`
    FOREIGN KEY (`diary_id`)
    REFERENCES `mydb`.`diary` (`diary_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`survey`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`survey` ;

CREATE TABLE IF NOT EXISTS `mydb`.`survey` (
    `genre` VARCHAR(20) NOT NULL,
    `video_id` VARCHAR(255) NOT NULL,
    `music_title` VARCHAR(32) NOT NULL,
    `music_artist` VARCHAR(32) NOT NULL,
    PRIMARY KEY (`genre`))
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`user_emotion`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_emotion` ;

CREATE TABLE IF NOT EXISTS `mydb`.`user_emotion` (
    `uid` VARCHAR(13) NOT NULL,
    `genre` VARCHAR(20) NOT NULL,
    `neutral` FLOAT NOT NULL DEFAULT '0',
    `joy` FLOAT NOT NULL DEFAULT '0',
    `sadness` FLOAT NOT NULL DEFAULT '0',
    `anger` FLOAT NOT NULL DEFAULT '0',
    `fear` FLOAT NOT NULL DEFAULT '0',
    `is_deleted` TINYINT NULL DEFAULT '0',
    PRIMARY KEY (`uid`, `genre`),
    CONSTRAINT `FK_User_Auth_TO_User_Emotion_1`
    FOREIGN KEY (`uid`)
    REFERENCES `mydb`.`user_auth` (`uid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `mydb`.`user_profile`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `mydb`.`user_profile` ;

CREATE TABLE IF NOT EXISTS `mydb`.`user_profile` (
    `uid` VARCHAR(13) NOT NULL,
    `file_path` VARCHAR(255) NOT NULL,
    `nickname` VARCHAR(12) NOT NULL,
    `has_surveyed` TINYINT(1) NOT NULL,
    PRIMARY KEY (`uid`),
    CONSTRAINT `FK_User_Auth_TO_User_Profile_1`
    FOREIGN KEY (`uid`)
    REFERENCES `mydb`.`user_auth` (`uid`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
    ENGINE = InnoDB
    DEFAULT CHARACTER SET = utf8mb3;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;