package cs340.getpet.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cs340.getpet.GetPet;
import cs340.getpet.persistence.Persistence;
import cs340.getpet.persistence.SearchRequest;
import cs340.getpet.persistence.SearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;

public class PersistenceHttpHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceHttpHandler.class);

    private final Map<String, Endpoint> endpoints = Map.of(
            "search", new Endpoint("POST") {
                @Override
                public String handle(Reader body) throws Persistence.PersistenceException {
                    SearchRequest searchRequest = gson.fromJson(body, SearchRequest.class);
                    SearchResponse searchResponse = persistence.search(searchRequest);
                    Arrays.stream(searchResponse.results).map(animal -> animal != null ? animal.name : null).forEach(System.out::println);
                    return gson.toJson(searchResponse);
                }
            }
    );

    private final Gson gson;
    private final Persistence persistence;

    private static abstract class Endpoint {
        public final String method;

        protected Endpoint(String method) {
            this.method = method;
        }

        abstract String handle(Reader body) throws Persistence.PersistenceException;
    }

    public PersistenceHttpHandler(Persistence persistence) {
        gson = new Gson();
        this.persistence = persistence;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String path = exchange.getRequestURI().getPath();
        String method = exchange.getRequestMethod();
        InputStream body = exchange.getRequestBody();

        try {
            if (path.startsWith("/persistence/")) {
                path = path.substring("/persistence/".length());

                Endpoint endpoint;
                if (endpoints.containsKey(path) && ((endpoint = endpoints.get(path)).method.equals(method))) {
                    String responseJson = endpoint.handle(new InputStreamReader(body));
                    exchange.sendResponseHeaders(200, responseJson.length());
                    exchange.getResponseBody().write(responseJson.getBytes(StandardCharsets.UTF_8));
                } else
                    exchange.sendResponseHeaders(404, -1);
            } else
                exchange.sendResponseHeaders(404, -1);
        } catch (Persistence.PersistenceException e) {
            // TODO: error page
            logger.error("Failed to perform search", e);
        }
    }
}
