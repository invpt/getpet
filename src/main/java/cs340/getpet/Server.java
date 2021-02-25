package cs340.getpet;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Serves the pages for the website as well as hosts the RESTful DB interaction API.
 */
public class Server {
    private final InetSocketAddress address;
    private final HttpServer http;

    private HttpContext fileContext;
    private HttpContext apiContext;

    public Server(String hostname, int port) throws IOException {
        address = new InetSocketAddress(hostname, port);
        http = HttpServer.create();
    }

    public void run() throws IOException {
        http.bind(address, -1);
        fileContext = http.createContext("/", new FileHandler());
        apiContext = http.createContext("/api", new ApiHandler());
    }

    private class FileHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            switch (httpExchange.getRequestMethod()) {
                case "GET":
            }
        }
    }

    private class ApiHandler implements HttpHandler {

        @Override
        public void handle(HttpExchange httpExchange) throws IOException {

        }
    }
}
