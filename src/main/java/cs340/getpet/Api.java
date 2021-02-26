package cs340.getpet;

import org.json.simple.JSONObject;

public class Api {
    public Api() {}

    public Response search(JSONObject request) {
        Object searchParametersObject = request.get("parameters");
        if (searchParametersObject instanceof JSONObject)
            ;
        else
            ;

    }

    public static class Response {
        public final int code;
        public final JSONObject jsonResponse;

        Response(int code, JSONObject jsonResponse) {
            this.code = code;
            this.jsonResponse = jsonResponse;
        }
    }
}
