package cs340.getpet.persistence;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.gson.annotations.SerializedName;

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

    private static class SearchQuery {
        LinkedList<String> ands = new LinkedList<>();
        LinkedList<Object> parameters = new LinkedList<>();

        SearchQuery() {}

        void is(String attributeName, String value) {
            if (value != null) {
                // add condition text
                ands.add(attributeName + " = ?");
                // add parameters
                parameters.add(value);
            }
        }

        void like(String attributeName, String value) {
            if (value != null) {
                // add condition text
                ands.add(attributeName + " LIKE ?");
                // add parameters
                parameters.add("%" + value + "%");
            }
        }

        void in(String attributeName, String[] values) {
            if (values != null && values.length != 0) {
                // add condition text
                ands.add(attributeName + " IN (" + "?,".repeat(values.length - 1) + "?)");
                // add parameters
                parameters.addAll(Arrays.asList(values));
            }
        }

        void has(String attributeName, String[] values) {
            if (values != null && values.length != 0) {
                // add condition text
                StringBuilder sb = new StringBuilder("(");
                for (int i = 0; i < values.length - 1; ++i)
                    sb.append(attributeName).append(" LIKE ? OR ");
                sb.append(attributeName).append(" LIKE ?");
                ands.add(sb.append(')').toString());
                // add parameters
                for (String value : values)
                    parameters.add("%" + value + "%");
            }
        }
    }

    private static final class EnumToString {
        private static final List<Class<? extends Enum<?>>> CLASSES = List.of(Animal.Species.class, Animal.Gender.class, Animal.Color.class, Animal.Size.class);
        private static final Map<Object, Map<Object, String>> classToConstantToName = new IdentityHashMap<>();

        static {
            for (Class<? extends Enum<?>> clazz : CLASSES)
                try {
                    Map<Object, String> constantToName = new IdentityHashMap<>();
                    
                    for (Enum<?> constant : clazz.getEnumConstants()) {
                        String name = constant.name();
                        SerializedName annotation = clazz.getField(name).getAnnotation(SerializedName.class);
                        if (annotation != null) {
                            name = annotation.value();
                        }
                        constantToName.put(constant, name);
                    }
    
                    classToConstantToName.put(clazz, constantToName);
                } catch (NoSuchFieldException e) {
                    throw new AssertionError(e);
                }
        }

        public static <T extends Enum<T>> String toString(T t, Class<T> clazz) {
            return classToConstantToName.get(clazz).get(t);
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
            //PREP_STMT_INTAKE_NUMBER = conn.prepareStatement("SELECT * FROM Animals WHERE intakeNumber = ?");
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create database connection", e);
        }
    }

    public SearchResponse search(SearchRequest searchRequest) throws PersistenceException {
        SearchQuery query = new SearchQuery();
        query.is("species", searchRequest.species);
        query.in("gender", searchRequest.genders);
        query.like("breed", searchRequest.breed);
        query.has("color", searchRequest.colors);
        query.in("size", searchRequest.sizes);

        String queryString = "SELECT * FROM Animals WHERE "
                + String.join(" AND ", query.ands);

        try (PreparedStatement stmt = conn.prepareStatement(queryString)) {
            // set parameters
            int i = 1;
            for (Object parameter : query.parameters)
                stmt.setObject(i++, parameter);

            // execute query
            ResultSet resultSet = stmt.executeQuery();
            
            // put results into usable format
            ArrayList<Animal> results = new ArrayList<>();
            while (resultSet.next())
                results.add(animalFromRow(resultSet));

            return new SearchResponse(results.toArray(new Animal[0]));
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create or execute animal search statement", e);
        }
    }

    private static Animal animalFromRow(ResultSet resultSet) throws SQLException {
        if (resultSet.next())
            return new Animal.Builder()
                    .intakeNumber(resultSet.getInt("intakeNumber"))
                    .species(resultSet.getString("species"))
                    .breed(resultSet.getString("breed"))
                    .size(resultSet.getString("size"))
                    .colors(Arrays.stream(resultSet.getString("color").split(" "))
                            .toArray(String[]::new))
                    .gender(resultSet.getString("gender"))
                    .weight(resultSet.getDouble("weight"))
                    .vaccinated(resultSet.getBoolean("vaccinated"))
                    .spayNeuter(resultSet.getBoolean("spayNeuter"))
                    .name(resultSet.getString("name"))
                    .date(resultSet.getDate("date"))
                    .missing(resultSet.getBoolean("missing"))
                    .build();
        else
            return null;
    }
}
