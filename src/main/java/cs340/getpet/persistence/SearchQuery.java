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
    /**
     * The cage number to search for animals.
     */
    public final Integer cageNumber;
    /**
     * Whether or not animals should be required to be vaccinated.
     */
    public final boolean vaccinated;
    /**
     * Whether or not animals should be required to be spayed/neutered.
     */
    public final boolean spayNeuter;

    protected SearchQuery(Species species, Gender[] genders, String breed, Color[] colors, Size[] sizes, Integer cageNumber, boolean vaccinated, boolean spayNeuter) {
        this.species = species;
        this.genders = genders;
        this.breed = breed;
        this.colors = colors;
        this.sizes = sizes;
        this.cageNumber = cageNumber;
        this.vaccinated = vaccinated;
        this.spayNeuter = spayNeuter;
    }
}
