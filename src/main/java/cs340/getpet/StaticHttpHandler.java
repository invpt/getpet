package cs340.getpet;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.*;

public class StaticHttpHandler implements HttpHandler {
    public StaticHttpHandler() {}

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        if (httpExchange.getRequestMethod().equals("GET")) {
            final String path = httpExchange.getRequestURI().getPath();
            final String resourcePath;

            // login.html is the default page
            if (path.equals("/"))
                resourcePath = "/content/login.html";
            else
                resourcePath = "/content" + path;

            if (getClass().getResource(resourcePath) != null) {
                try (InputStream inStream = getClass().getResourceAsStream(resourcePath);
                     OutputStream outStream = httpExchange.getResponseBody()) {
                    // read file data
                    byte[] data = inStream.readAllBytes();

                    // send headers
                    httpExchange.sendResponseHeaders(200, data.length);

                    // send data
                    outStream.write(data);
                }
            } else
                httpExchange.sendResponseHeaders(404, -1);
        } else
            httpExchange.sendResponseHeaders(405, -1);
    }
}
