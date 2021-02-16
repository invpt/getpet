package cs340.getpet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Persistence {
    /**
     * The connection to the database.
     */
    Connection conn;

    public static class PersistenceException extends Exception {
        private static final long serialVersionUID = 1062624232678597426L;

        PersistenceException(String message) {
            super(message);
        }

        PersistenceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Creates a Persistence, connecting to the database using the given information.
     * 
     * @param server the server address, including port if applicable
     * @param database the database name within the server
     * @param user the user for the database
     * @param password the password for the user
     * @throws PersistenceException when the database connection could not be created
     */
    public Persistence(String server, String database, String user, String password) throws PersistenceException {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://" + server + "/" + database, user, password);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create database connection", e);
        }
    }

    /**
     * Searches for animals in the database.
     * 
     * @param query the search query
     * @return the results of the searc
     * @throws PersistenceException when the database statement
     *                              failed to execute
     */
    public ResultSet animalSearch(SearchQuery query) throws PersistenceException {
        // build query string
        String queryString = new StringBuilder("SELECT * FROM Animals WHERE ")
                .append(String.join(" AND ", query.ands))
                .toString();

        try (PreparedStatement stmt = conn.prepareStatement(queryString)) {
            // set parameters
            int i = 1;
            for (DatabaseValue parameter : query.parameters)
                stmt.setObject(i++, parameter.toDatabaseRepresentation());
            
            return stmt.executeQuery();
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create or execute animal search statement", e);
        }
    }

    /**
     * A search query for animals. Can be built with {@link Builder}. 
     */
    public static class SearchQuery {
        /**
         * The conditions to join with " AND " in the where clause.
         */
        LinkedList<String> ands = new LinkedList<>();
        /**
         * The values to use for the parameters of the prepared statement.
         */
        LinkedList<DatabaseValue> parameters = new LinkedList<>();

        /**
         * Builder class for {@link SearchQuery}.
         */
        public static class Builder {
            /**
             * The species of animal to search for. Should not be null.
             */
            // TODO: enforce nonnull
            private Species species;
            /**
             * The genders of animal to search for. If empty, both are allowed.
             */
            private LinkedList<Gender> genders = new LinkedList<>();
            /**
             * The breed of animal to search for. If null, no restrictions
             * are placed on breed.
             */
            private DatabaseObject<String> breed;
            /**
             * 
             */
            private LinkedList<Color> colors = new LinkedList<>();
            private LinkedList<Size> sizes = new LinkedList<>();

            public Builder() {}

            public SearchQuery build() {
                SearchQuery sq = new SearchQuery();

                sq.is("species", species);
                sq.in("gender", genders);
                sq.is("breed", breed);
                sq.has("color", colors);
                sq.in("size", sizes);

                // always:
                sq.is("vaccinated", new DatabaseObject<>(true));
                sq.is("spayneuter", new DatabaseObject<>(true));

                return sq;
            }

            /**
             * Sets the species of animal to search for. Must be set before building.
             * 
             * @param species the species of animal to search for
             * @return this
             */
            public Builder species(Species species) {
                this.species = species;
                return this;
            }

            /**
             * Add a gender to search for. If uncalled before building, no restrictions are
             * placed on gender.
             * 
             * @param gender the gender of animal to allow in the query
             * @return this
             */
            public Builder gender(Gender gender) {
                genders.add(gender);
                return this;
            }

            /**
             * Sets the breed of animal to search for. If unset before building, no restriction
             * is placed on breed.
             * 
             * @param breed the breed of animal to search for
             * @return this
             */
            public Builder breed(DatabaseObject<String> breed) {
                this.breed = breed;
                return this;
            }

            /**
             * Adds a color to search for. If uncalled before building, no restrictions are
             * placed on gender.
             * 
             * @param color the color of animal to allow in the query
             * @return this
             */
            public Builder color(Color color) {
                colors.add(color);
                return this;
            }

            /**
             * Adds a size to search for. If uncalled before building, no restrictions are
             * placed on size.
             * 
             * @param size the size of animal to allow in the query
             * @return this
             */
            public Builder size(Size size) {
                sizes.add(size);
                return this;
            }
        }

        protected SearchQuery() {}

        /**
         * Requires that the given attribute is equal to the given value.
         * 
         * @param attributeName the attribute to check
         * @param value the value to check the attribute against
         */
        protected void is(String attributeName, DatabaseValue value) {
            if (value != null) {
                // add query text
                ands.add(new StringBuilder(attributeName).append(" = ?").toString());

                // add parameters
                parameters.add(value);
            }
        }

        /**
         * Requires that the given attribute is one of the given values.
         * 
         * @param attributeName the attribute to check
         * @param values the values to check the attribute against
         */
        protected void in(String attributeName, LinkedList<? extends DatabaseValue> values) {
            if (!values.isEmpty()) {
                // add query text
                StringBuilder sb = new StringBuilder(attributeName).append(" IN (");
                for (int i = 0; i < values.size() - 1; ++i)
                    sb.append("?,");
                sb.append('?');
                ands.add(sb.append(')').toString());

                // add parameters
                parameters.addAll(values);
            }
        }

        /**
         * Requires that the given attribute contains one of the given enum values.
         * 
         * @param attributeName the given attribute. this is a string, as that is how
         *                      we represent enums in the database.
         * @param values the enum to check that the value contains
         */
        protected void has(String attributeName, LinkedList<? extends DatabaseEnum> values) {
            if (!values.isEmpty()) {
                // add query text
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < values.size() - 1; ++i)
                    sb.append(attributeName).append(" LIKE ? OR ");
                sb.append(attributeName).append(" LIKE ?");
                ands.add(sb.append(')').toString());

                // add parameters
                for (DatabaseEnum value : values)
                    parameters.add(new DatabaseObject<>("%" + value.toDatabaseString() + "%"));
            }
        }
    }

    /**
     * A Java value that has a database representation.
     */
    public static interface DatabaseValue {
        /**
         * @return the representation of the value that
         *         should be given to the JDBC to be stored in the database
         */
        public Object toDatabaseRepresentation();
    }

    /**
     * A generic DatabaseValue that can be directly stored in the database.
     */
    public static class DatabaseObject<T> implements DatabaseValue {
        public final T value;

        public DatabaseObject(T value) {
            this.value = value;
        }

        @Override
        public Object toDatabaseRepresentation() {
            return value;
        }
    }

    /**
     * An enumeration that can be stored and retrieved from the database.
     */
    public static interface DatabaseEnum extends DatabaseValue {
        /**
         * Converts the enum value to the string representation in the database.
         * 
         * @return the string representation in the database
         */
        public String toDatabaseString();
        /**
         * Converts a string representation from the database to an enum value.
         * 
         * @param s the string representation
         * @return the enum value corresponding with the string representation
         */
        public static DatabaseEnum fromDatabaseString(String s) { return null; }

        @Override
        default Object toDatabaseRepresentation() {
            return toDatabaseString();
        }
    }

    /**
     * Either male or female.
     */
    public static enum Gender implements DatabaseEnum {
        Male, Female;

        public String toDatabaseString() {
            switch (this) {
                case Male: return "m";
                case Female: return "f";
            }

            // should never happen
            return null;
        }

        public static Gender fromDatabaseString(String s) {
            switch (s) {
                case "m": return Male;
                case "f": return Female;
            }

            // should never happen
            return null;
        }
    }

    /**
     * Either dog or cat.
     */
    public static enum Species implements DatabaseEnum {
        Dog, Cat;

        public String toDatabaseString() {
            switch (this) {
                case Dog: return "dog";
                case Cat: return "cat";
            }

            // should never happen
            return null;
        }

        public static Species fromDatabaseString(String s) {
            switch (s) {
                case "dog": return Dog;
                case "cat": return Cat;
            }

            // should never happen
            return null;
        }
    }

    /**
     * One of black, gray, white, brown, or gold.
     */
    public static enum Color implements DatabaseEnum {
        Black, Gray, White, Brown, Gold;

        public String toDatabaseString() {
            switch (this) {
                case Black: return "black";
                case Gray: return "gray";
                case White: return "white";
                case Brown: return "brown";
                case Gold: return "gold";
            }

            // should never happen
            return null;
        }

        public static Color fromDatabaseString(String s) {
            switch (s) {
                case "black": return Black;
                case "gray": return Gray;
                case "white": return White;
                case "brown": return Brown;
                case "gold": return Gold;
            }

            // should never happen
            return null;
        }
    }

    /**
     * One of small, medium, or large.
     */
    public static enum Size implements DatabaseEnum {
        Small, Medium, Large;

        public String toDatabaseString() {
            switch (this) {
                case Small: return "small";
                case Medium: return "medium";
                case Large: return "large";
            }

            // should never happen
            return null;
        }

        public static Size fromDatabaseString(String s) {
            switch (s) {
                case "small": return Small;
                case "medium": return Medium;
                case "large": return Large;
            }

            // should never happen
            return null;
        }
    }
}