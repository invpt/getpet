package cs340.getpet;

import cs340.getpet.data.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class Persistence {
    /**
     * The connection to the database.
     */
    Connection conn;

    public static class PersistenceException extends Exception {
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
     * Adds an animal to the database.
     * 
     * @param animal the animal to add
     * @throws PersistenceException when the database statement
     *                              fails to execute
     */
    public void addAnimal(Animal animal) throws PersistenceException {
        // TODO
    }

    /**
     * Updates an animal in the database.
     * 
     * @param intakeNumber the intake number of the animal to update
     * @param animal the new information to update the animal with
     * @throws PersistenceException when the database statement
     *                              fails to execute
     */
    public void updateAnimal(int intakeNumber, Animal animal) throws PersistenceException {
        // TODO
    }

    public Animal getAnimal() {
        // TODO
        return null;
    }

    /**
     * Searches for animals in the database.
     * 
     * @param query the search query
     * @return the results of the search
     * @throws PersistenceException when the database statement
     *                              fails to execute
     */
    public AnimalSearchResult findAnimal(AnimalSearchQuery query) throws PersistenceException {
        // build query string
        String queryString = new StringBuilder("SELECT * FROM Animals WHERE ")
                .append(String.join(" AND ", query.ands))
                .toString();

        try (PreparedStatement stmt = conn.prepareStatement(queryString)) {
            // set parameters
            int i = 1;
            for (DatabaseValue<?> parameter : query.parameters)
                stmt.setObject(i++, parameter.toDatabaseRepresentation());

            return new AnimalSearchResult(stmt.executeQuery());
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create or execute animal search statement", e);
        }
    }

    public static class AnimalSearchResult {
        private final ResultSet resultSet;

        AnimalSearchResult(ResultSet resultSet) {
            this.resultSet = resultSet;
        }

		public Animal next() throws PersistenceException {
            try {
                if (resultSet.next()) {
                    String[] colorStrings = resultSet.getString("color").split(" ");
                    Color[] colors = new Color[colorStrings.length];
                    for (int i = 0; i < colorStrings.length; ++i) {
                        colors[i] = Color.fromDatabaseRepresentation(colorStrings[i]);
                    }

                    return new Animal.Builder()
                            .intakeNumber(new IntakeNumber(resultSet.getInt("intakeNumber")))
                            .species(Species.fromDatabaseRepresentation(resultSet.getString("species")))
                            .breed(resultSet.getString("breed"))
                            .size(Size.fromDatabaseRepresentation(resultSet.getString("size")))
                            .colors(colors)
                            .gender(Gender.fromDatabaseRepresentation(resultSet.getString("gender")))
                            .weight(resultSet.getDouble("weight"))
                            .vaccinated(resultSet.getBoolean("vaccinated"))
                            .spayNeuter(resultSet.getBoolean("spayNeuter"))
                            .name(resultSet.getString("name"))
                            .date(resultSet.getDate("date"))
                            .missing(resultSet.getBoolean("missing"))
                            .build();
                } else {
                    return null;
                }
            } catch (SQLException e) {
                throw new PersistenceException("Error while getting next search result", e);
            }
		}
    }

    /**
     * A search query for animals. Can be built with {@link Builder}. 
     */
    public static class AnimalSearchQuery {
        /**
         * The conditions to join with " AND " in the where clause.
         */
        LinkedList<String> ands = new LinkedList<>();
        /**
         * The values to use for the parameters of the prepared statement.
         */
        LinkedList<DatabaseValue<?>> parameters = new LinkedList<>();

        /**
         * Builder class for {@link AnimalSearchQuery}.
         */
        public static class Builder {
            /**
             * The species of animal to search for. Should not be null.
             */
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
             * The colors of animal to search for. If empty, all are allowed.
             */
            private LinkedList<Color> colors = new LinkedList<>();
            /**
             * The sizes of animal to search for. If empty, all are allowed.
             */
            private LinkedList<Size> sizes = new LinkedList<>();

            public Builder() {}

            public AnimalSearchQuery build() throws PersistenceException {
                if (species == null)
                    throw new PersistenceException("Species must not be null in a search query");

                AnimalSearchQuery sq = new AnimalSearchQuery();

                sq.is("species", species);
                sq.in("gender", genders);
                if (!breed.value.isEmpty())
                    sq.has("breed", List.of(breed));
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
                if (species == null)
                    throw new IllegalArgumentException();

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
                if (gender == null)
                    throw new IllegalArgumentException();

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
                if (breed == null)
                    throw new IllegalArgumentException();

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
                if (color == null)
                    throw new IllegalArgumentException();

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
                if (size == null)
                    throw new IllegalArgumentException();

                sizes.add(size);
                return this;
            }
        }

        protected AnimalSearchQuery() {}

        /**
         * Requires that the given attribute is equal to the given value.
         * 
         * @param attributeName the attribute to check
         * @param value the value to check the attribute against
         */
        protected void is(String attributeName, DatabaseValue<?> value) {
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
        protected void in(String attributeName, List<? extends DatabaseValue<?>> values) {
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
        protected void has(String attributeName, List<? extends DatabaseValue<String>> values) {
            if (!values.isEmpty()) {
                // add query text
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < values.size() - 1; ++i)
                    sb.append(attributeName).append(" LIKE ? OR ");
                sb.append(attributeName).append(" LIKE ?");
                ands.add(sb.append(')').toString());

                // add parameters
                for (DatabaseValue<String> value : values)
                    parameters.add(new DatabaseObject<>("%" + value.toDatabaseRepresentation() + "%"));
            }
        }
    }

    /**
     * A Java value that has a database representation.
     */
    public static interface DatabaseValue<T> {
        /**
         * @return the representation of the value that
         *         should be given to the JDBC to be stored in the database
         */
        public T toDatabaseRepresentation();
    }

    /**
     * A Java value whose type is equal to what should be stored in the database.
     */
    public static class DatabaseObject<T> implements DatabaseValue<T> {
        public final T value;

        public DatabaseObject(T value) {
            this.value = value;
        }

        @Override
        public T toDatabaseRepresentation() {
            return value;
        }
    }

    /**
     * An enumeration that can be stored and retrieved from the database.
     */
    public static interface DatabaseEnum extends DatabaseValue<String> {}


}