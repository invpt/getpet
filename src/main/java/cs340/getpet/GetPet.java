package cs340.getpet;

import cs340.getpet.persistence.Persistence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class GetPet {
    private static final Logger logger = LoggerFactory.getLogger(GetPet.class);

    public static void main(String[] args) {
        Server server;
        try {
            server = new Server(new Server.Configuration.Builder()
                    .homePage("/home.html")
                    .address("localhost", 8080)
                    .databaseName(":memory:")
                    .build());
        } catch (IOException e) {
            logger.error("Failed to instantiate server", e);
            return;
        }

        try {
            server.run();
        } catch (Persistence.PersistenceException | IOException e) {
            logger.error("Exception while starting or running server", e);
        }
    }
}
