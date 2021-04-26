-- NOTE: YOU DO NOT NEED TO APPLY THIS MANUALLY! The code now
--       does this for you, so it is no longer necessary.
-- This is code to initialize the database with sample data.
DROP TABLE IF EXISTS Animals;
CREATE TABLE IF NOT EXISTS Animals (
    intakeNumber INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    species TEXT NOT NULL,
    vaccinated INTEGER,
    breed TEXT,
    gender TEXT NOT NULL,
    name TEXT,
    color TEXT NOT NULL,
    weight REAL NOT NULL, -- Pounds
    cageNumber INTEGER NOT NULL,
    ownerCustomerId INTEGER,
    missing INTEGER,
    spayNeuter INTEGER,
    size TEXT                                 -- Dogs: Small 2-22 lbs, Medium 23-57lb, Large 58+ lbs     *As an adult*
	                                          -- Cats: Small 2-10 lbs, Medium 10-15 lbs, Large 15+ lbs   *As an adult*
);

INSERT INTO Animals VALUES(
    0001,
    "dog",
    1,
    "Shiba Inu",
    "m",
    "Doge",
    "gold",
    42.0,
    2,
    NULL,
    0,
	1,
	"medium");

INSERT INTO Animals VALUES(
    0002,
    "cat",
    1,
    "Somali Cat",
    "m",
    "Tupac",
    "gold",
    8,
    1,
    1,
    0,
	1,
	"small");

INSERT INTO Animals VALUES(
    0003,
    "dog",
    1,
    "Goberian",
    "m",
    "Napolean",
    "gold",
    79,
    2,
    2,
    1,
	1,
	"large");

INSERT INTO Animals VALUES(
    0004,
    "dog",
    1,
    "Springer Spaniel",
    "m",
    "Sully",
    "brown",
    46.0,
    0,
    NULL,
    0,
	1,
	"medium");

INSERT INTO Animals VALUES(
    0005,
    "cat",
    1,
    "Siamese",
    "f",
    "Sammy",
    "white",
    10,
    2,
    2,
    0,
	1,
	"medium");

INSERT INTO Animals VALUES(
    0006,
    "cat",
    1,
    "Munchkin",
    "m",
    "Melvin",
    "white",
    5,
    2,
    2,
    0,
	1,
	"small");

INSERT INTO Animals VALUES(
    0007,
    "dog",
    1,
    "Golden Doodle",
    "m",
    "Piper",
    "black",
    46.7,
    8,
    8,
    0,
	1,
	"medium");

INSERT INTO Animals VALUES(
    0008,
    "dog",
    1,
    "German Shepherd",
    "m",
    "Bacon",
    "black,brown",
    72,
    9,
    9,
    0,
	1,
	"large");

INSERT INTO Animals VALUES(
    0009,
    "dog",
    1,
    "Corgi",
    "f",
    "Peter Dinklage",
    "gold,white",
    5,
    13,
    13,
    0,
	1,
	"small");