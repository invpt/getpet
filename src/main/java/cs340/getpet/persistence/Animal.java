package cs340.getpet.persistence;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

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
    public final Species species;
    /**
     * The breed of the animal.
     */
    public final String breed;
    /**
     * The size of the animal.
     */
    public final Size size;
    /**
     * The primary colors that the animal's fur has.
     */
    public final Color[] colors;
    /**
     * The gender of the animal.
     */
    public final Gender gender;
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

    public enum Species {
        @SerializedName("dog")
        DOG,
        @SerializedName("cat")
        CAT,
    }

    public enum Size {
        @SerializedName("small")
        SMALL,
        @SerializedName("medium")
        MEDIUM,
        @SerializedName("large")
        LARGE,
    }

    public enum Color {
        @SerializedName("black")
        BLACK,
        @SerializedName("white")
        WHITE,
        @SerializedName("brown")
        BROWN,
        @SerializedName("gold")
        GOLD,
        @SerializedName("dGray")
        DARK_GRAY,
        @SerializedName("lGray")
        LIGHT_GRAY,
    }

    public enum Gender {
        @SerializedName("m")
        MALE,
        @SerializedName("f")
        FEMALE,
    }

    public static class Builder {
        Integer intakeNumber;
        Species species;
        String breed;
        Size size;
        Color[] colors;
        Gender gender;
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
                throw new RuntimeException("Invalid call to build(): One or more required variables is unset!");
            else
                return new Animal(this);
        }
        public Builder intakeNumber(int intakeNumber) { this.intakeNumber = intakeNumber; return this; }
        public Builder species(Species species) { this.species = species; return this; }
        public Builder breed(String breed) { this.breed = breed; return this; }
        public Builder size(Size size) { this.size = size; return this; }
        public Builder colors(Color[] colors) { this.colors = colors; return this; }
        public Builder gender(Gender gender) { this.gender = gender; return this; }
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
}