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
    sex CHAR(1) NOT NULL,
    name VARCHAR(50),
    color VARCHAR(50),
    weight DOUBLE NOT NULL, -- Pounds
    cage_number INT NOT NULL,
    owner_customer_id INT,
    missing BOOLEAN,
    date DATE NOT NULL
);

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Shiba Inu',
    sex = 'm',
    name = 'Doge',
    color = NULL,
    weight = 42.0,
    cage_number = 2,
    owner_customer_id = NULL,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'Short hair cat',
    vaccinated = TRUE,
    breed = 'm',
    sex = 'm',
    name = 'Tupac',
    color = 'Orange',
    weight = 8,
    cage_number = 1,
    owner_customer_id = 1,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Goberian',
    sex = 'm',
    name = 'Napolean',
    color = 'Golden',
    weight = 79,
    cage_number = 2,
    owner_customer_id = 2,
    missing = TRUE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'Bird',
    vaccinated = TRUE,
    breed = 'Scarlet Macaw',
    sex = 'f',
    name = 'baron',
    color = 'Red',
    weight = 3.2,
    cage_number = 3,
    owner_customer_id = 3,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = TRUE,
    breed = 'Springer Spaniel',
    sex = 'm',
    name = 'Sully',
    color = 'Brown',
    weight = 999.0,
    cage_number = 0,
    owner_customer_id = NULL,
    missing = false,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'cat',
    vaccinated = TRUE,
    breed = 'cat',
    sex = 'f',
    name = 'Tammy',
    color = 'White',
    weight = 10,
    cage_number = 2,
    owner_customer_id = 2,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'Bull',
    vaccinated = TRUE,
    breed = 'Cow',
    sex = 'm',
    name = 'Cowboy',
    color = 'Black',
    weight = 300,
    cage_number = 5,
    owner_customer_id = 5,
    missing = TRUE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'Dog',
    vaccinated = true,
    breed = 'Golden Doodle',
    sex = 'm',
    name = 'Piper',
    color = 'Black',
    weight = 46.7,
    cage_number = 8,
    owner_customer_id = 8,
    missing = false,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'Dog',
    vaccinated = TRUE,
    breed = 'German Shepherd',
    sex = 'm',
    name = 'Bacon',
    color = 'Black, Brown',
    weight = 15,
    cage_number = 9,
    owner_customer_id = 9,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'horse',
    vaccinated = TRUE,
    breed = 'm',
    sex = 'm',
    name = 'Duragno',
    color = 'White',
    weight = 300,
    cage_number = 10,
    owner_customer_id = 10,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'goat',
    vaccinated = false,
    breed = 'goat breed?',
    sex = 'f',
    name = 'Roger',
    color = 'Golden',
    weight = 60.0,
    cage_number = 11,
    owner_customer_id = 11,
    missing = TRUE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'alligator',
    vaccinated = TRUE,
    breed = 'crocodile',
    sex = 'f',
    name = 'Crocogator',
    color = 'Green',
    weight = 555.555,
    cage_number = 2,
    owner_customer_id = 2,
    missing = FALSE,
    date = CURRENT_DATE;

INSERT INTO Animals SET
    species = 'dog',
    vaccinated = FALSE,
    breed = 'corgi',
    sex = 'f',
    name = 'Peter Dinklage',
    color = 'Golden, White',
    weight = 5,
    cage_number = 13,
    owner_customer_id = 13,
    missing = FALSE,
    date = CURRENT_DATE;