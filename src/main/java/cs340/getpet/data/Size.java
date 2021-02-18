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

        // should never happen
        return null;
    }

    public static Size fromFormRepresentation(String s) {
        switch (s) {
            case "Small": return Small;
            case "Medium": return Medium;
            case "Large": return Large;
        }

        // should never happen
        return null;
    }

    public static Size fromDatabaseRepresentation(String s) {
        switch (s) {
            case "small": return Small;
            case "medium": return Medium;
            case "large": return Large;
        }

        // should never happen
        return null;
    }
}