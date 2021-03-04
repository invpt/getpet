package cs340.getpet.http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

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
import java.util.Map;

public class PersistenceHttpHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(PersistenceHttpHandler.class);

    @FunctionalInterface
    private interface Endpoint {
        public String handle(Reader body) throws Persistence.PersistenceException;
    }

    private final Map<String, Endpoint> endpoints = Map.of(
        "search", this::handleSearch);
    private final Gson gson;
    private final Persistence persistence;

    public PersistenceHttpHandler(Persistence persistence) {
        gson = new Gson();
        this.persistence = persistence;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String path = exchange.getRequestURI().getPath();
        final InputStream body = exchange.getRequestBody();

        int responseCode = -1;
        try {
            assert path.startsWith("/persistence/");
            final String endpointPath = path.substring("/persistence/".length());

            Endpoint endpoint = endpoints.get(endpointPath);
            if (endpoint != null) {
                String responseJson = endpoint.handle(new InputStreamReader(body));

                if (responseJson != null) {
                    exchange.sendResponseHeaders(responseCode = 200, responseJson.length());
                    exchange.getResponseBody().write(responseJson.getBytes(StandardCharsets.UTF_8));
                } else
                    exchange.sendResponseHeaders(responseCode = 401, -1);
            } else
                exchange.sendResponseHeaders(responseCode = 404, -1);
        } catch (Persistence.PersistenceException e) {
            logger.info("Database error while servicing request to " + path, e);

            exchange.sendResponseHeaders(responseCode = 500, -1);
        }

        logger.info("HTTP " + responseCode + ": " + path);

        exchange.close();
    }

    private String handleSearch(Reader body) throws Persistence.PersistenceException {
        SearchRequest searchRequest = gson.fromJson(body, SearchRequest.class);
        SearchResponse searchResponse = persistence.search(searchRequest);
        return gson.toJson(searchResponse);
    }
}
