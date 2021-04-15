package cs340.getpet.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import cs340.getpet.persistence.Animal.Color;

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

    /**
     * Information for connecting to the database.
     */
    public static class Configuration {
        public final String server;
        public final String database;
        public final String user;
        public final String password;

        private Configuration(Builder b) {
            server = b.server;
            database = b.database;
            user = b.user;
            password = b.password;
        }

        public static class Builder {
            private String server, database, user, password;
            public Builder() {}
            public Configuration build() { return new Configuration(this); }
            public Builder server(String server) { this.server = server; return this; }
            public Builder database(String database) { this.database = database; return this; }
            public Builder user(String user) { this.user = user; return this; }
            public Builder password(String password) { this.password = password; return this; }
        }
    }

    /**
     * Creates a Persistence, connecting to the database using the given information.
     *
     * @param conf the configuration
     * @throws PersistenceException when the database connection could not be created
     */
    public Persistence(Configuration conf) throws PersistenceException {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://" + conf.server + "/" + conf.database, conf.user, conf.password);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create database connection", e);
        }
        
        try {
            applySampleData();
        } catch (SQLException | IOException e) {
            throw new PersistenceException("Failed to apply sample data", e);
        }
    }

    /**
     * Applies sample data to the database.
     * 
     * @throws SQLException when the application fails
     * @throws IOException when the sample data cannot be read
     */
    private void applySampleData() throws SQLException, IOException {
        Statement batch = conn.createStatement();
        String sql = new String(getClass().getResourceAsStream("/sample-db-create.sql").readAllBytes(), StandardCharsets.UTF_8);
        for (String stmt : sql.split(";")) {
            batch.addBatch(stmt.trim());
        }
        batch.executeBatch();
    }

    /**
     * Retrieves an animal fron the database using its intake number as a key.
     * 
     * @param intakeNumber the intake number of the animal
     * @return the found animal, or null if none is found
     * @throws PersistenceException when the database query fails
     */
    public Animal getAnimal(int intakeNumber) throws PersistenceException {
        String queryString = "SELECT * FROM Animals WHERE intakeNumber = ?";

        try (PreparedStatement stmt = conn.prepareStatement(queryString)) {
            stmt.setInt(1, intakeNumber);

            ResultSet resultSet = stmt.executeQuery();

            resultSet.next();

            if (!resultSet.isAfterLast())
                return animalFromRow(resultSet);
            else
                return null;
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create or execute animal search statement", e);
        }
    }

    /**
     * Searches for animals in the database using a SearchQuery.
     * 
     * @param searchRequest the search query
     * @return the list of found animals
     * @throws PersistenceException when the database query fails
     */
    public Animal[] search(SearchQuery searchRequest) throws PersistenceException {
        // LinkedLists used to build the where clause of the query
        final LinkedList<String> ands = new LinkedList<>();
        final LinkedList<Object> parameters = new LinkedList<>();

        // Functions used in conjunction with the previously defined variables
        // to build the where clause in an easy-to-understand way.
        final BiConsumer<String, String> is = (attrName, value) -> {
            if (value != null) {
                // add condition text
                ands.add(attrName + " = ?");
                // add parameters
                parameters.add(value);
            }
        };
        final BiConsumer<String, String> like = (attrName, value) -> {
            if (value != null) {
                // add condition text
                ands.add(attrName + " LIKE ?");
                // add parameters
                parameters.add("%" + value + "%");
            }
        };
        final BiConsumer<String, String[]> in = (attrName, values) -> {
            if (values != null && values.length != 0) {
                // add condition text
                ands.add(attrName + " IN (" + "?,".repeat(values.length - 1) + "?)");
                // add parameters
                parameters.addAll(Arrays.asList(values));
            }
        };
        final BiConsumer<String, String[]> has = (attrName, values) -> {
            if (values != null && values.length != 0) {
                // add condition text
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < values.length - 1; ++i)
                    sb.append(attrName).append(" LIKE ? OR ");
                sb.append(attrName).append(" LIKE ?");
                ands.add(sb.append(')').toString());
                // add parameters
                for (String value : values)
                    parameters.add("%" + value + "%");
            }
        };

        if (searchRequest.species != null)
            is.accept("species", searchRequest.species.toString());
        if (searchRequest.genders != null)
            in.accept("gender", Arrays.stream(searchRequest.genders).map(gender -> gender.toString()).collect(Collectors.toList()).toArray(new String[0]));
        if (searchRequest.breed != null)
            like.accept("breed", searchRequest.breed);
        if (searchRequest.colors != null)
            has.accept("color", Arrays.stream(searchRequest.colors).map(color -> color.toString()).collect(Collectors.toList()).toArray(new String[0]));
        if (searchRequest.sizes != null)
            in.accept("size", Arrays.stream(searchRequest.sizes).map(size -> size.toString()).collect(Collectors.toList()).toArray(new String[0]));

        String queryString = "SELECT * FROM Animals WHERE "
                + String.join(" AND ", ands)
                + " ORDER BY name";

        try (PreparedStatement stmt = conn.prepareStatement(queryString)) {
            // set parameters
            int i = 1;
            for (Object parameter : parameters)
                stmt.setObject(i++, parameter);

            // execute query
            ResultSet resultSet = stmt.executeQuery();
            
            // put results into usable format
            ArrayList<Animal> results = new ArrayList<>();
            while (resultSet.next())
                results.add(animalFromRow(resultSet));

            return results.toArray(new Animal[0]);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create or execute animal search statement", e);
        }
    }

    /**
     * Adds an animal to the database, automatically assigning it an intake number.
     * 
     * @param animal the animal to add to the database
     * @throws PersistenceException when the database update fails
     */
    public void newAnimal(Animal animal) throws PersistenceException {
        String query = "INSERT INTO Animals SET " +
                "species = ?," +
                "breed = ?," +
                "size = ?," +
                "color = ?," +
                "gender = ?," +
                "weight = ?," +
                "vaccinated = ?," +
                "spayNeuter = ?," +
                "name = ?," +
                "missing = ?," +
                "cageNumber = ?," +
                "date = ?";
        Object[] parameters = new Object[] {
                animal.species.toString(),
                animal.breed,
                animal.size.toString(),
                String.join(",", Arrays.stream(animal.colors).map(Color::toString).collect(Collectors.toList())),
                animal.gender.toString(),
                animal.weight,
                animal.vaccinated,
                animal.spayNeuter,
                animal.name,
                animal.missing,
                animal.cageNumber,
                new Date(),
        };

        try (PreparedStatement prepStmt = conn.prepareStatement(query)) {
            // make sure we're setting the right number of parameters as a sanity check
            assert prepStmt.getParameterMetaData().getParameterCount() == parameters.length;

            for (int i = 0; i < parameters.length; ++i)
                prepStmt.setObject(i + 1, parameters[i]);

            if (prepStmt.executeUpdate() != 1)
                ; // this is bad D:
        } catch (SQLException e) {
            throw new PersistenceException("Failed to update animal", e);
        }
    }

    /**
     * Updates an animal in the database using an intake number as the key.
     * 
     * @param intakeNumber the intake number of the animal to update
     * @param animal the new animal data to be stored in the corresponding row of the database table
     * @throws PersistenceException
     */
    public void updateAnimal(int intakeNumber, Animal animal) throws PersistenceException {
        String query = "UPDATE Animals SET " +
                "species = ?," +
                "breed = ?," +
                "size = ?," +
                "color = ?," +
                "gender = ?," +
                "weight = ?," +
                "vaccinated = ?," +
                "spayNeuter = ?," +
                "name = ?," +
                "missing = ?," +
                "cageNumber = ? " +
                "WHERE intakeNumber = ?";
        Object[] parameters = new Object[] {
                animal.species.toString(),
                animal.breed,
                animal.size.toString(),
                String.join(",", Arrays.stream(animal.colors).map(Color::toString).collect(Collectors.toList())),
                animal.gender.toString(),
                animal.weight,
                animal.vaccinated,
                animal.spayNeuter,
                animal.name,
                animal.missing,
                animal.cageNumber,
                intakeNumber,
        };

        try (PreparedStatement prepStmt = conn.prepareStatement(query)) {
            // make sure we're setting the right number of parameters as a sanity check
            assert prepStmt.getParameterMetaData().getParameterCount() == parameters.length;

            for (int i = 0; i < parameters.length; ++i)
                prepStmt.setObject(i + 1, parameters[i]);

            if (prepStmt.executeUpdate() != 1)
                ; // this is bad D:
        } catch (SQLException e) {
            throw new PersistenceException("Failed to update animal", e);
        }
    }

    /**
     * Removes an animal from the database, using its intake number as a key.
     * 
     * @param intakeNumber the intake number of the animal to remove
     * @throws PersistenceException when the database operation fails
     */
    public void deleteAnimal(int intakeNumber) throws PersistenceException {
        String query = "DELETE FROM Animals WHERE intakeNumber = ?";

        try (PreparedStatement prepStmt = conn.prepareStatement(query)) {
            prepStmt.setInt(1, intakeNumber);

            if (prepStmt.executeUpdate() != 1)
                ; // this is bad D:
        } catch (SQLException e) {
            throw new PersistenceException("Failed to euthanize animal", e);
        }
    }

    /**
     * Constructs an animal from a row of the Animals table.
     * 
     * @param resultSet the result set, positioned at the row that data should be retrieved from
     * @return the constructed animal
     * @throws SQLException when a field could not be found
     */
    private static Animal animalFromRow(ResultSet resultSet) throws SQLException {
        return new Animal.Builder()
                .intakeNumber(resultSet.getInt("intakeNumber"))
                .cageNumber(resultSet.getInt("cageNumber"))
                .species(Animal.Species.fromString(resultSet.getString("species")))
                .breed(resultSet.getString("breed"))
                .size(Animal.Size.fromString(resultSet.getString("size")))
                .colors(Arrays.stream(resultSet.getString("color").split(","))
                        .map(Animal.Color::fromString)
                        .toArray(Animal.Color[]::new))
                .gender(Animal.Gender.fromString(resultSet.getString("gender")))
                .weight(resultSet.getDouble("weight"))
                .vaccinated(resultSet.getBoolean("vaccinated"))
                .spayNeuter(resultSet.getBoolean("spayNeuter"))
                .name(resultSet.getString("name"))
                .date(resultSet.getDate("date"))
                .missing(resultSet.getBoolean("missing"))
                .build();
    }
}
