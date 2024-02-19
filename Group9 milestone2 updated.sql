-- Create new schema
DROP SCHEMA IF EXISTS CS5200Project;
CREATE SCHEMA CS5200Project;
USE CS5200Project;

-- Create tables
-- The 'Player' table stores player information.
CREATE TABLE Player (
  accountId INT AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  emailAddress VARCHAR(255) NOT NULL,
  isActive BOOL DEFAULT TRUE,
  CONSTRAINT pk_Player_accountId PRIMARY KEY (accountId)
);

-- The 'Character' table stores character information for each player, and a player can have one or more characters.
CREATE TABLE `Character` (
  characterId INT AUTO_INCREMENT,
  accountId INT NOT NULL,
  characterFirstName VARCHAR(255) NOT NULL,
  characterLastName VARCHAR(255) NOT NULL,
  CONSTRAINT pk_Character_characterId PRIMARY KEY (characterId),
  CONSTRAINT fk_Character_accountId FOREIGN KEY (accountId)
    REFERENCES Player(accountId)
	ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT uq_Character_characterFirstName_characterLastName UNIQUE (characterFirstName, characterLastName)
);

-- The 'Job' table contains all job-related information.
CREATE TABLE Job (
  jobId INT AUTO_INCREMENT,
  jobName VARCHAR(50) NOT NULL,
  jobLevel SMALLINT NOT NULL,
  minLevelExp BIGINT NOT NULL,
  maxLevelExp BIGINT NOT NULL,
  CONSTRAINT pk_Job_jobId PRIMARY KEY (jobId)
);

-- The 'Currency' table holds data on various currencies, with the 'discontinued' column indicating whether a specific currency is still in use.
CREATE TABLE Currency (
  currencyName VARCHAR(30),
  totalCap BIGINT NOT NULL,
  weeklyCap INT NOT NULL,
  discontinued BOOL DEFAULT FALSE,
  CONSTRAINT pk_Currency_currencyName PRIMARY KEY (currencyName)
);

-- The 'Attributes' table stores various attribute types for characters, such as Strength and Dexterity.
CREATE TABLE Attributes (
  attribute VARCHAR(50) NOT NULL,
  CONSTRAINT pk_Attributes_attribute PRIMARY KEY (attribute)
);

-- The 'Item table' serves as the parent table and includes common properties for four subtypes: gear, weapons, consumables, and miscellaneous.
CREATE TABLE Item(
  itemId INT AUTO_INCREMENT,
  itemName VARCHAR(255) NOT NULL,
  maxStackSize INT NOT NULL,
  vendorPrice INT,
  canBeSold BOOLEAN DEFAULT TRUE,
  CONSTRAINT pk_Item_itemId PRIMARY KEY (itemId)
);

-- The 'Consumable' table is dedicated to storing consumable items.
CREATE TABLE Consumable(
  itemId INT NOT NULL,
  itemLevel INT NOT NULL,
  `description` TEXT NOT NULL,
  CONSTRAINT pk_Consumable_itemId PRIMARY KEY (itemId),
  CONSTRAINT fk_Consumable_itemId FOREIGN KEY (itemId) 
    REFERENCES Item (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'Miscellaneous' table is reserved for miscellaneous items.
CREATE TABLE Miscellaneous(
  itemId INT NOT NULL,
  `description` TEXT NOT NULL,
  CONSTRAINT pk_Miscellaneous_itemId PRIMARY KEY (itemId),
  CONSTRAINT fk_Miscellaneous_itemId FOREIGN KEY (itemId) 
    REFERENCES Item (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'ConsumableBonus' table contains information about attribute bonuses for consumable items, identifying them with various attributes.
CREATE TABLE ConsumableBonus(
  itemId INT NOT NULL,
  attribute VARCHAR(50) NOT NULL,
  bonusPercentage DECIMAL NOT NULL,
  bonusCap BIGINT NOT NULL,
  CONSTRAINT pk_ConsumableBonus_itemId_attribute PRIMARY KEY (itemId,attribute),
  CONSTRAINT fk_ConsumableBonus_itemId FOREIGN KEY (itemId)
    REFERENCES Consumable(itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_ConsumableBonus_attribute FOREIGN KEY (attribute)
    REFERENCES Attributes(attribute)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'SlotTypes' table stores information about equipment slots. Instead of using enumerations, we will incorporate the required slots into the assignment as table data.
CREATE TABLE SlotTypes (
  slotType VARCHAR(255) NOT NULL,
  CONSTRAINT pk_SlotTypes_slotType PRIMARY KEY (slotType)
);

-- The 'Equippable' table acts as a parent table, encompassing both Gear and Weapon tables, which represent items that can be equipped in various slots.
CREATE TABLE Equippable (
  itemId INT NOT NULL,
  itemLevel INT NOT NULL,
  slotType VARCHAR(255) NOT NULL,
  requiredJobLevel INT,
  CONSTRAINT pk_Equippable_itemId PRIMARY KEY (itemId),
  CONSTRAINT fk_Equippable_itemId FOREIGN KEY (itemId)
    REFERENCES Item (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Equippable_slotType FOREIGN KEY (slotType)
    REFERENCES SlotTypes (slotType)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'Weapon' table contains information about weapon items.
CREATE TABLE Weapon (
  itemId INT NOT NULL,
  damageDone FLOAT NOT NULL,
  autoAttack FLOAT NOT NULL,
  attackDelay FLOAT NOT NULL,
  associatedJob INT NOT NULL,
  CONSTRAINT pk_Weapon_itemId PRIMARY KEY (itemId),
  CONSTRAINT fk_Weapon_itemId FOREIGN KEY (itemId)
    REFERENCES Equippable (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Weapon_associatedJob FOREIGN KEY (associatedJob)
    REFERENCES Job (jobId)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'Gear' table contains information about gear items.
CREATE TABLE Gear (
  itemId INT NOT NULL,
  defenseRating FLOAT,
  magicDefenseRating FLOAT,
  CONSTRAINT pk_Gear_itemId PRIMARY KEY (itemId),
  CONSTRAINT fk_Gear_itemId FOREIGN KEY (itemId)
    REFERENCES Equippable (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'EquippableBonus' table stores attribute bonuses for Gear and Weapon items, enabling the identification of attributes associated with each item.
CREATE TABLE EquippableBonus (
  itemId INT NOT NULL,
  attribute VARCHAR(50) NOT NULL,
  bonusValue FLOAT NOT NULL,
  CONSTRAINT pk_EquippableBonus_itemId_attribute PRIMARY KEY (itemId,attribute),
  CONSTRAINT fk_EquippableBonus_itemId FOREIGN KEY (itemId)
    REFERENCES Equippable (itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_EquippableBonus_attribute FOREIGN KEY (attribute)
	REFERENCES Attributes (attribute)
	ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'GearJob' table contains information about which Jobs can equip Gear items.
CREATE TABLE GearJob(
  itemId INT NOT NULL,
  jobId INT NOT NULL,
  CONSTRAINT pk_GearJob_itemId_jobId PRIMARY KEY (itemId,jobId),
  CONSTRAINT fk_Gearjob_itemId FOREIGN KEY (itemId)
	REFERENCES  Gear(itemId)
	ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Gearjob_jobId FOREIGN KEY (jobId)
	REFERENCES Job(jobId)
	ON UPDATE CASCADE ON DELETE CASCADE
 );
 
 -- The 'CharacterAttribute' table stores attribute information for each character.
CREATE TABLE CharacterAttribute(
  characterId INT NOT NULL,
  attributeName VARCHAR(50) NOT NULL,
  attributeValue BIGINT NOT NULL,
  CONSTRAINT pk_CharacterAttribute_characterId_attributeName PRIMARY KEY (characterId,attributeName),
  CONSTRAINT fk_CharacterAttribute_characterId FOREIGN KEY (characterId)
	REFERENCES `Character`(characterId)
	ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_CharacterAttribute_attributeName FOREIGN KEY (attributeName)
	REFERENCES Attributes(attribute)
	ON UPDATE CASCADE ON DELETE CASCADE
 );
 
 -- The 'Colors' table stores the colors that can be customized in various equippable items by different characters.
  CREATE TABLE Colors(
    color VARCHAR(255) NOT NULL,
    CONSTRAINT pk_Colors_color PRIMARY KEY(color)
  );
  
-- 'Customization' records customized equippable items by different characters.
CREATE TABLE Customization(
  customizationId INT AUTO_INCREMENT,
  itemId INT NOT NULL,
  dyeColor VARCHAR(255),
  isHighQuality ENUM('High','Normal') NOT NULL,
  `condition` FLOAT NOT NULL,
  madeBy INT,
  CONSTRAINT pk_Customization_customizationId PRIMARY KEY(customizationId),
  CONSTRAINT fk_Customization_itemId FOREIGN KEY (itemId)
	REFERENCES Equippable(itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Customization_dyeColor FOREIGN KEY (dyeColor)
	REFERENCES Colors (color)
    ON UPDATE CASCADE ON DELETE SET NULL,
  CONSTRAINT fk_Customization_madeBy FOREIGN KEY (madeBy)
	REFERENCES `Character` (characterId)
    ON UPDATE CASCADE ON DELETE SET NULL
);

-- The 'Inventory' table holds collections of items for each character, with each character having their dedicated slotId for item information.
CREATE TABLE Inventory (
  characterId INT NOT NULL,
  slotId INT NOT NULL,
  itemId INT NOT NULL,
  customizationId INT,
  quantity INT NOT NULL,
  CONSTRAINT pk_Inventory_characterId_slotId PRIMARY KEY (characterId, slotId),
  CONSTRAINT fk_Inventory_characterId FOREIGN KEY (characterId) 
    REFERENCES `Character` (characterId) 
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Inventory__itemId FOREIGN KEY (itemId) 
    REFERENCES Item(itemId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_Inventory__customizationId FOREIGN KEY (customizationId)
    REFERENCES Customization (customizationId) 
    ON UPDATE CASCADE ON DELETE SET NULL
);

-- The 'CharacterJob' table contains data about various jobs available to each character, with 'isUnlocked' and 'isCurrentJob' used to indicate unlocked and current jobs, respectively.
CREATE TABLE CharacterJob (
  characterId INT NOT NULL,
  jobId INT NOT NULL,
  currentExp BIGINT NOT NULL,
  isUnlocked BOOLEAN DEFAULT FALSE,
  isCurrentJob BOOLEAN DEFAULT FALSE,
  CONSTRAINT pk_CharacterJob_characterId_jobId PRIMARY KEY (characterId, jobId),
  CONSTRAINT fk_CharacterJob_characterId FOREIGN KEY (characterId) 
    REFERENCES `Character` (characterId)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_CharacterJob_JobId FOREIGN KEY (JobId) 
    REFERENCES Job (JobId) 
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'CharacterCurrency' table stores currency data for each character.
CREATE TABLE CharacterCurrency (
  characterId INT NOT NULL,
  currencyName VARCHAR(30) NOT NULL,
  amountOwned BIGINT NOT NULL,
  weeklyAmountOwned INT NOT NULL,
  CONSTRAINT pk_CharacterCurrency_characterId_currencyName PRIMARY KEY (characterId, currencyName), 
  CONSTRAINT fk_CharacterCurrency_characterId FOREIGN KEY (characterId) 
    REFERENCES `Character` (characterId) 
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_CharacterCurrency_currencyName FOREIGN KEY (currencyName) 
    REFERENCES Currency(currencyName) 
    ON UPDATE CASCADE ON DELETE CASCADE
);

-- The 'CharacterSlot' table records equipped items in different slots for each character.
CREATE TABLE CharacterSlot (
    characterId INT NOT NULL,
    slotType VARCHAR(255) NOT NULL,
    equippedItem INT NOT NULL,
    customization INT,
	CONSTRAINT pk_CharacterSlot_characterId_slotType PRIMARY KEY (characterId, slotType), 
    CONSTRAINT fk_CharacterSlot_characterId FOREIGN KEY (characterId)
      REFERENCES `Character`(characterId)
      ON UPDATE CASCADE ON DELETE CASCADE,
	CONSTRAINT fk_CharacterSlot_slotType FOREIGN KEY (slotType)
      REFERENCES slotTypes (slotType)
      ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT fk_Inventory_equippedItem FOREIGN KEY (equippedItem) 
      REFERENCES Equippable(itemId) 
      ON UPDATE CASCADE ON DELETE CASCADE,
    CONSTRAINT pk_CharacterSlot_customization  FOREIGN KEY (customization) 
      REFERENCES Customization(customizationId) 
      ON UPDATE CASCADE ON DELETE SET NULL
);


-- Insert some sample data
-- Insert data into the table 'Player'
INSERT INTO Player (accountId, `name`, emailAddress, isActive) 
  VALUES (1,	'Joe', 'joe@gamil.com', TRUE),
         (2,	'Bob', 'Bb123@gmail.com', FALSE),
         (33, 'Sherry', 'Sherry@northeastern.edu', TRUE),
         (90, 'Hello', 'Hhh@sina.com', TRUE),
         (529, 'Cool', 'coolcool@coo.com', FALSE),
         (1000, 'Kaden', 'Kaden112233@gmail.com', TRUE),
         (100, 'Daden', 'Daden112233@gmail.com', TRUE),
         (10, 'Gaden', 'Gaden112233@gmail.com', FALSE),
         (10000, 'Faden', 'Faden112233@gmail.com', TRUE),
         (100000, 'Laden', 'Laden112233@gmail.com', TRUE),
         (1000000, 'Maden', 'Maden112233@gmail.com', TRUE);
    
-- Insert data into the table 'Character'
INSERT INTO `Character` (characterId, accountId, characterFirstName, characterLastName) 
  VALUES (1,	33, 'Dragon', 'Li'),
         (2,	1, 'Fury', 'Zhang'),
         (4, 90, 'Iron', 'Chen'),
         (8, 33, 'Shadow', 'Li'),
         (100, 1000, 'Golden', 'Kong'),
         (200, 90, 'Panda', 'Wang'),
         (1000, 33, 'Tiger', 'Wang'),
         (10000, 100, 'Cat', 'Zhao'),
         (100000, 100, 'Dog', 'Heath'),
         (1000000, 100, 'Mouse', 'Adam'),
         (10000000, 100, 'Cow', 'Wei'),
         (100000000, 100, 'Red', 'Wang');
    
-- Insert data into the table 'Job'
INSERT INTO Job (jobId,jobName, jobLevel, MinLevelExp, MaxLevelExp) 
  VALUES (1,'King', 1, 0, 2000),
         (2,'King', 2, 2000, 5000),
         (3,'King', 3, 5000, 50000),
         (4,'Knight', 1, 0, 1000),
		 (5,'Knight', 2, 1000, 2000),
         (6,'Knight', 3, 2000, 3000),
         (7,'Civilian', 1, 0, 1000),
         (8,'Civilian', 2, 1000, 5000),
         (9,'Mage', 2, 0, 8000),
         (10,'Mage', 3, 8000, 80000);

-- Insert data into the table 'Currency'
INSERT INTO Currency (currencyName, totalCap, weeklyCap, discontinued) 
  VALUES ('Gold', 1000000, 50000, FALSE),
         ('Silver', 2000000, 2000, FALSE),
         ('Bronze', 300000, 300, TRUE),
         ('Diamonds', 5000000, 100000, FALSE),
         ('Copper', 150000, 1000, FALSE),
         ('Ruby', 4000000, 80000, FALSE),
		 ('Sapphire', 2500000, 30000, FALSE),
		 ('Emerald', 1800000, 7000, FALSE),
         ('Pearls', 120000, 500, TRUE),
         ('Platinum', 1000000, 50000, FALSE);

-- Insert data into the table 'Attributesy'
INSERT INTO Attributes (attribute) 
  VALUES ('maxHP'),
         ('strength'),
         ('dexterity'),
         ('vitality'),
         ('intelligence'),
         ('mind'),
         ('criticalHit'),
		 ('determination'),
         ('directHitRate'),
         ('defense'),
         ('magicDefense'),
         ('attackPower'),
         ('SkillSpeed'),
         ('attackMagicPotency'),
         ('HealingMagicPotency'),
         ('spellSpeed'),
         ('averageItemLevel'),
         ('tenacity'),
         ('piety');
         
-- Insert data into the table 'Attributesy'
INSERT INTO Item (itemId,itemName,maxStackSize,vendorPrice,canbeSold)
  VALUES (1, 'Health Potion', 5, 90, TRUE),
		 (2, 'Energy Elixir', 10, 100, TRUE),
         (3, 'Speed Boost', 8, 101, TRUE),
         (4, 'Fire Scroll', 5, 102, TRUE),
         (5, 'Invisibility Potion', 10, 103, TRUE),
         (6, 'Treasure Map', 5, 104, TRUE),
         (7, 'Lockpick Set', 3, 105, TRUE),
         (8, 'Lucky Charm',3, 0, FALSE),
         (9, 'Ancient Relic', 5, 107, TRUE),
         (10, 'Enchanted Amulet', 5, 108, TRUE),
         (11, 'Sword', 5, 0, FALSE),
		 (12, 'Bow', 5, 110, TRUE),
         (13, 'War Hammer', 5, 0, FALSE),
         (14, 'Staff', 5, 112, TRUE),
         (15, 'Dagger', 5, 113, TRUE),
         (16, 'Plate Armor', 5, 114, TRUE),
         (17, 'Cloak', 5, 115, TRUE),
         (18, 'Amulet',5,116,TRUE),
         (19, 'Leather Boots', 5, 117, TRUE),
         (20, 'Helmet', 5, 118, TRUE),
         (21, 'Poison Vial', 5, 119, TRUE),
         (22, 'Mage Robe', 5, 120, TRUE),
         (23, 'Thunderbolt Scroll', 5, 121, TRUE),
		 (24, 'Healing Crystal', 3, 0, FALSE),
		 (25, 'Dragon Scale', 1, 500, TRUE),
         (26, 'Harmony Potion', 10, 122, TRUE),
		 (27, 'Teleportation Scroll', 1, 123, TRUE),
		 (28, 'Mirror Shield', 1, 124, TRUE),
         (29, 'Vorpal Blade', 1, 0, FALSE),
         (30, 'Phoenix Feather', 5, 125, TRUE),
         (31, 'Scroll of Wisdom', 5, 126, TRUE),
         (32, 'Giant Strength Elixir', 5, 127, TRUE),
		 (33, 'Ice Shard', 5, 128, TRUE),
         (34, 'Eagle Eye Potion', 5, 129, TRUE),
         (35, 'Glowing Crystal', 5, 130, TRUE),
		 (36, 'Mystic Rune', 5, 131, TRUE),
         (37, 'Shadow Cloak', 5, 132, TRUE),
         (38, 'Potion of Levitation', 5, 133, TRUE),
         (39, 'Diamond Dagger', 1, 0, FALSE),
         (40, 'Silver Crown', 1, 134, TRUE);
         
-- Insert data into the table 'Consumable'
INSERT INTO Consumable (itemId,itemLevel,description)
  VALUE (1, 1, 'Restores 50 health points.'),
        (2, 3, 'Replenishes 30 energy.'),
        (3, 2, 'Increases movement speed.'),
        (4, 5, 'Casts a fireball spell.'),
        (5, 1, 'Makes you invisible.'),
        (6, 2, 'Restores 75 health points.'),
        (7, 4, 'Boosts energy regeneration.'),
        (8, 3, 'Grants a temporary speed boost.'),
        (9, 6, 'Unleashes a powerful fire spell.'),
		(10, 2, 'Induces invisibility for a short duration.');

-- Insert data into the table 'Miscellaneous'
INSERT INTO Miscellaneous (itemId,description)
  VALUE (11, 'Leads to hidden riches.'),
        (12, 'Used to pick locks.'),
        (13, 'Grants good luck for a short time.'),
        (14, 'An artifact with unknown powers.'),
		(15, 'Enhances your magical abilities.'),
        (16, 'Contains clues to hidden treasures.'),
        (17, 'A set of high-quality lockpicking tools.'),
        (18, 'Brings exceptional luck to the user.'),
	    (19, 'A mysterious artifact with ancient powers.'),
        (20, 'Amplifies magical prowess.');
        
-- Insert data into the table 'ConsumableBonus'
INSERT INTO ConsumableBonus (itemId,attribute,bonusPercentage,bonusCap)
  VALUE (2,'SkillSpeed',8,100),
		(3,'attackPower',10,100),
        (2,'defense',10,100),
        (3,'SkillSpeed',30,300),
        (1,'tenacity',10,1000),
        (11, 'tenacity', 15, 1500),
        (12, 'SkillSpeed', 12, 150),
        (13, 'attackPower', 15, 200),
        (14, 'defense', 20, 250),
        (15, 'SkillSpeed', 40, 400);

-- Insert sample data into table 'SlotTypes'
INSERT INTO SlotTypes (slotType)
  VALUE ('Main hand'),
		('head'),
		('hands'),
		('body'),
		('feet');

-- Insert sample data into table 'Equippable'
INSERT INTO Equippable (itemId, itemLevel, slotType, requiredJobLevel)
VALUES 
  (21, 1, 'Main hand', 1),
  (22, 2, 'Hands', 1),
  (23, 2, 'Hands', 1),
  (24, 1, 'Main hand', 1),
  (25, 3, 'Main hand', 2),
  (26, 2, 'Body', 1),
  (27, 1, 'Body', 1),
  (28, 3, 'Hands', 3),
  (29, 2, 'Feet', 2),
  (30, 2, 'Head', 2),
  (31, 3, 'Main hand', 2),
  (32, 2, 'Hands', 1),
  (33, 4, 'Main hand', 3),
  (34, 3, 'Body', 2),
  (35, 4, 'Feet', 2),
  (36, 2, 'Head', 1),
  (37, 3, 'Hands', 2),
  (38, 4, 'Main hand', 3),
  (39, 3, 'Body', 2),
  (40, 4, 'Feet', 2);


  
-- Insert sample data into table 'Weapon'
INSERT INTO Weapon (itemId,damageDone,autoAttack,attackDelay,associatedJob)
  VALUE (21, 100, 10, 1, 1),
        (22, 100, 5, 2, 1),
        (23, 100, 5, 3, 2),
        (24, 100, 10, 3, 3),
        (25, 100, 10, 4, 6),
        (26, 120, 12, 1, 2),
        (27, 150, 8, 2, 1),
        (28, 180, 10, 2, 4),
        (29, 130, 15, 3, 3),
        (30, 200, 20, 4, 5);

-- Insert sample data into table 'Gear'
INSERT INTO Gear (itemId,defenseRating,magicDefenseRating)
  VALUE (31, 100, 50),
        (32, 100, 50),
        (33, 100, 50),
        (34, 100, 50),
        (35, 100, 50),
        (36, 120, 60),
        (37, 130, 70),
        (38, 140, 80),
        (39, 110, 50),
        (40, 150, 90);
  
-- Insert sample data into table 'EquippableBonus'
INSERT INTO EquippableBonus (itemId,attribute,bonusValue)
  VALUE (21, 'Strength', 15),
	    (22, 'Defense', 120),
        (23, 'AttackPower', 80),
        (24, 'Strength', 50),
        (25, 'Defense', 150),
        (26, 'Strength', 20),
        (37, 'Defense', 100),
        (38, 'AttackPower', 120),
        (39, 'Strength', 25),
        (40, 'Defense', 110);
        
-- Insert sample data into table 'GearJob'
INSERT INTO GearJob(itemId, jobId)
 VALUES (31, 2),
        (32, 3),
        (33, 4),
        (34, 10),
        (35, 5),
        (36, 2),
        (37, 3),
        (38, 4),
        (39, 1),
        (40, 5);
 
-- Insert sample data into table 'CharacterAttribut'
INSERT INTO CharacterAttribute(characterId, attributeName,attributeValue)
  VALUES (1,'maxHP',100),
		 (2,'strength',100),
         (4,'dexterity',10),
         (4,'vitality',50),
         (8,'intelligence',10),
         (100,'maxHP',100),
		 (200,'strength',100),
         (100,'dexterity',10),
         (1000,'vitality',50),
         (10000,'intelligence',10);

-- Insert sample data into table 'Colors'
INSERT INTO Colors(color)
  VALUES ('Red'),
		 ('Blue'),
         ('Yellow'),
         ('Purple'),
         ('Green'),
         ('Orange'),
         ('Pink'),
         ('Brown'),
         ('Gray'),
         ('Black');

-- Insert sample data into table 'Customization'
INSERT INTO Customization(customizationId,itemId,dyeColor,isHighQuality,`condition`,madeBy)
  VALUES (1,21,'Red','High',50,1),
		 (2,22,'Blue','Normal',20,4),
         (3,24,'Red','Normal',10,100),
         (4,27,'Red','Normal',20,1),
         (5,27,'Blue','High',20,1),
         (6,23,'Red','High',50,1),
		 (7,25,'Blue','Normal',20,4),
         (8,26,'Red','Normal',10,100),
         (9,28,'Red','Normal',20,1000),
         (10,29,'Blue','High',20,1);


-- Insert sample data into table 'Inventory'
INSERT INTO Inventory (characterId, slotId, itemId, customizationId, quantity) 
  VALUES (1, 1, 21, 1, 5),
         (1, 2, 22, 2, 3),
         (100, 3, 23, 6, 2),
         (4, 4, 23, 6, 1),
         (8, 5, 23, 6, 1),
         (100, 1, 21, 1, 5),
         (200, 2, 22, 2, 3),
         (1000, 1, 23, 6, 2),
         (1000, 2, 23, 6, 1),
         (1000, 3, 23, 6, 1);
         
-- Insert sample data into table 'CharacterSlot'
INSERT INTO CharacterSlot (characterId, slotType, equippedItem, customization) 
  VALUES (1, 'Main hand', 21, 1),
         (2, 'hands', 22, NULL),
         (4, 'feet', 29, NULL),
         (1, 'hands', 27, 4),
         (100, 'head', 30, NULL),
         (100, 'Main hand', 21, 1),
         (200, 'hands', 22, NULL),
         (1000, 'feet', 29, NULL),
         (10000, 'hands', 27, 4),
         (100000, 'head', 30, NULL);
	
-- Insert sample data into table 'CharacterJob'
INSERT INTO CharacterJob (characterId, jobId, currentExp, isUnlocked, isCurrentJob ) 
  VALUES (1, 1, 100,TRUE, TRUE),
         (2, 1, 100,TRUE, TRUE),
         (4, 4, 100,TRUE, TRUE),
         (1, 4, 100,FALSE, FALSE),
         (4, 3, 100,FALSE, FALSE),
         (100, 5, 100,TRUE, TRUE),
         (100, 6, 100,FALSE, FALSE),
         (1000, 7, 100,TRUE, TRUE),
         (1000, 7, 100,FALSE, FALSE),
         (200, 7, 100,FALSE, FALSE);
         
-- Insert sample data into table 'CharacterCurrency'
INSERT INTO CharacterCurrency (characterId, currencyName, amountOwned, weeklyAmountOwned) 
  VALUES (1, 'Gold', 1000, 500),
         (2, 'Gold', 2000, 500),
         (1, 'Bronze', 3000, 500),
         (2, 'Bronze', 3000, 500),
         (4, 'Gold', 1000, 0),
         (100, 'Gold', 1000, 500),
         (200, 'Gold', 2000, 500),
         (100, 'Bronze', 3000, 500),
         (1000, 'Bronze', 3000, 500),
         (1000, 'Gold', 1000, 0);
   
