package cs340.getpet.persistence;

import java.util.Arrays;

import checkers.nullness.quals.NonNull;
import checkers.nullness.quals.Nullable;

public class SearchRequest {
    public final @NonNull String species;
    public final @Nullable String[] genders;
    public final @Nullable String breed;
    public final @Nullable String[] colors;
    public final @Nullable String[] sizes;

    SearchRequest(@NonNull String species, @Nullable String[] genders, @Nullable String breed, @Nullable String[] colors, @Nullable String[] sizes) {
        this.species = species;
        this.genders = genders;
        this.breed = breed;
        this.colors = colors;
        this.sizes = sizes;
    }
}
