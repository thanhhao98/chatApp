package com.muc;
import java.io.IOException;

public class ServerMain {
    public static void main(String[] args) throws IOException {
        int port = 8890;
        Server server = new Server(port);
        System.out.println("Server is running on port "+ port);
        server.run();
    }


}
