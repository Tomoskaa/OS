package aud6.networking;

import java.io.IOException;

public class Application {

    private final static String IP_ADDRESS_SERVER = "localhost";
    private final static int port = 4498;
    private final static String FOLDER_PATH = "D:\\OS\\resources";
    private final static String FILE_PATH_TO_RESULTS = "D:\\OS\\resources\\results.txt";

    public static void main(String[] args) {
        Server server = new Server(FILE_PATH_TO_RESULTS, port);
        server.start();
        Client c1 = new Client(IP_ADDRESS_SERVER, port, FOLDER_PATH);
        c1.start();

        Client c2 = new Client(IP_ADDRESS_SERVER, port, FOLDER_PATH);
        c2.start();
    }
}

