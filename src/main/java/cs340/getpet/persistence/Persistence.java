package cs340.getpet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class Persistence {
    Connection conn;

    public Persistence() {}

    public Response search(JSONObject request) throws InvalidRequestException {
        Object temp;

        if (!((temp = request.get("parameters")) instanceof JSONObject))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        JSONObject searchParameters = (JSONObject) temp;

        if (!((temp = searchParameters.get("species")) instanceof String))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        String species = (String) temp;

        if (!((temp = searchParameters.get("genders")) == null || temp instanceof JSONArray))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        String[] genders = temp != null ? stringArray((JSONArray) temp) : null;

        if (!((temp = searchParameters.get("breed")) == null || temp instanceof String))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        String breed = temp != null ? (String) temp : null;

        if (!((temp = searchParameters.get("colors")) == null || temp instanceof JSONArray))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        String[] colors = temp != null ? stringArray((JSONArray) temp) : null;

        if (!((temp = searchParameters.get("sizes")) == null || temp instanceof JSONArray))
            throw new InvalidRequestException(InvalidRequestKind.INVALID);
        String[] sizes = temp != null ? stringArray((JSONArray) temp) : null;

        LinkedList<String> ands = new LinkedList<>();
        LinkedList<Object> queryParameters = new LinkedList<>();

        // Species
        ands.add("species = ?");
        queryParameters.add(species);

        // Genders
        if (genders != null) {
            for (String gender : genders) {

            }
        }

        // Breed
        ands.add("breed = ?");
    }
    
    private static String[] stringArray(JSONArray jsonArray) throws InvalidRequestException {
        String[] array = new String[jsonArray.size()];
        
        Object temp;
        for (int i = 0; i < jsonArray.size(); ++i)
            if (!((temp = jsonArray.get(i)) instanceof String))
                throw new InvalidRequestException(InvalidRequestKind.INVALID);
            else
                array[i] = (String) temp;
        
        return array;
    }

    public static class Response {
        public final int code;
        public final JSONObject jsonResponse;

        Response(int code, JSONObject jsonResponse) {
            this.code = code;
            this.jsonResponse = jsonResponse;
        }
    }

    public static class InvalidRequestException extends Exception {
        public final InvalidRequestKind kind;

        InvalidRequestException(InvalidRequestKind kind) {
            this.kind = kind;
        }
    }

    public enum InvalidRequestKind {
        INVALID
    }
}
