package cs340.getpet;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Serves the pages for the website as well as hosts the RESTful DB interaction API.
 */
public class Server {
    private final InetSocketAddress address;
    private final HttpServer http;
    private final Map<String, File> staticFiles;

    private HttpContext fileContext;
    private HttpContext apiContext;

    public Server(String hostname, int port, Map<String, File> staticFiles) throws IOException {
        address = new InetSocketAddress(hostname, port);
        http = HttpServer.create();
        this.staticFiles = staticFiles;
    }

    public void run() throws IOException {
        http.bind(address, -1);
        fileContext = http.createContext("/", new StaticHandler(staticFiles));
        apiContext = http.createContext("/api", new ApiHandler());
        http.start();
    }

    private static class StaticHandler implements HttpHandler {
        private final Map<String, File> pathMappings;

        public StaticHandler(Map<String, File> pathMappings) {
            this.pathMappings = pathMappings;
        }

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            if (httpExchange.getRequestMethod().equals("GET")) {
                String path = httpExchange.getRequestURI().getPath();

                if (pathMappings.containsKey(path)) {
                    File file = pathMappings.get(path);

                    httpExchange.sendResponseHeaders(200, file.length());

                    try (InputStream inputStream = new FileInputStream(pathMappings.get(path))) {
                        OutputStream outputStream = httpExchange.getResponseBody();
                        while (inputStream.available() > 0)
                            outputStream.write(inputStream.read());
                    }
                } else {
                    httpExchange.sendResponseHeaders(404, -1);
                }
            } else {
                throw new IOException("Bad thing");
            }
        }
    }

    private static class ApiHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }
}
