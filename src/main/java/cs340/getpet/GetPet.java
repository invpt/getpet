package cs340.getpet;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class GetPet {
    public static void main(String[] args) throws IOException  {
        Server server = new Server("localhost", 8080);
        server.run();
    }
}
