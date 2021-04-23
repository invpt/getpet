package cs340.getpet.persistence;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

/**
 * A search query for finding an animal.
 */
public class SearchQuery {
    /**
     * The species of animal to search for.
     */
    public final @NonNull Species species;
    /**
     * The genders of animal to search for.
     */
    public final @Nullable Gender[] genders;
    /**
     * The breed of animal to search for.
     */
    public final @Nullable String breed;
    /**
     * The colors of animal to search for.
     */
    public final @Nullable Color[] colors;
    /**
     * The sizes of animal to search for.
     */
    public final @Nullable Size[] sizes;

    protected SearchQuery(@NonNull Species species, @Nullable Gender[] genders, @Nullable String breed, @Nullable Color[] colors, @Nullable Size[] sizes) {
        this.species = species;
        this.genders = genders;
        this.breed = breed;
        this.colors = colors;
        this.sizes = sizes;
    }
}
