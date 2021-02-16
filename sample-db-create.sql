-- To initialize your local MariaDB instance with data, just go into the
-- MariaDB command-line interface as the root MariaDB user, and paste this in.
-- However, WATCH OUT: This script will delete the "getpet" database if it exists.
-- Just be careful to avoid losing data.
DROP DATABASE getpet;
CREATE DATABASE getpet;
USE getpet;

CREATE TABLE Animals (
    species VARCHAR(50) NOT NULL,
    vaccinated BOOLEAN,
    breed VARCHAR(50),
    gender CHAR(1) NOT NULL,
    name VARCHAR(50),
    color VARCHAR(50),
    weight DOUBLE NOT NULL, -- Pounds
    cage_number INT NOT NULL,
    owner_customer_id INT,
    missing BOOLEAN,
    date DATE NOT NULL,
    spayneuter BOOLEAN
);

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Shiba Inu',
    gender = 'm',
    name = 'Doge',
    color = 'gold',
    weight = 42.0,
    cage_number = 2,
    owner_customer_id = NULL,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'cat',
    vaccinated = TRUE,
    breed = 'Short hair',
    gender = 'm',
    name = 'Tupac',
    color = 'gold',
    weight = 8,
    cage_number = 1,
    owner_customer_id = 1,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Goberian',
    gender = 'm',
    name = 'Napolean',
    color = 'gold',
    weight = 79,
    cage_number = 2,
    owner_customer_id = 2,
    missing = TRUE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'Bird',
    vaccinated = TRUE,
    breed = 'Scarlet Macaw',
    gender = 'f',
    name = 'baron',
    color = 'Red',
    weight = 3.2,
    cage_number = 3,
    owner_customer_id = 3,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Springer Spaniel',
    gender = 'm',
    name = 'Sully',
    color = 'brown',
    weight = 999.0,
    cage_number = 0,
    owner_customer_id = NULL,
    missing = false,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'cat',
    vaccinated = TRUE,
    breed = 'cat',
    gender = 'f',
    name = 'Tammy',
    color = 'white',
    weight = 10,
    cage_number = 2,
    owner_customer_id = 2,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = true,
    breed = 'Golden Doodle',
    gender = 'm',
    name = 'Piper',
    color = 'black',
    weight = 46.7,
    cage_number = 8,
    owner_customer_id = 8,
    missing = false,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'German Shepherd',
    gender = 'm',
    name = 'Bacon',
    color = 'black brown',
    weight = 15,
    cage_number = 9,
    owner_customer_id = 9,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = FALSE,
    breed = 'Corgi',
    gender = 'f',
    name = 'Peter Dinklage',
    color = 'gold white',
    weight = 5,
    cage_number = 13,
    owner_customer_id = 13,
    missing = FALSE,
    date = CURRENT_DATE,spayneuter = true;