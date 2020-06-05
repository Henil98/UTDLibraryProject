-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema utdlibrary
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema utdlibrary
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `utdlibrary` DEFAULT CHARACTER SET latin1 ;
USE `utdlibrary` ;

-- -----------------------------------------------------
-- Table `utdlibrary`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`author` (
  `authorID` INT(11) NOT NULL AUTO_INCREMENT,
  `authorName` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`authorID`))
ENGINE = InnoDB
AUTO_INCREMENT = 63
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`location`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`location` (
  `location_id` INT(11) NOT NULL AUTO_INCREMENT,
  `floorNumber` VARCHAR(10) NULL DEFAULT NULL,
  `shelf` VARCHAR(10) NULL DEFAULT NULL,
  `section` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`location_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 14
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`publisher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`publisher` (
  `publisherID` INT(11) NOT NULL AUTO_INCREMENT,
  `publisherName` VARCHAR(45) NULL DEFAULT NULL,
  `publisherAddress_Line1` VARCHAR(45) NULL DEFAULT NULL,
  `publisherAddress_City` VARCHAR(45) NULL DEFAULT NULL,
  `publisherAddress_State` VARCHAR(45) NULL DEFAULT NULL,
  `publisherAddress_Country` VARCHAR(45) NULL DEFAULT NULL,
  `publisherAddress_Zipcode` VARCHAR(45) NULL DEFAULT NULL,
  `publisherPhone` CHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`publisherID`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`typeofitem`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`typeofitem` (
  `typeOfItemID` INT(11) NOT NULL AUTO_INCREMENT,
  `itemType` VARCHAR(35) NOT NULL COMMENT 'itemType - This attribute defines type of item or resource like hardcopy book, magazine, journal, research paper, CD, etc.',
  `maxFine` INT(11) NOT NULL COMMENT 'maxFine - This attribute tells us the max fine that can be imposed on any particular type of resource if it is returned late.',
  PRIMARY KEY (`typeOfItemID`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`book` (
  `isbn` CHAR(13) NOT NULL,
  `title` VARCHAR(85) NOT NULL,
  `deweyDecimalSystemNumber` CHAR(10) NULL DEFAULT NULL COMMENT 'DDSN - This attribute exists if the book is non-fiction. If it is a fiction book, DDSN doesn\'\'t exist for it.',
  `subject` VARCHAR(15) NOT NULL COMMENT 'subject - it contains subject of the book or resource. For example, a book on databases will contain subject database.',
  `libraryOfCongressCatalogNumber` VARCHAR(13) NULL DEFAULT NULL,
  `numberOfPages` CHAR(5) NOT NULL,
  `edition` VARCHAR(10) NULL DEFAULT NULL,
  `description` VARCHAR(500) NOT NULL,
  `numberOfCopies` INT(11) NOT NULL COMMENT 'numberOfCopies - It tells the number of copies of a particular book.\\n\\ncurrentlyAvailableCopies - This is a derived attribute. So it doesn\'\'t have physical presence in table. It shows number of copies of a particular resource which are currently available.',
  `rareOrOriginal` VARCHAR(5) NOT NULL COMMENT 'rareOrOriginal - It contains true or false. If book is rare, it will be true and that book cannot be lent to anyone.',
  `publicationDate` DATE NOT NULL,
  `category` VARCHAR(45) NULL DEFAULT NULL,
  `bookType_ID` INT(11) NOT NULL,
  `bookLoc_ID` INT(11) NOT NULL,
  `bookPub_ID` INT(11) NOT NULL,
  PRIMARY KEY (`isbn`),
  INDEX `fk_book_typeOfItem_idx` (`bookType_ID` ASC),
  INDEX `fk_book_location_idx` (`bookLoc_ID` ASC),
  INDEX `fk_book_publisher_idx` (`bookPub_ID` ASC),
  INDEX `title_index` (`title` ASC),
  CONSTRAINT `fk_book_location`
    FOREIGN KEY (`bookLoc_ID`)
    REFERENCES `utdlibrary`.`location` (`location_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_book_publisher`
    FOREIGN KEY (`bookPub_ID`)
    REFERENCES `utdlibrary`.`publisher` (`publisherID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_book_typeOfItem`
    FOREIGN KEY (`bookType_ID`)
    REFERENCES `utdlibrary`.`typeofitem` (`typeOfItemID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`authorhaswritten`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`authorhaswritten` (
  `author_ID` INT(11) NOT NULL COMMENT 'This is the foreign key which refers to author table.',
  `isbn` CHAR(13) NOT NULL COMMENT 'This is the foreign key that refers to book table.',
  PRIMARY KEY (`author_ID`, `isbn`),
  INDEX `fk_isbn_hasWritten_book_idx` (`isbn` ASC),
  CONSTRAINT `fk_aID_hasWritten_author`
    FOREIGN KEY (`author_ID`)
    REFERENCES `utdlibrary`.`author` (`authorID`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_isbn_hasWritten_book`
    FOREIGN KEY (`isbn`)
    REFERENCES `utdlibrary`.`book` (`isbn`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`bookhastype`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`bookhastype` (
  `isbn` CHAR(1) NOT NULL,
  `bookTypeID` INT(11) NOT NULL,
  PRIMARY KEY (`isbn`, `bookTypeID`),
  INDEX `fk_typeID_hasType_typeOfItem_idx` (`bookTypeID` ASC),
  CONSTRAINT `fk_isbn_hasType_book`
    FOREIGN KEY (`isbn`)
    REFERENCES `utdlibrary`.`book` (`isbn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_typeID_hasType_typeOfItem`
    FOREIGN KEY (`bookTypeID`)
    REFERENCES `utdlibrary`.`typeofitem` (`typeOfItemID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`borrowertype`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`borrowertype` (
  `bTypeID` INT(11) NOT NULL AUTO_INCREMENT,
  `borrower_Type` VARCHAR(45) NOT NULL COMMENT 'borrower_Type - borrower_Type can contain types like graduate student, undergraduate or Faculty.',
  `maxCheckoutItemsLimit` INT(11) NOT NULL COMMENT 'Example:- faculty can checkout upto 300 items.',
  `maxDurationOfCheckout` CHAR(3) NOT NULL,
  PRIMARY KEY (`bTypeID`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`borrower`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`borrower` (
  `utdID` CHAR(10) NOT NULL,
  `netID` CHAR(9) NOT NULL,
  `firstname` VARCHAR(40) NOT NULL,
  `middlename` VARCHAR(40) NULL DEFAULT NULL,
  `lastname` VARCHAR(40) NOT NULL,
  `gender` CHAR(1) NOT NULL,
  `department` VARCHAR(35) NOT NULL,
  `isActive` TINYINT(4) NOT NULL COMMENT 'isActive - Is_Active is a boolean attribute, it will indicate whether borrower currently works or is enrolled in university or not. If not then he cannot issue or checkout the book.',
  `numberOfcheckedoutResources` INT(11) NULL DEFAULT NULL COMMENT 'numberOfcheckedoutResources - This attribute gives us total number of resources checked out by particular borrower. ',
  `borrowertypeID` INT(11) NOT NULL,
  PRIMARY KEY (`utdID`),
  INDEX `fk_borrower_bType_idx` (`borrowertypeID` ASC),
  INDEX `netID_index` (`netID` ASC),
  CONSTRAINT `fk_borrower_bType`
    FOREIGN KEY (`borrowertypeID`)
    REFERENCES `utdlibrary`.`borrowertype` (`bTypeID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`borroweraddress`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`borroweraddress` (
  `addressID` INT(11) NOT NULL AUTO_INCREMENT,
  `borrowerAddressType` VARCHAR(15) NOT NULL,
  `borrowerAddress_Line1` VARCHAR(55) NOT NULL,
  `borrowerAddress_Line2` VARCHAR(55) NULL DEFAULT NULL,
  `borrowerAddress_City` VARCHAR(20) NOT NULL,
  `borrowerAddress_State` VARCHAR(20) NOT NULL,
  `borrowerAddress_Country` VARCHAR(20) NOT NULL,
  `borrowerAddress_Zipcode` VARCHAR(10) NOT NULL,
  `utd_ID` CHAR(10) NOT NULL,
  PRIMARY KEY (`addressID`),
  INDEX `fk_bAddress_borrower_idx` (`utd_ID` ASC),
  CONSTRAINT `fk_bAddress_borrower`
    FOREIGN KEY (`utd_ID`)
    REFERENCES `utdlibrary`.`borrower` (`utdID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`borroweremail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`borroweremail` (
  `borrowerEmailID` INT(11) NOT NULL AUTO_INCREMENT,
  `borrowerEmailType` VARCHAR(15) NOT NULL,
  `borrowerEmail` VARCHAR(45) NOT NULL,
  `utd_ID` CHAR(10) NOT NULL,
  PRIMARY KEY (`borrowerEmailID`),
  INDEX `fk_borrowerEmail_borrower_idx` (`utd_ID` ASC),
  CONSTRAINT `fk_borrowerEmail_borrower`
    FOREIGN KEY (`utd_ID`)
    REFERENCES `utdlibrary`.`borrower` (`utdID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`borrowerphone`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`borrowerphone` (
  `phoneID` INT(11) NOT NULL AUTO_INCREMENT,
  `utd_ID` CHAR(10) NULL DEFAULT NULL,
  `phoneType` VARCHAR(20) NULL DEFAULT NULL,
  `countryCode` VARCHAR(7) NULL DEFAULT NULL,
  `phoneNumber` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`phoneID`),
  INDEX `fk_bPhone_Borrower_idx` (`utd_ID` ASC),
  CONSTRAINT `fk_bPhone_Borrower`
    FOREIGN KEY (`utd_ID`)
    REFERENCES `utdlibrary`.`borrower` (`utdID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;


-- -----------------------------------------------------
-- Table `utdlibrary`.`checksout`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `utdlibrary`.`checksout` (
  `isbn` CHAR(13) NOT NULL,
  `utd_ID` CHAR(10) NOT NULL,
  `issueDate` DATE NOT NULL COMMENT 'issueDate- This gives the date when the item is checked out.',
  `dueDate` DATE NOT NULL,
  `returnDate` DATE NULL DEFAULT NULL,
  PRIMARY KEY (`isbn`, `utd_ID`),
  INDEX `fk_checksout_borrower_idx` (`utd_ID` ASC),
  CONSTRAINT `fk_checksout_book`
    FOREIGN KEY (`isbn`)
    REFERENCES `utdlibrary`.`book` (`isbn`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_checksout_borrower`
    FOREIGN KEY (`utd_ID`)
    REFERENCES `utdlibrary`.`borrower` (`utdID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = latin1;

USE `utdlibrary` ;

-- -----------------------------------------------------
-- procedure insert_book
-- -----------------------------------------------------

DELIMITER $$
USE `utdlibrary`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_book`(IN  pCustomerNumber INT, 
OUT pCustomerLevel  VARCHAR(20))
BEGIN
	DECLARE isbn_count INT;
    SELECT COUNT(*) INTO isbn_count FROM utdlibrary.book AS b WHERE b.isbn = isbn;
    IF isbn_count > 0 THEN
        SET pCustomerLevel = 'I am already here';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure insert_book_new
-- -----------------------------------------------------

DELIMITER $$
USE `utdlibrary`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `insert_book_new`(IN  isbn VARCHAR(13), 
OUT pCustomerLevel  VARCHAR(20))
BEGIN
	DECLARE isbn_count INT;
    SELECT COUNT(*) INTO isbn_count FROM utdlibrary.book AS b WHERE b.isbn = isbn;
    IF isbn_count > 0 THEN
        SET pCustomerLevel = 'I am already here';
    END IF;
END$$

DELIMITER ;

-- -----------------------------------------------------
-- procedure sp_insertAuthor
-- -----------------------------------------------------

DELIMITER $$
USE `utdlibrary`$$
CREATE DEFINER=`root`@`localhost` PROCEDURE `sp_insertAuthor`(
IN isbn varchar(150)
)
BEGIN

SELECT DISTINCT(aw.isbn) AS 'ISBN'
FROM author as a 
INNER JOIN authorhaswritten AS aw
ON  a.authorID = aw.author_ID
WHERE a.authorName IN ('akshay', 'henil');

END$$

DELIMITER ;

SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
