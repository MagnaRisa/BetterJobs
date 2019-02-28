-- ---------------------------------------------------------------------------------------------------------------------
-- This file should NOT be changed. Doing so will cause unforeseeable errors within the plugin. You have been Warned!
-- ---------------------------------------------------------------------------------------------------------------------

-- -------------------------------------
-- Insertions for the Professions Table.
-- -------------------------------------

-- ID: 1
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Miner", "miner", "miner_wage.json",
"Diggy Diggy Hole, Diggy Diggy Hole, I hate myself for remembering this song! *Dated Sourdough*");

-- ID: 2
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Angler", "angler", "angler_wage.json",
"This Profession is all focused around fishing and catching the various treasures of the sea.");

-- ID: 3
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Lumberjack", "lumberjack", "lumberjack_wage.json",
"This Job will get you money based on chopping down trees for that oh’ so precious Lumber.");

-- ID: 4
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Knight", "knight", "knight_wage.json",
"Become a Knight and take on all of the things that go bump in the night and get paid while doing it!");

-- ID: 5
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Architect", "architect", "architect_wage.json",
"Your your innate building skillz to create masterpieces and get paid while creating those structurally sound builds.");

-- ID: 6
INSERT INTO Professions (ProfessionName, InternalName, WageTableRef, Description) VALUES ("The Farmer", "farmer", "farmer_wage.json",
"This profession will allow you to to obtain money from doing any type of farming within the game. Specializations of the Farmer Profession are The Rancher, and Crop Cultivation");

-- ---------------------------------------
-- Insertions for the SubProfessions Table
-- ---------------------------------------

-- ID: 1
INSERT INTO SubProfessions (SubProfessionName, InternalName, WageTableRef, Description) VALUES ("The Arrow Smith", "arrow_smith", "arrow_smith_wage.json",
"Arrows, what a pain to craft…. Why would I want to craft all of those kewl new arrows? Now with the backing of payment for crafting such objects, maybe you’ll find a use for them.");

-- ID: 2
INSERT INTO SubProfessions (SubProfessionName, InternalName, WageTableRef, Description) VALUES ("The Enchanter", "enchanter", "enchanter_wage.json",
"Have you ever lost all of your gear in the void and regretted the beautiful grind to get it back? Well fear no more *cough cough Malathier cough* because now there is an incentive to the grind by getting paid per enchantment.");

-- ID: 3
INSERT INTO SubProfessions (SubProfessionName, InternalName, WageTableRef, Description) VALUES ("The Alchemist", "alchemist", "alchemist_wage.json",
"Is that a wart I see on your nose? Oh that is just your face, become the wicked witch and brew up dem potions to have more money to spend on your dark arts.");

-- ID: 4
INSERT INTO SubProfessions (SubProfessionName, InternalName, WageTableRef, Description) VALUES ("The Merchant", "merchant", "merchant_wage.json",
"Become the pompous arrogant Merchant when you trade with those weird unisex people from the far distant Nose-Opolis.");

-- ID: 5
INSERT INTO SubProfessions (SubProfessionName, InternalName, WageTableRef, Description) VALUES ("The Chef", "chef", "chef_wage.json",
"When you gotta eat, you gotta eat. Fear no more about buying your food from the store! In stead get paid when you cook your food.");

-- ----------------------------------------
-- Insertions for the Specializations Table
-- ----------------------------------------
-- ID: 1
INSERT INTO Specializations (ProfessionID, SpecName, Description) VALUES (1, "Ore Affinity", 
"This Specialization or Affinity leans towards the Mining of ores over anything else. This Specialization will allow you to get more money when you mine ores as opposed to the stone variants.");

-- ID: 2
INSERT INTO Specializations (ProfessionID, SpecName, Description) VALUES (1, "Stone Affinity",
"This Specialization focuses on the Stone Mining aspect of the Miner Job in which more money is awarded to the user to mine stone rather than ores. This specialization would lean towards those that like to dig out large areas.");

-- ID: 3
INSERT INTO Specializations (ProfessionID, SpecName, Description) VALUES (6, "The Rancher",
"Welcome to the realm of Animal Husbandry, everything from the breeding and slaughtering of livestock to the taming of many of the tameable mammals across the large cuboid of a world.");

-- ID: 4
INSERT INTO Specializations (ProfessionID, SpecName, Description) VALUES (6, "Crop Cultivation",
"Have you ever wanted to play Farming Simulator… In Minecraft? Well fear not person of weird taste, Now you get paid more while Farming! How fantastic is that!");

-- ---------------------------------
-- Insertions for the Augments Table
-- ---------------------------------
-- ID: 1
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Minimal Experience Bonus", "MinExpBonus", 1, 
"This augment allows this profession to gain more experience per action performed. For example if you have the miner profession, you will obtain slightly more exp per block mined.");

-- ID: 2
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Standard Experience Bonus", "StdExpBonus", 10, 
"This augment allows this profession to gain a moderate amount of experience gain. This will give more than the Minimal Exp Bonus.");

-- ID: 3
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Improved Experience Bonus", "ImpExpBonus", 25,
"This augment allows this profession to gain an improved amount of exp from the moderate level of experience.");

-- ID: 4
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Maximum Experience Bonus", "MaxExpBonus", 45,
"Description goes here.");

-- ID: 5
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Minimum Income Bonus", "MinIncomeBonus", 1,
"Description goes here.");

-- ID: 6
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Standard Income Bonus", "StdIncomeBonus", 10,
"Description goes here.");

-- ID: 7
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Improved Income Bonus", "ImpIncomeBonus", 25,
"Description goes here.");

-- ID: 8
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Maximum Income Bonus", "MaxIncomeBonus", 45,
"Description goes here.");

-- ID: 9
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Minimum [Token] Bonus", "MinTokenBonus", 1,
"Description goes here.");

-- ID: 10
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Standard [Token] Bonus", "StdTokenBonus", 10,
"Description goes here");

-- ID: 11
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Improved [Token] Bonus", "ImpTokenBonus", 25,
"Description goes here.");

-- ID: 12
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Maximum [Token] Bonus", "MaxTokenBonus", 45,
"Description goes here.");

-- ID: 13
INSERT INTO Augments (AugmentName, AugShorthand, MinLevel, Description) VALUES ("Specialize Profession", "SpecProfession", 25,
"Description goes here.");

-- ----------------------------------
-- Insertions for the BonusType Table
-- ----------------------------------
INSERT INTO BonusTypes (BonusType) VALUES ("Experience");
INSERT INTO BonusTypes (BonusType) VALUES ("Income");
INSERT INTO BonusTypes (BonusType) VALUES ("[Token]");

-- ----------------------------------
-- Insertions for the Settings Table
-- ----------------------------------
INSERT INTO Settings (SettingName, Active) VALUES ("Setup", 1);