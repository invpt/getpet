package cs340.getpet.persistence;


/**
 * A search query for finding an animal.
 */
public class SearchQuery {
    /**
     * The species of animal to search for.
     */
    public final Species species;
    /**
     * The genders of animal to search for.
     */
    public final Gender[] genders;
    /**
     * The breed of animal to search for.
     */
    public final String breed;
    /**
     * The colors of animal to search for.
     */
    public final Color[] colors;
    /**
     * The sizes of animal to search for.
     */
    public final Size[] sizes;

    protected SearchQuery(Species species, Gender[] genders, String breed, Color[] colors, Size[] sizes) {
        this.species = species;
        this.genders = genders;
        this.breed = breed;
        this.colors = colors;
        this.sizes = sizes;
    }
}
