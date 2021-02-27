package cs340.getpet.persistence;

import java.util.Date;

/**
 * An in-memory representation of an entity from the Animals table.
 */
public class Animal {
    /**
     * The intake number of the animal. May be null.
     */
    public final int intakeNumber;
    /**
     * The species of the animal.
     */
    public final String species;
    /**
     * The breed of the animal.
     */
    public final String breed;
    /**
     * The size of the animal.
     */
    public final String size;
    /**
     * The primary colors that the animal's fur has.
     */
    public final String[] colors;
    /**
     * The gender of the animal.
     */
    public final String gender;
    /**
     * The weight of the animal.
     */
    public final double weight;
    /**
     * True if the animal is vaccinated, else false.
     */
    public final boolean vaccinated;
    /**
     * True if the animal has been spayed or neutered, else false.
     */
    public final boolean spayNeuter;
    /**
     * The name of the animal.
     */
    public final String name;
    /**
     * The date the animal was brought into the shelter.
     */
    public final Date date;
    /**
     * True if the animal is known to be missing, else false.
     */
    public boolean missing;

    Animal(int intakeNumber, String species, String breed, String size, String[] colors, String gender, double weight, boolean vaccinated, boolean spayNeuter, String name, Date date, boolean missing) {
        this.intakeNumber = intakeNumber;
        this.species = species;
        this.breed = breed;
        this.size = size;
        this.colors = colors;
        this.gender = gender;
        this.weight = weight;
        this.vaccinated = vaccinated;
        this.spayNeuter = spayNeuter;
        this.name = name;
        this.date = date;
        this.missing = missing;
    }
}