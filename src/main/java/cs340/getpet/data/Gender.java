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

        throw new IllegalStateException();
    }

    public static Gender fromDatabaseRepresentation(String s) {
        switch (s) {
            case "m": return Male;
            case "f": return Female;
        }

        return null;
    }
}