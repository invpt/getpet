package cs340.getpet;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.LinkedList;

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
            for (DatabaseValue parameter : query.parameters)
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

		public boolean hasNext() throws PersistenceException {
            try {
                return !resultSet.isLast();
            } catch (SQLException e) {
                throw new PersistenceException("Error while checking if at end of result set", e);
            }
		}

		public Animal next() throws PersistenceException {
            if (hasNext())
                try {
                    resultSet.next();

                    return new Animal.Builder()
                        .species(Species.fromDatabaseRepresentation(resultSet.getString("species")))
                        .breed(resultSet.getString("breed"))
                        .colors((Color[]) Arrays.stream(resultSet.getString("color").split(" ")).map(s -> Color.fromDatabaseRepresentation(s)).toArray())
                        .weight(resultSet.getDouble("weight"))
                        .vaccinated(resultSet.getBoolean("vaccinated"))
                        .spayNeuter(resultSet.getBoolean("spayneuter"))
                        .name(resultSet.getString("name"))
                        .date(resultSet.getDate("date"))
                        .missing(resultSet.getBoolean("missing"))
                        .build();
                } catch (SQLException e) {
                    throw new PersistenceException("Error while getting next search result", e);
                }
            else
                try {
                    if (!resultSet.isClosed())
                        resultSet.close();
                    return null;
                } catch (SQLException e) {
                    throw new PersistenceException("Failed to close result set", e);
                }
            
		}
    }

    /**
     * An in-memory representation of an entity from the Animals table.
     */
    public static class Animal {
        /**
         * The species of the animal.
         */
        public final Species species;
        /**
         * The breed of the animal.
         */
        public final String breed;
        /**
         * The size of the animal.
         */
        public final Size size;
        /**
         * The primary colors that the animal's fur has.
         */
        public final Color[] colors;
        /**
         * The gender of the animal.
        */
        public final Gender gender;
        /**
         * The weight of the animal.
         */
        public final double weight;
        /**
         * True if the animal is vaccinated, else false.
         */
        public final boolean vaccinated;
        /**
         * True if the animal has been spayed or neutered, else false.
         */
        public final boolean spayNeuter;
        /**
         * The name of the animal.
         */
        public final String name;
        /**
         * The date the animal was brought into the shelter.
         */
        public final Date date;
        /**
         * True if the animal is known to be missing, else false.
         */
        public boolean missing;

        public static class Builder {
            Species species;
            String breed;
            Size size;
            Color[] colors;
            Gender gender;
            Double weight;
            Boolean vaccinated;
            Boolean spayNeuter;
            String name;
            Date date;
            Boolean missing;
            
            public Builder() {}
            public Animal build() {
                if (species == null || breed == null || size == null || colors == null || gender == null || weight == null || vaccinated == null || spayNeuter == null || name == null || date == null || missing == null)
                    return null;
                else
                    return new Animal(this);
            }
            public Builder species(Species species) { this.species = species; return this; }
            public Builder breed(String breed) { this.breed = breed; return this; }
            public Builder size(Size size) { this.size = size; return this; }
            public Builder colors(Color[] colors) { this.colors = colors; return this; }
            public Builder gender(Gender gender) { this.gender = gender; return this; }
            public Builder weight(double weight) { this.weight = weight; return this; }
            public Builder vaccinated(boolean vaccinated) { this.vaccinated = vaccinated; return this; }
            public Builder spayNeuter(boolean spayNeuter) { this.spayNeuter = spayNeuter; return this; }
            public Builder name(String name) { this.name = name; return this; }
            public Builder date(Date date) { this.date = date; return this; }
            public Builder missing(boolean missing) { this.missing = missing; return this; }
        }

        Animal(Builder b) {
            species = b.species;
            vaccinated = b.vaccinated;
            breed = b.breed;
            gender = b.gender;
            name = b.name;
            colors = b.colors;
            weight = b.weight;
            missing = b.missing;
            date = b.date;
            spayNeuter = b.spayNeuter;
            size = b.size;
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
        LinkedList<DatabaseValue> parameters = new LinkedList<>();

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

        protected AnimalSearchQuery() {}

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
                    parameters.add(new DatabaseObject<>("%" + value.toDatabaseRepresentation() + "%"));
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
     * A Java value whose type is equal to what should be stored in the database.
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
    public static interface DatabaseEnum extends DatabaseValue {}

    /**
     * Either male or female.
     */
    public static enum Gender implements DatabaseEnum {
        Male, Female;

        @Override
        public Object toDatabaseRepresentation() {
            switch (this) {
                case Male: return "m";
                case Female: return "f";
            }

            // should never happen
            return null;
        }

        public static Gender fromDatabaseRepresentation(String s) {
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

        @Override
        public Object toDatabaseRepresentation() {
            switch (this) {
                case Dog: return "dog";
                case Cat: return "cat";
            }

            // should never happen
            return null;
        }

        public static Species fromDatabaseRepresentation(String s) {
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

        @Override
        public Object toDatabaseRepresentation() {
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

        public static Color fromDatabaseRepresentation(String s) {
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

        @Override
        public Object toDatabaseRepresentation() {
            switch (this) {
                case Small: return "small";
                case Medium: return "medium";
                case Large: return "large";
            }

            // should never happen
            return null;
        }

        public static Size fromDatabaseRepresentation(String s) {
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