package cs340.getpet.http.rest;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;

import com.google.gson.Gson;
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

        try {
            for (Endpoint endpoint : endpoints) {
                PathVariables pathVariables = endpoint.path.match(endpointPath);

                if (pathVariables != null) {
                    Response<?> resp = handleFor(exchange.getRequestMethod(), pathVariables, exchange.getRequestBody(), endpoint);
                    String json = gson.toJson(resp.body);

                    exchange.sendResponseHeaders(resp.code, json.length() == 0 ? -1 : json.length());
                    exchange.getResponseBody().write(json.getBytes(StandardCharsets.UTF_8));
                    break;
                }
            }
        } catch(RestException e) {

        } finally {
            exchange.close();
        }
    }

    private Response<?> handleFor(String method, PathVariables pathVariables, InputStream inputStream, Endpoint endpoint) throws RestException, IOException {
        try (InputStreamReader body = new InputStreamReader(inputStream)) {
            switch (method) {
                case "GET":
                    return fuck(pathVariables, body, endpoint.getHandler);
                case "POST":
                    return fuck(pathVariables, body, endpoint.postHandler);
                case "PUT":
                    return fuck(pathVariables, body, endpoint.putHandler);
                case "DELETE":
                    return fuck(pathVariables, body, endpoint.deleteHandler);
                default:
                    throw new RestException(RestException.Code.UNKNOWN_METHOD);
            }
        }
    }

    private <Req extends RequestBody, Resp extends ResponseBody> Response<Resp> fuck(PathVariables pathVariables, Reader body, MethodHandler<Req, Resp> requestHandler) throws RestException {
        Req req = gson.fromJson(body, requestHandler.requestClass);
        req.validate();
        return requestHandler.handler.handle(new Request<>(pathVariables, req));
    }
}
