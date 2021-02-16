package cs340.getpet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Persistence {
    Connection conn;

    public static class PersistenceException extends Exception {
        PersistenceException(String message) {
            super(message);
        }

        PersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public Persistence(String server, String database, String user, String password) throws PersistenceException {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://" + server + "/" + database, user, password);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create database connection", e);
        }
    }
}