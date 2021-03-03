package cs340.getpet;

import com.sun.net.httpserver.HttpServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cs340.getpet.http.StaticHttpHandler;
import cs340.getpet.http.PersistenceHttpHandler;
import cs340.getpet.persistence.Persistence;

import java.io.*;
import java.net.InetSocketAddress;

/**
 * Serves the pages for the website as well as hosts the RESTful DB interaction API.
 */
public class Server {
    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private final Configuration configuration;
    private final HttpServer http;

    public Server(Configuration conf) throws IOException {
        configuration = conf;
        http = HttpServer.create();
    }

    public void run() throws IOException, Persistence.PersistenceException {
        // connect to database
        Persistence persistence = new Persistence(configuration.persistenceConf);

        // bind server to the address
        http.bind(configuration.address, -1);

        // create contexts
        http.createContext("/", new StaticHttpHandler(configuration.homePage));
        http.createContext("/persistence", new PersistenceHttpHandler(persistence));
        
        // start http server
        http.start();

        logger.info("Successfully started on " + configuration.address);
    }

    public static final class Configuration {
        public final String homePage;
        public final InetSocketAddress address;
        public final Persistence.Configuration persistenceConf;

        private Configuration(Builder b) {
            homePage = b.homePage;
            address = b.address;
            persistenceConf = b.persistenceConf;
        }

        public static class Builder {
            private String homePage;
            private InetSocketAddress address;
            private Persistence.Configuration persistenceConf;

            public Builder() {}
            public Configuration build() { return new Configuration(this); }
            public Builder homePage(String homePage) { this.homePage = homePage; return this; }
            public Builder address(String address, int port) { this.address = new InetSocketAddress(address, port); return this; }
            public Builder persistenceConf(Persistence.Configuration persistenceConf) { this.persistenceConf = persistenceConf; return this; }
        }
    }
}
