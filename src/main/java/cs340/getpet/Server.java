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
        fileContext = http.createContext("/", new StaticHttpHandler());
        apiContext = http.createContext("/api", new ApiHandler());
        http.start();
    }

    private static class ApiHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }
}
