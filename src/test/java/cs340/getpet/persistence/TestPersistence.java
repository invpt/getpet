package cs340.getpet.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import cs340.getpet.persistence.Persistence.PersistenceException;

public class TestPersistence {
    private static final Animal[] testingAnimals = new Animal[] {
        new Animal.Builder()
                .cageNumber(5)
                .species(Species.DOG)
                .breed("Shiba Inu")
                .size(Size.MEDIUM)
                .colors(new Color[] { Color.GOLD })
                .gender(Gender.MALE)
                .weight(42.0)
                .vaccinated(true)
                .spayNeuter(true)
                .name("Doge")
                .missing(false)
                .build(),
    };

    @Test
    public void testAddAnimal() throws PersistenceException {
        Persistence persistence = new Persistence(":memory:");

        int intakeNumber = persistence.newAnimal(testingAnimals[0]);
        Assertions.assertEquals(testingAnimals[0], persistence.getAnimal(intakeNumber));
    }

    // Here are some ideas for tests:
    // Persistence.search:
    //      - Make a test that searches with an empty query, assert that it has all of the sample animals (Found in src/resources/sample-db-create.sql)
    //      - Make a test that searches with a very specific query, assert that is has the expected sample animal
    //      - Make a test that searches with a typical user query, assert that it has the expected sample animal(s)
    //      - Make a test that adds an animal and searches for it immediately after with a precise query
    // Persistence.newAnimal:
    //      - Make a test that adds the same animal twice, assert that the two intake numbers returned back are different
    //      - Make a test that adds an animal and deletes it immediately, making sure that it is deleted properly
    // Persistence.deleteAnimal:
    //      - Make a test that adds an animal, updates it, then deletes it

    @Test
    public void testDeleteAnimal() throws PersistenceException {
        Persistence persistence = new Persistence(":memory:");

        final int INTAKE_NUMBER = 1;

        Animal preexistingDoge = persistence.getAnimal(INTAKE_NUMBER);
        Assertions.assertNotNull(preexistingDoge);
        
        persistence.deleteAnimal(INTAKE_NUMBER);

        Animal doge = persistence.getAnimal(INTAKE_NUMBER);
        Assertions.assertNull(doge);
    }

    @Test
    public void testDeleteTwice() throws PersistenceException {
        Persistence persistence = new Persistence(":memory:");

        final int PREEXISTING_INTAKE_NUMBER = 1;

        // Assert that the animal already exists
        Assertions.assertNotNull(persistence.getAnimal(PREEXISTING_INTAKE_NUMBER));

        // Delete the animal
        Assertions.assertTrue(persistence.deleteAnimal(PREEXISTING_INTAKE_NUMBER));

        // Assert that it no longer exists
        Assertions.assertNull(persistence.getAnimal(PREEXISTING_INTAKE_NUMBER));

        // Delete a second time
        Assertions.assertFalse(persistence.deleteAnimal(PREEXISTING_INTAKE_NUMBER));

        // Assert that is still does not exist
        Assertions.assertNull(persistence.getAnimal(PREEXISTING_INTAKE_NUMBER));
    }

    @Test
    public void testDeleteNonexistent() throws PersistenceException {
        Persistence persistence = new Persistence(":memory:");

        final int NONEXISTENT_INTAKE_NUMBER = 20;

        // Assert that the animal does not already exist
        Assertions.assertNull(persistence.getAnimal(NONEXISTENT_INTAKE_NUMBER));

        // Try to delete it
        Assertions.assertFalse(persistence.deleteAnimal(NONEXISTENT_INTAKE_NUMBER));

        // Assert that it still does not exist
        Assertions.assertNull(persistence.getAnimal(NONEXISTENT_INTAKE_NUMBER));
    }

    @Test
    public void testDeletePreexisting() throws PersistenceException {
        Persistence persistence = new Persistence(":memory:");

        final int PREEXISTING_INTAKE_NUMBER = 1;

        // Assert that the animal already exists
        Assertions.assertNotNull(persistence.getAnimal(PREEXISTING_INTAKE_NUMBER));

        // Delete the animal
        Assertions.assertTrue(persistence.deleteAnimal(PREEXISTING_INTAKE_NUMBER));

        // Assert that it no longer exists
        Assertions.assertNull(persistence.getAnimal(PREEXISTING_INTAKE_NUMBER));
    }
}
