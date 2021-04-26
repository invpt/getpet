package cs340.getpet.http.rest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
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
        final String endpointPath = exchange.getRequestURI().getPath().substring(basePath.length());

        // find endpoint with which to handle the exchange
        PathVariables pathMatch = null;
        MethodHandler<?, ?> requestHandler = null;
        for (Endpoint endpoint : endpoints) {
            pathMatch = endpoint.path.match(endpointPath);
            requestHandler = endpoint.getHandlerForMethod(exchange.getRequestMethod());

            if (pathMatch != null)
                break;
        }

        // generate the response
        Response<?> resp;
        try (InputStreamReader body = new InputStreamReader(exchange.getRequestBody())) {
            if (pathMatch == null)
                throw new RestException(RestException.Code.NOT_FOUND);

            resp = handleAndValidateRequest(pathMatch, body, requestHandler);
        } catch (RestException e) {
            if (e.code == RestException.Code.INTERNAL)
                // Something went wrong but the code is handling it properly
                logger.error("Internal exception while handling request", e);
            else
                // The client gave a bad request
                logger.info("Client-caused exception while handling request", e);

            resp = Response.withBody(e.code.httpResponseCode(), new ErrorResponse(e.getMessage()));
        } catch (RuntimeException e) {
            // Something went wrong with the code, and an exception went unhandled
            logger.error("Unhandled runtime exception - this is a bug!", e);
            exchange.close();
            throw e;
        }

        // send the response
        try {
            String json = gson.toJson(resp.body);

            exchange.sendResponseHeaders(resp.code, json.length() == 0 ? -1 : json.length());
            exchange.getResponseBody().write(json.getBytes(StandardCharsets.UTF_8));
        } finally {
            exchange.close();
        }
    }

    private <Req extends RequestBody, Resp extends ResponseBody> Response<Resp> handleAndValidateRequest(
            PathVariables pathVariables, Reader body, MethodHandler<Req, Resp> requestHandler) throws RestException {
        try {
            Req req = gson.fromJson(body, requestHandler.requestClass);
            if (requestHandler.requestClass != RequestBody.EmptyRequest.class)
                validateRequest(req);
            return requestHandler.handler.handle(new Request<>(pathVariables, req));
        } catch (JsonSyntaxException e) {
            throw new RestException(RestException.Code.INVALID_STRUCTURE, e);
        } catch (ValidationException e) {
            throw new RestException(RestException.Code.INVALID_DATA, e);
        }
    }

    private <Req extends RequestBody> void validateRequest(Req request) throws ValidationException {
        try {
            request.validate();
        } catch (RuntimeException e) {
            throw new ValidationException("Uncaught exception while validating request", e);
        }
    }
}
