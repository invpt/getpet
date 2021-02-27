package cs340.getpet.persistence;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;

public class PersistenceHttpHandler implements HttpHandler {
    private final Gson gson;
    private final Persistence persistence;

    public PersistenceHttpHandler() {
        gson = new Gson();
        persistence = new Persistence();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        InputStream body = exchange.getRequestBody();

        Object response = null;
        try {
            switch (exchange.getRequestURI().getPath()) {
                case "/persistence/search":
                    SearchRequest searchRequest = gson.fromJson(new InputStreamReader(body), SearchRequest.class);
                    SearchResponse searchResponse = persistence.search(searchRequest);

                    response = searchResponse;
                    break;
            }

            String responseJson = gson.toJson(response);
            exchange.sendResponseHeaders(200, responseJson.length());
            exchange.getResponseBody().write(responseJson.getBytes(StandardCharsets.UTF_8));
        } catch (Persistence.PersistenceException e) {
            
        } catch (JsonSyntaxException e) {

        }
    }
}
