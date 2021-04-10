package cs340.getpet.persistence;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;
import cs340.getpet.persistence.Animal.Color;
import cs340.getpet.persistence.Animal.Gender;
import cs340.getpet.persistence.Animal.Size;
import cs340.getpet.persistence.Animal.Species;

/**
 * A search query for finding an animal.
 */
public class SearchQuery {
    public final @NonNull Species species;
    public final @Nullable Gender[] genders;
    public final @Nullable String breed;
    public final @Nullable Color[] colors;
    public final @Nullable Size[] sizes;

    protected SearchQuery(@NonNull Species species, @Nullable Gender[] genders, @Nullable String breed, @Nullable Color[] colors, @Nullable Size[] sizes) {
        this.species = species;
        this.genders = genders;
        this.breed = breed;
        this.colors = colors;
        this.sizes = sizes;
    }
}
