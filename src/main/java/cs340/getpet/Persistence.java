package cs340.getpet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class Persistence {
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

    public Persistence(String server, String database, String user, String password) throws PersistenceException {
        try {
            conn = DriverManager.getConnection("jdbc:mariadb://" + server + "/" + database, user, password);
        } catch (SQLException e) {
            throw new PersistenceException("Failed to create database connection", e);
        }
    }

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

    public static class SearchQuery {
        LinkedList<String> ands = new LinkedList<>();
        LinkedList<DatabaseValue> parameters = new LinkedList<>();

        public static class Builder {
            private Species species;
            private LinkedList<Gender> genders = new LinkedList<>();
            private DatabaseObject<String> breed;
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

            public Builder species(Species species) {
                this.species = species;
                return this;
            }

            public Builder gender(Gender gender) {
                genders.add(gender);
                return this;
            }

            public Builder breed(DatabaseObject<String> breed) {
                this.breed = breed;
                return this;
            }

            public Builder color(Color color) {
                colors.add(color);
                return this;
            }

            public Builder size(Size size) {
                sizes.add(size);
                return this;
            }
        }

        protected SearchQuery() {}

        protected void is(String attributeName, DatabaseValue value) {
            if (value != null) {
                // add query text
                ands.add(new StringBuilder(attributeName).append(" = ?").toString());

                // add parameters
                parameters.add(value);
            }
        }

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

    public static interface DatabaseValue {
        public Object toDatabaseRepresentation();
    }

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

    public static interface DatabaseEnum extends DatabaseValue {
        public String toDatabaseString();
        public DatabaseEnum fromDatabaseString(String s);

        @Override
        default Object toDatabaseRepresentation() {
            return toDatabaseString();
        }
    }

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

        public Gender fromDatabaseString(String s) {
            switch (s) {
                case "m": return Male;
                case "f": return Female;
            }

            // should never happen
            return null;
        }
    }

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

        public Species fromDatabaseString(String s) {
            switch (s) {
                case "dog": return Dog;
                case "cat": return Cat;
            }

            // should never happen
            return null;
        }
    }

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

        public Color fromDatabaseString(String s) {
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

        public Size fromDatabaseString(String s) {
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