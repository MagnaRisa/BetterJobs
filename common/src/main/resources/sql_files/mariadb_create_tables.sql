-- ---------------------------------------------------------------------------------------------------------------------
-- This file should NOT be changed. Doing so will cause unforeseeable errors within the program. You have been Warned!
-- ---------------------------------------------------------------------------------------------------------------------
 DROP TABLE IF EXISTS SideJobSpecificAugments;
 DROP TABLE IF EXISTS ProfSpecificAugments;
 DROP TABLE IF EXISTS UserSideJobHasAugments;
 DROP TABLE IF EXISTS UserProfHasAugments;
 DROP TABLE IF EXISTS SideJobs;
 DROP TABLE IF EXISTS Careers;
 DROP TABLE IF EXISTS Bonus;
 DROP TABLE IF EXISTS Specializations;
 DROP TABLE IF EXISTS Augments;
 DROP TABLE IF EXISTS BonusTypes;
 DROP TABLE IF EXISTS Levels;
 DROP TABLE IF EXISTS SubProfessions;
 DROP TABLE IF EXISTS Professions;
 DROP TABLE IF EXISTS Users;
 DROP TABLE IF EXISTS Settings;

-- Create the User Table
CREATE TABLE IF NOT EXISTS Users
(
	UserID INT NOT NULL AUTO_INCREMENT,
	UUID BINARY (16) NOT NULL,
	Username VARCHAR (50) NOT NULL,
	UserLevel INT UNSIGNED DEFAULT 0,
	DateOfCreation TIMESTAMP,
	
	CONSTRAINT Users_UserID_PK PRIMARY KEY (UserID),
	CONSTRAINT Users_UUID_U UNIQUE (UUID),
	
	INDEX Users_UUID_IDX (UUID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Professions table
CREATE TABLE IF NOT EXISTS Professions
(
	ProfessionID INT NOT NULL AUTO_INCREMENT,
	ProfessionName VARCHAR (25) NOT NULL,
  InternalName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	WageTableRef VARCHAR (30) NOT NULL,
	
	CONSTRAINT Professions_ProfessionID_PK PRIMARY KEY (ProfessionID),
	CONSTRAINT Professions_WageTableRef_U UNIQUE (WageTableRef)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Sub-Professions Table
CREATE TABLE IF NOT EXISTS SubProfessions
(
	SubProfessionID INT NOT NULL AUTO_INCREMENT,
	SubProfessionName VARCHAR (25) NOT NULL,
	InternalName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	WageTableRef VARCHAR (30) NOT NULL,
	TotalAugSlots INT UNSIGNED DEFAULT 0,
	FilledAugSlots INT UNSIGNED DEFAULT 0,
	
	CONSTRAINT SubProfessions_SubProfessionID_PK PRIMARY KEY (SubProfessionID),
	CONSTRAINT SubProfessions_WageTableRef_U UNIQUE (WageTableRef)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the BonusTypes table
CREATE TABLE IF NOT EXISTS BonusTypes
(
	BonusTypeID INT NOT NULL AUTO_INCREMENT,
	BonusType VARCHAR (20),
	
	CONSTRAINT BonusTypes_BonusTypeID_PK PRIMARY KEY (BonusTypeID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Augments Table
CREATE TABLE IF NOT EXISTS Augments
(
	AugmentID INT NOT NULL AUTO_INCREMENT,
	AugmentName VARCHAR (50) NOT NULL,
	AugShorthand VARCHAR (25) DEFAULT NULL,
	MinLevel INT NOT NULL,
	Description TEXT DEFAULT NULL,
	
	CONSTRAINT Augments_AugmentID_PK PRIMARY KEY (AugmentID),
	CONSTRAINT Augments_AugShorthand_U UNIQUE (AugShorthand)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Specializations Augment subclass table
CREATE TABLE IF NOT EXISTS Specializations
(
	SpecID INT NOT NULL AUTO_INCREMENT,
	ProfessionID INT NOT NULL,
	SpecName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	
	CONSTRAINT Specializations_SpecID_PK PRIMARY KEY (SpecID),
	CONSTRAINT Specializations_ProfessionID_FK FOREIGN KEY (ProfessionID)
		REFERENCES Professions (ProfessionID) ON DELETE CASCADE
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Bonus Augments subclass table
CREATE TABLE IF NOT EXISTS Bonus
(
	BonusID INT NOT NULL AUTO_INCREMENT,
	BTypeID INT NOT NULL,
	BonusAmount DOUBLE DEFAULT 0,
	
	CONSTRAINT Bonus_BonusID_PK PRIMARY KEY (BonusID),
	CONSTRAINT Bonus_BTypeID_FK FOREIGN KEY (BTypeID)
		REFERENCES BonusTypes (BonusTypeID) ON DELETE CASCADE
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Professions and Users Relationship of Careers
CREATE TABLE IF NOT EXISTS Careers
(
	UserID INT NOT NULL,
	ProfessionID INT NOT NULL,
	`Level` INT DEFAULT 0,
	CurrentExp DOUBLE DEFAULT 0,
	TotalExp DOUBLE DEFAULT 0,
	PrestigeLevel INT DEFAULT 0,
	ProfStatus VARCHAR (25) DEFAULT 'default',
	
	CONSTRAINT Careers_UserID_ProfessionID_PK PRIMARY KEY (UserID, ProfessionID),
	CONSTRAINT Careers_UserID_FK FOREIGN KEY (UserID)
		REFERENCES Users (UserID) ON DELETE CASCADE,
	CONSTRAINT Careers_ProfessionID_FK FOREIGN KEY (ProfessionID)
		REFERENCES Professions (ProfessionID) ON DELETE CASCADE,
		
	INDEX Careers_ProfessionID_IDX (ProfessionID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the Users and SubProfessions Relationship of SideJobs
CREATE TABLE IF NOT EXISTS SideJobs
(
	UserID INT NOT NULL,
	SubProfessionID INT NOT NULL,
	`Level` INT DEFAULT 0,
	CurrentExp DOUBLE DEFAULT 0,
	TotalExp DOUBLE DEFAULT 0,
	
	CONSTRAINT SideJobs_UserID_SubProfessionID_PK PRIMARY KEY (UserID, SubProfessionID),
	CONSTRAINT SideJobs_UserID_FK FOREIGN KEY (UserID)
		REFERENCES Users (UserID) ON DELETE CASCADE,
	CONSTRAINT SideJobs_SubprofessionID_FK FOREIGN KEY (SubProfessionID)
		REFERENCES SubProfessions (SubProfessionID) ON DELETE CASCADE,
		
	INDEX SideJobs_SubProfessionID_IDX (SubProfessionID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create Careers and Augments Relationship of UserProfHasAugments
CREATE TABLE IF NOT EXISTS UserProfHasAugments
(
	UserID INT NOT NULL,
	ProfessionID INT NOT NULL,
	AugmentID INT NOT NULL,
	
	CONSTRAINT UserProfHasAugments_UserID_ProfessionID_AugmentID_PK 
		PRIMARY KEY (UserID, ProfessionID, AugmentID),
	CONSTRAINT UserProfHasAugments_UserID_ProfessionID_FK FOREIGN KEY (UserID, ProfessionID)
		REFERENCES Careers (UserID, ProfessionID) ON DELETE CASCADE,
	CONSTRAINT UserProfHasAugments_AugmentID_FK FOREIGN KEY (AugmentID)
		REFERENCES Augments (AugmentID) ON DELETE CASCADE,
		
	INDEX UserProfHasAugments_IDX (AugmentID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create SideJobs and Augments Relationship of UserSideJobHasAugments
CREATE TABLE IF NOT EXISTS UserSideJobHasAugments
(
	UserID INT NOT NULL,
	SubProfessionID INT NOT NULL,
	AugmentID INT NOT NULL,
	
	CONSTRAINT UserSideJobHasAugments_UserID_SubProfessionID_AugmentID_PK 
		PRIMARY KEY (UserID, SubProfessionID, AugmentID),
	CONSTRAINT UserSideJobHasAugments_UserID_SubProfessionID_FK FOREIGN KEY (UserID, SubProfessionID)
		REFERENCES SideJobs (UserID, SubProfessionID) ON DELETE CASCADE,
	CONSTRAINT UserSideJobHasAugments_AugmentID_FK FOREIGN KEY (AugmentID)
		REFERENCES Augments (AugmentID) ON DELETE CASCADE,
		
	INDEX UserSideJobHasAugments_IDX (AugmentID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create Professions and Augments Relationship of ProfSpecificAugments
CREATE TABLE IF NOT EXISTS ProfSpecificAugments
(
	ProfessionID INT NOT NULL,
	AugmentID INT NOT NULL,
	
	CONSTRAINT ProfSpecificAugments_ProfessionID_AugmentID_PK PRIMARY KEY (ProfessionID, AugmentID),
	CONSTRAINT ProfSpecificAugments_ProfessionID_FK FOREIGN KEY (ProfessionID)
		REFERENCES Professions (ProfessionID) ON DELETE CASCADE,
	CONSTRAINT ProfSpecificAugments_AugmentID_FK FOREIGN KEY (AugmentID)
		REFERENCES Augments (AugmentID) ON DELETE CASCADE
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create SubProfessions and Augments Relationship of SideJobSpecificAugments
CREATE TABLE IF NOT EXISTS SideJobSpecificAugments
(
	SubProfessionID INT NOT NULL,
	AugmentID INT NOT NULL,
	
	CONSTRAINT SideJobSpecificAugments_SubProfessionID_AugmentID_PK PRIMARY KEY (SubProfessionID, AugmentID),
	CONSTRAINT SideJobSpecificAugments_SubProfessionID_FK FOREIGN KEY (SubProfessionID)
		REFERENCES SubProfessions (SubProfessionID) ON DELETE CASCADE,
	CONSTRAINT SideJobSpecificAugments_AugmentID_FK FOREIGN KEY (AugmentID)
		REFERENCES Augments (AugmentID) ON DELETE CASCADE
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;

-- Create the settings table. This will house useful information about the database.
CREATE TABLE IF NOT EXISTS Settings
(
  SettingID INT NOT NULL AUTO_INCREMENT,
  SettingName VARCHAR (25) NOT NULL,
  Active BOOLEAN NOT NULL DEFAULT FALSE,

	CONSTRAINT Settings_SettingID_PK PRIMARY KEY (SettingID)
) Engine=InnoDB CHARACTER SET utf8 COLLATE utf8_bin;