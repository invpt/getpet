package cs340.getpet.http.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs340.getpet.http.rest.Endpoint.MethodHandler;
import cs340.getpet.http.rest.Endpoint.PathVariables;

public abstract class RestHttpHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(RestHttpHandler.class);

    private final String basePath;
    private final Endpoint[] endpoints;
    private final Gson gson;

    protected RestHttpHandler(String basePath, Endpoint... endpoints) {
        this.basePath = basePath;
        this.endpoints = endpoints;
        this.gson = new Gson();
    }

    @Override
    public final void handle(HttpExchange exchange) throws IOException {
        final String absolutePath = exchange.getRequestURI().getPath();
        assert absolutePath.startsWith(basePath);
        final String endpointPath = absolutePath.substring(basePath.length());

        int responseCode = -1;
        try {
            boolean endpointFound = false;
            
            for (Endpoint endpoint : endpoints) {
                PathVariables pathVariables = endpoint.path.match(endpointPath);

                if (endpointFound = pathVariables != null) {
                    Response<?> resp = handleRequest(exchange.getRequestMethod(), pathVariables, exchange.getRequestBody(), endpoint);
                    String json = gson.toJson(resp.body);

                    exchange.sendResponseHeaders(responseCode = resp.code, json.length() == 0 ? -1 : json.length());
                    exchange.getResponseBody().write(json.getBytes(StandardCharsets.UTF_8));
                    break;
                }
            }

            if (!endpointFound)
                throw new RestException(RestException.Code.UNKNOWN_ENDPOINT);
        } catch (RestException e) {
            if (e.code == RestException.Code.INTERNAL) {
                // Something went wrong but the code is handling it properly
                logger.info("Internal exception while handling request", e);
                responseCode = 500;
            } else {
                // The client gave a bad request
                responseCode = 400;
            }
                
            // Build JSON response with error message
            JsonObject resp = new JsonObject();
            resp.addProperty("message", e.getMessage());
            String json = gson.toJson(resp);

            // Send response
            exchange.sendResponseHeaders(responseCode, json.length() == 0 ? -1 : json.length());
            exchange.getResponseBody().write(json.getBytes(StandardCharsets.UTF_8));
        } catch (RuntimeException e) {
            // Something went wrong with the code, and an exception went unhandled
            logger.info("Unhandled runtime exception - this is a bug!", e);
            throw e;  
        } finally {
            logger.info("HTTP " + responseCode + ": " + absolutePath);
            exchange.close();
        }
    }

    private Response<?> handleRequest(String method, PathVariables pathVariables, InputStream inputStream, Endpoint endpoint) throws RestException, IOException {
        try (InputStreamReader body = new InputStreamReader(inputStream)) {
            switch (method) {
                case "GET":
                    return handleRequestWithHandler(pathVariables, body, endpoint.getHandler);
                case "POST":
                    return handleRequestWithHandler(pathVariables, body, endpoint.postHandler);
                case "PUT":
                    return handleRequestWithHandler(pathVariables, body, endpoint.putHandler);
                case "DELETE":
                    return handleRequestWithHandler(pathVariables, body, endpoint.deleteHandler);
                default:
                    throw new RestException(RestException.Code.UNKNOWN_METHOD);
            }
        }
    }

    private <Req extends RequestBody, Resp extends ResponseBody> Response<Resp> handleRequestWithHandler(PathVariables pathVariables, Reader body, MethodHandler<Req, Resp> requestHandler) throws RestException {
        Req req;
        try {
            req = gson.fromJson(body, requestHandler.requestClass);
        } catch (JsonSyntaxException | NumberFormatException e) {
            throw new RestException(RestException.Code.INVALID_STRUCTURE, e);
        }

        if (req != null)
            try {
                req.validate();
            } catch (ValidationException e) {
                throw new RestException(RestException.Code.INVALID_DATA, e);
            }
        return requestHandler.handler.handle(new Request<>(pathVariables, req));
    }
}
