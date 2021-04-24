package cs340.getpet.persistence;

/**
 * An in-memory representation of information about a cage of animals.
 */
public final class Cage {
    /**
     * The cage number of the cage.
     */
    public final int cageNumber;
    /**
     * The number of dogs housed in the cage.
     */
    public final int dogCount;
    /**
     * The number of cats housed in the cage.
     */
    public final int catCount;

    public Cage(int cageNumber, int dogCount, int catCount) {
        this.cageNumber = cageNumber;
        this.dogCount = dogCount;
        this.catCount = catCount;
    }
}
