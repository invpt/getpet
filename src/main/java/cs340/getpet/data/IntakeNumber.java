package cs340.getpet.data;

import cs340.getpet.Persistence;

/**
 * An intake number of an animal that can be stored in the database.
 */
public class IntakeNumber implements Persistence.DatabaseValue<Integer> {
    public final int number;

    public IntakeNumber(int number) {
        this.number = number;
    }

    @Override
    public Integer toDatabaseRepresentation() {
        return number;
    }
}