package cs340.getpet.persistence;

import java.sql.Connection;

public class Persistence {
    Connection conn;

    public Persistence() {}

    public SearchResponse search(SearchRequest searchRequest) throws PersistenceException {
        return null;
    }

    public static class PersistenceException extends Exception {
        PersistenceException(String message) {
            super(message);
        }
    }
}
