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
    public void handle(HttpExchange httpExchange) throws IOException {
        final String path = httpExchange.getRequestURI().getPath();
        final int responseCode;

        if (httpExchange.getRequestMethod().equals("GET")) {
            final String resourcePath;

            if (path.equals("/"))
                resourcePath = "/content" + homePage;
            else
                resourcePath = "/content" + path;

            if (getClass().getResource(resourcePath) != null) {
                try (InputStream inStream = getClass().getResourceAsStream(resourcePath);
                     OutputStream outStream = httpExchange.getResponseBody()) {
                    // read file data
                    byte[] data = inStream.readAllBytes();

                    // send headers
                    httpExchange.sendResponseHeaders(responseCode = 200, data.length);

                    // send data
                    outStream.write(data);
                }
            } else
                httpExchange.sendResponseHeaders(responseCode = 404, -1);
        } else
            httpExchange.sendResponseHeaders(responseCode = 405, -1);


        logger.info("HTTP " + responseCode + ": " + path);
    }
}
