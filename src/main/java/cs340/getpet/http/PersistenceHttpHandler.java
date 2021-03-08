package cs340.getpet.http;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import cs340.getpet.persistence.GetAnimalRequest;
import cs340.getpet.persistence.GetAnimalResponse;
import cs340.getpet.persistence.Persistence;
import cs340.getpet.persistence.SearchRequest;
import cs340.getpet.persistence.SearchResponse;
import cs340.getpet.persistence.UpdateAnimalRequest;
import cs340.getpet.persistence.UpdateAnimalResponse;

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
        public String handle(String method, Reader body) throws Persistence.PersistenceException;
    }

    private final Map<String, Endpoint> endpoints = Map.of(
        "search", this::handleSearch, "animal", this::handleAnimal, "updateAnimal", this::updateAnimal);
    private final Gson gson;
    private final Persistence persistence;

    public PersistenceHttpHandler(Persistence persistence) {
        gson = new GsonBuilder().enableComplexMapKeySerialization().create();
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
                String responseJson = endpoint.handle(exchange.getRequestMethod(), new InputStreamReader(body));

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
        } catch (Exception e) {
            logger.info("Error parsing request JSON while servicing request to " + path, e);

            exchange.sendResponseHeaders(responseCode = 400, -1);
        }

        logger.info("HTTP " + responseCode + ": " + path);

        exchange.close();
    }

    private String handleSearch(String method, Reader body) throws Persistence.PersistenceException {
        SearchRequest searchRequest = gson.fromJson(body, SearchRequest.class);
        SearchResponse searchResponse = persistence.search(searchRequest);
        return gson.toJson(searchResponse);
    }

    private String handleAnimal(String method, Reader body) throws Persistence.PersistenceException {
        GetAnimalRequest request = gson.fromJson(body, GetAnimalRequest.class);
        GetAnimalResponse response = persistence.getAnimal(request);
        return gson.toJson(response);
    }

    private String updateAnimal(String method, Reader body) throws Persistence.PersistenceException {
        UpdateAnimalRequest request = gson.fromJson(body, UpdateAnimalRequest.class);
        UpdateAnimalResponse response = persistence.updateAnimal(request);
        return gson.toJson(response);
    }
}
