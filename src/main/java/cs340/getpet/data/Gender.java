package cs340.getpet.data;

import cs340.getpet.Persistence;

/**
 * Either male or female.
 */
public enum Gender implements Persistence.DatabaseEnum {
    Male, Female;

    @Override
    public String toDatabaseRepresentation() {
        switch (this) {
            case Male: return "m";
            case Female: return "f";
        }

        // should never happen
        return null;
    }

    public static Gender fromFormRepresentation(String s) {
        switch (s) {
            case "Male": return Male;
            case "Female": return Female;
        }

        // should never happen
        return null;
    }

    public static Gender fromDatabaseRepresentation(String s) {
        switch (s) {
            case "m": return Male;
            case "f": return Female;
        }

        // should never happen
        return null;
    }
}