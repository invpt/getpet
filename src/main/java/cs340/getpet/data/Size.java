package cs340.getpet.data;

import cs340.getpet.Persistence;

/**
 * One of small, medium, or large.
 */
public enum Size implements Persistence.DatabaseEnum {
    Small, Medium, Large;

    @Override
    public String toDatabaseRepresentation() {
        switch (this) {
            case Small: return "small";
            case Medium: return "medium";
            case Large: return "large";
        }

        throw new IllegalStateException();
    }

    public static Size fromDatabaseRepresentation(String s) {
        switch (s) {
            case "small": return Small;
            case "medium": return Medium;
            case "large": return Large;
        }

        return null;
    }
}