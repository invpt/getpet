package cs340.getpet.data;

import cs340.getpet.Persistence;

/**
 * One of black, gray, white, brown, or gold.
 */
public enum Color implements Persistence.DatabaseEnum {
    Black, White, Brown, Gold, DarkGray, LightGray;

    @Override
    public String toString() {
        switch (this) {
            case Black: return "Black";
            case White: return "White";
            case Brown: return "Brown";
            case Gold: return "Gold";
            case DarkGray: return "Dark Gray";
            case LightGray: return "Light Gray";
            default: throw new IllegalStateException();
        }
    }

    @Override
    public String toDatabaseRepresentation() {
        switch (this) {
            case Black: return "black";
            case White: return "white";
            case Brown: return "brown";
            case Gold: return "gold";
            case DarkGray: return "dGray";
            case LightGray: return "lGray";
        }

        throw new IllegalStateException();
    }

    public static Color fromDatabaseRepresentation(String s) {
        switch (s) {
            case "black": return Black;
            case "white": return White;
            case "brown": return Brown;
            case "gold": return Gold;
            case "dGray": return DarkGray;
            case "lGray": return LightGray;
        }

        return null;
    }
}