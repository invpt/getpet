package cs340.getpet.data;

import cs340.getpet.Persistence;

/**
 * Either dog or cat.
 */
public enum Species implements Persistence.DatabaseEnum {
    Dog, Cat;

    @Override
    public String toDatabaseRepresentation() {
        switch (this) {
            case Dog: return "dog";
            case Cat: return "cat";
        }

        // should never happen
        return null;
    }

    public static Species fromFormRepresentation(String s) {
        switch (s) {
            case "Dog": return Dog;
            case "Cat": return Cat;
        }

        // should never happen
        return null;
    }

    public static Species fromDatabaseRepresentation(String s) {
        switch (s) {
            case "dog": return Dog;
            case "cat": return Cat;
        }

        // should never happen
        return null;
    }
}
