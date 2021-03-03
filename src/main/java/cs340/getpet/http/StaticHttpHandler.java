package cs340.getpet.http;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class StaticHttpHandler implements HttpHandler {
    private static final Logger logger = LoggerFactory.getLogger(StaticHttpHandler.class);

    private final String homePage;

    public StaticHttpHandler(String homePage) {
        this.homePage = homePage;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        final String requestPath = exchange.getRequestURI().getPath();
        final int responseCode;

        if (exchange.getRequestMethod().equals("GET")) {
            final String resourcePath;

            if (requestPath.equals("/"))
                resourcePath = "/content" + homePage;
            else
                resourcePath = "/content" + requestPath;

            if (getClass().getResource(resourcePath) != null) {
                try (InputStream inStream = getClass().getResourceAsStream(resourcePath);
                     OutputStream outStream = exchange.getResponseBody()) {
                    // read file data
                    byte[] data = inStream.readAllBytes();

                    // send headers
                    exchange.sendResponseHeaders(responseCode = 200, data.length);

                    // send data
                    outStream.write(data);
                }
            } else
                exchange.sendResponseHeaders(responseCode = 404, -1);
        } else
            exchange.sendResponseHeaders(responseCode = 405, -1);


        logger.info("HTTP " + responseCode + ": " + requestPath);

        exchange.close();
    }
}
