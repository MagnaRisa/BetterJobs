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
	UserID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	UUID BLOB NOT NULL,
	Username VARCHAR (50) NOT NULL,
	UserLevel INT UNSIGNED DEFAULT 0,
	DateOfCreation TIMESTAMP,

	CONSTRAINT Users_UUID_U UNIQUE (UUID)
);

CREATE UNIQUE INDEX IF NOT EXISTS Users_UUID_U ON Users (UUID);

-- Create the Professions table
CREATE TABLE IF NOT EXISTS Professions
(
	ProfessionID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	ProfessionName VARCHAR (25) NOT NULL,
  InternalName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	WageTableRef VARCHAR (30) NOT NULL,
	TotalAugSlots INT UNSIGNED DEFAULT 0,
	FilledAugSlots INT UNSIGNED DEFAULT 0,
	
	CONSTRAINT Professions_WageTableRef_U UNIQUE (WageTableRef)
);

-- Create the Sub-Professions Table
CREATE TABLE IF NOT EXISTS SubProfessions
(
	SubProfessionID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	SubProfessionName VARCHAR (25) NOT NULL,
  InternalName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	WageTableRef VARCHAR (30) NOT NULL,
	TotalAugSlots INT UNSIGNED DEFAULT 0,
	FilledAugSlots INT UNSIGNED DEFAULT 0,
	
	CONSTRAINT SubProfessions_WageTableRef_U UNIQUE (WageTableRef)
);

-- Create the BonusTypes table
CREATE TABLE IF NOT EXISTS BonusTypes
(
	BonusTypeID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	BonusType VARCHAR (20)
);

-- Create the Augments Table
CREATE TABLE IF NOT EXISTS Augments
(
	AugmentID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	AugmentName VARCHAR (50) NOT NULL,
	AugShorthand VARCHAR (25) DEFAULT NULL,
	MinLevel INT NOT NULL,
	Description TEXT DEFAULT NULL,
	
	CONSTRAINT Augments_AugShorthand_U UNIQUE (AugShorthand)
);

-- Create the Specializations Augment subclass table
CREATE TABLE IF NOT EXISTS Specializations
(
	SpecID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	ProfessionID INT NOT NULL,
	SpecName VARCHAR (25) NOT NULL,
	Description TEXT DEFAULT NULL,
	
	CONSTRAINT Specializations_ProfessionID_FK FOREIGN KEY (ProfessionID)
		REFERENCES Professions (ProfessionID) ON DELETE CASCADE
);

-- Create the Bonus Augments subclass table
CREATE TABLE IF NOT EXISTS Bonus
(
	BonusID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
	BTypeID INT NOT NULL,
	BonusAmount DOUBLE DEFAULT 0,
	
	CONSTRAINT Bonus_BTypeID_FK FOREIGN KEY (BTypeID)
		REFERENCES BonusTypes (BonusTypeID) ON DELETE CASCADE
);

-- Create the Professions and Users Relationship of Careers
CREATE TABLE IF NOT EXISTS Careers
(
	UserID INT NOT NULL,
	ProfessionID INT NOT NULL,
	`Level` INT DEFAULT 0,
	CurrentExp DOUBLE DEFAULT 0,
	TotalExp DOUBLE DEFAULT 0,
	PrestigeLevel INT DEFAULT 0,
	
	CONSTRAINT Careers_UserID_ProfessionID_PK PRIMARY KEY (UserID, ProfessionID),
	
	CONSTRAINT Careers_UserID_FK FOREIGN KEY (UserID)
		REFERENCES Users (UserID) ON DELETE CASCADE,
		
	CONSTRAINT Careers_ProfessionID_FK FOREIGN KEY (ProfessionID)
		REFERENCES Professions (ProfessionID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS Careers_ProfessionID_IDX ON Careers (ProfessionID);

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
		REFERENCES SubProfessions (SubProfessionID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS SideJobs_SubProfessionID_IDX ON SideJobs (SubProfessionID);

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
		REFERENCES Augments (AugmentID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS UserProfHasAugments_ProfessionID_IDX ON UserProfHasAugments (ProfessionID);

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
		REFERENCES Augments (AugmentID) ON DELETE CASCADE
);

CREATE INDEX IF NOT EXISTS UserSideJobHasAugments_SubProfessionID_IDX ON UserSideJobHasAugments (SubProfessionID);

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
);

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
);

-- Create the settings table. This will house useful information about the database.
CREATE TABLE IF NOT EXISTS Settings
(
  SettingID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  SettingName VARCHAR (25) NOT NULL,
  Active INT NOT NULL DEFAULT 0
);