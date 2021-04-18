package cs340.getpet.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import cs340.getpet.persistence.Animal.Color;
import cs340.getpet.persistence.Animal.Gender;
import cs340.getpet.persistence.Animal.Size;
import cs340.getpet.persistence.Animal.Species;
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

    private static Persistence persistence;

    @BeforeAll
    public static void initializePersistence() throws PersistenceException {
        persistence = new Persistence(":memory:");
    }

    @Test
    public void testAddAnimal() throws PersistenceException {
        int intakeNumber = persistence.newAnimal(testingAnimals[0]);
        Assertions.assertEquals(testingAnimals[0], persistence.getAnimal(intakeNumber));
    }
}
