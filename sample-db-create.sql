-- To initialize your local MariaDB instance with data, just go into the
-- MariaDB command-line interface as the root MariaDB user, and paste this in.
-- However, WATCH OUT: This script will delete the "getpet" database if it exists.
-- Just be careful to avoid losing data.
DROP DATABASE getpet;
CREATE DATABASE getpet;
USE getpet;

CREATE TABLE Animals (
    intake_number INT,
    species VARCHAR(50) NOT NULL,
    vaccinated BOOLEAN,
    breed VARCHAR(50),
    gender CHAR(1) NOT NULL,
    name VARCHAR(50),
    color VARCHAR(50),
    weight DOUBLE NOT NULL, -- Pounds
    cageNumber INT NOT NULL,
    ownerCustomerId INT,
    missing BOOLEAN,
    date DATE NOT NULL,
    spayNeuter BOOLEAN,
    size VARCHAR(50)
);

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Shiba Inu',
    gender = 'm',
    name = 'Doge',
    color = 'gold',
    weight = 42.0,
    cageNumber = 2,
    ownerCustomerId = NULL,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter = true,size = 'medium';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'cat',
    vaccinated = TRUE,
    breed = 'Short hair',
    gender = 'm',
    name = 'Tupac',
    color = 'gold',
    weight = 8,
    cageNumber = 1,
    ownerCustomerId = 1,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter = true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Goberian',
    gender = 'm',
    name = 'Napolean',
    color = 'gold',
    weight = 79,
    cageNumber = 2,
    ownerCustomerId = 2,
    missing = TRUE,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'Bird',
    vaccinated = TRUE,
    breed = 'Scarlet Macaw',
    gender = 'f',
    name = 'baron',
    color = 'Red',
    weight = 3.2,
    cageNumber = 3,
    ownerCustomerId = 3,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Springer Spaniel',
    gender = 'm',
    name = 'Sully',
    color = 'brown',
    weight = 999.0,
    cageNumber = 0,
    ownerCustomerId = NULL,
    missing = false,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'cat',
    vaccinated = TRUE,
    breed = 'cat',
    gender = 'f',
    name = 'Tammy',
    color = 'white',
    weight = 10,
    cageNumber = 2,
    ownerCustomerId = 2,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = true,
    breed = 'Golden Doodle',
    gender = 'm',
    name = 'Piper',
    color = 'black',
    weight = 46.7,
    cageNumber = 8,
    ownerCustomerId = 8,
    missing = false,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = TRUE,
    breed = 'German Shepherd',
    gender = 'm',
    name = 'Bacon',
    color = 'black brown',
    weight = 15,
    cageNumber = 9,
    ownerCustomerId = 9,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';

INSERT INTO Animals
    intakeNumber = 69,
    species = 'dog',
    vaccinated = FALSE,
    breed = 'Corgi',
    gender = 'f',
    name = 'Peter Dinklage',
    color = 'gold white',
    weight = 5,
    cageNumber = 13,
    ownerCustomerId = 13,
    missing = FALSE,
    date = CURRENT_DATE,spayNeuter=true,size = 'INSERT SIZE HERE';