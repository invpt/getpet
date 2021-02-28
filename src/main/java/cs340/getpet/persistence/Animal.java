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

    public static class Builder {
        Integer intakeNumber;
        String species;
        String breed;
        String size;
        String[] colors;
        String gender;
        Double weight;
        Boolean vaccinated;
        Boolean spayNeuter;
        String name;
        Date date;
        Boolean missing;

        public Builder() {}
        public Animal build() {
            if (species == null || breed == null || size == null || colors == null || gender == null || weight == null || vaccinated == null || spayNeuter == null || name == null || date == null || missing == null)
                // TODO: throwing a runtime exception without a message is probably a bad idea
                throw new RuntimeException();
            else
                return new Animal(this);
        }
        public Builder intakeNumber(int intakeNumber) { this.intakeNumber = intakeNumber; return this; }
        public Builder species(String species) { this.species = species; return this; }
        public Builder breed(String breed) { this.breed = breed; return this; }
        public Builder size(String size) { this.size = size; return this; }
        public Builder colors(String[] colors) { this.colors = colors; return this; }
        public Builder gender(String gender) { this.gender = gender; return this; }
        public Builder weight(double weight) { this.weight = weight; return this; }
        public Builder vaccinated(boolean vaccinated) { this.vaccinated = vaccinated; return this; }
        public Builder spayNeuter(boolean spayNeuter) { this.spayNeuter = spayNeuter; return this; }
        public Builder name(String name) { this.name = name; return this; }
        public Builder date(Date date) { this.date = date; return this; }
        public Builder missing(boolean missing) { this.missing = missing; return this; }
    }

    Animal(Builder b) {
        intakeNumber = b.intakeNumber;
        species = b.species;
        vaccinated = b.vaccinated;
        breed = b.breed;
        gender = b.gender;
        name = b.name;
        colors = b.colors;
        weight = b.weight;
        missing = b.missing;
        date = b.date;
        spayNeuter = b.spayNeuter;
        size = b.size;
    }

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